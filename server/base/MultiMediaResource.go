package base

import (
	"database/sql"
	"encoding/binary"
	"fmt"
	"log"
	"os"
	"reflect"
	"sync"
	"unsafe"
)

var (
	IfMedia *sql.Stmt
)

const (
	multiMediaDataDir = "/mm"
	multiMediaDataFileName = "/mm/%x.meta"
	multiMediaPieceDir = "/mp"
	multiMediaPieceFileName = "/mp/%x.data"
	imageDataDir = "/im"
	imageDataFileName = "/im/%x.img"
)

var mediaMetaFileName string
var mediaDataFileName string
var imageFileName string

func PrepareDir() {
	mediaMetaFileName = dataDir + multiMediaDataFileName
	CheckAndMakeDir(dataDir + multiMediaDataDir)
	mediaDataFileName = dataDir + multiMediaPieceFileName
	CheckAndMakeDir(dataDir + multiMediaPieceDir)
	imageFileName = dataDir + imageDataFileName
	CheckAndMakeDir(dataDir + imageDataDir)
}

func PrepareMultiMediaSQL() (uint8, error) {
	var err error
	IfMedia, err = SQLPrepare("select id from link_media where passage_id = $1")
	if err != nil {
		return 0, err
	}
	return 0, nil
}

type MultiMediaMetadata struct {
	Id      int64
	Length  int64
	Type    uint8
	DataIds []int64
}

var multiMediaMetadataPool = new(sync.Pool)

func init() {
	multiMediaMetadataPool.New = func() interface{} {
		return &MultiMediaMetadata{}
	}
}

func GetMultiMediaMetadata() *MultiMediaMetadata {
	return multiMediaMetadataPool.Get().(*MultiMediaMetadata)
}

func RecycleMultiMediaMetadata(m *MultiMediaMetadata) {
	multiMediaMetadataPool.Put(m)
}

func LoadMultiMediaMetadata(passageId int64) (*MultiMediaMetadata, error) {
	r := IfMedia.QueryRow(passageId)
	var id int64
	err := r.Scan(&id)
	if err != nil {
		return nil, err
	}
	f, err := os.Open(fmt.Sprintf(mediaMetaFileName, id))
	if err != nil {
		return nil, err
	}
	stat, err := f.Stat()
	if err != nil {
		return nil, err
	}
	goal := GetMultiMediaMetadata()
	err = binary.Read(f, binary.LittleEndian, &(goal.Type))
	if err != nil {
		RecycleMultiMediaMetadata(goal)
		return nil, err
	}
	err = binary.Read(f, binary.LittleEndian, &(goal.Length))
	if err != nil {
		RecycleMultiMediaMetadata(goal)
		return nil, err
	}
	buffer := make([]byte, stat.Size() - 5)
	_, err = f.Read(buffer)
	if err != nil {
		RecycleMultiMediaMetadata(goal)
		return nil, err
	}
	err = f.Close()
	if err != nil {
		log.Println(err)
	}
	(*reflect.SliceHeader)(unsafe.Pointer(&buffer)).Len /= 8
	(*reflect.SliceHeader)(unsafe.Pointer(&buffer)).Cap /= 8
	goal.DataIds = *((*[]int64)(unsafe.Pointer(&buffer)))
	goal.Id = id
	return goal, nil
}

type data struct {
	Id   int64
	Data []byte
}

func loadData(name string, id int64) (*data, error) {
	f, err := os.Open(name)
	if err != nil {
		return nil, err
	}
	stat, err := f.Stat()
	if err != nil {
		return nil, err
	}
	goal := &data{Id:id, Data:make([]byte, stat.Size())}
	_, err = f.Read(goal.Data)
	if err != nil {
		return nil, err
	}
	err = f.Close()
	if err != nil {
		log.Println(err)
	}
	return goal, nil
}

func saveData(name string, d []byte, id int64) error {
	f, err := os.Create(name)
	if err != nil {
		return err
	}
	_, err = f.Write(d)
	if err != nil {
		return err
	}
	return f.Close()
}

type MediaPiece data

var nextMediaPieceId int64
var mediaPieceIdLock = new(sync.Mutex)

func LoadMediaPiece(id int64) (*MediaPiece, error) {
	goal, err := loadData(fmt.Sprintf(mediaDataFileName, id), id)
	return (*MediaPiece)(goal), err
}

func NewMediaPiece(d []byte) (*MediaPiece, error) {
	mediaPieceIdLock.Lock()
	id := nextMediaPieceId
	nextMediaPieceId++
	mediaPieceIdLock.Unlock()
	err := saveData(fmt.Sprintf(mediaDataFileName, id), d, id)
	if err != nil {
		return nil, err
	}
	return (*MediaPiece)(&data{Id:id, Data:d}), nil
}

type ImageData data

var nextImageDataId int64
var imageDataIdLock = new(sync.Mutex)

func LoadImageData(id int64) (*ImageData, error) {
	goal, err := loadData(fmt.Sprintf(mediaMetaFileName, id), id)
	return (*ImageData)(goal), err
}

func NewImageData(d []byte) (*ImageData, error) {
	imageDataIdLock.Lock()
	id := nextImageDataId
	nextImageDataId++
	imageDataIdLock.Unlock()
	err := saveData(fmt.Sprintf(imageFileName, id), d, id)
	if err != nil {
		return nil, err
	}
	return (*ImageData)(&data{Id:id, Data:d}), nil
}
