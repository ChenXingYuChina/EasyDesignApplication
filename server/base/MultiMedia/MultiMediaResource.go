package MultiMedia

import (
	. "EasyDesignApplication/server/base"
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
	ifMedia   *sql.Stmt
	linkMedia *sql.Stmt
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

func PrepareMultiMediaDir() {
	mediaMetaFileName = DataDir + multiMediaDataFileName
	CheckAndMakeDir(DataDir + multiMediaDataDir)
	mediaDataFileName = DataDir + multiMediaPieceFileName
	CheckAndMakeDir(DataDir + multiMediaPieceDir)
	imageFileName = DataDir + imageDataFileName
	CheckAndMakeDir(DataDir + imageDataDir)
}

func PrepareMultiMediaSQL() (uint8, error) {
	var err error
	ifMedia, err = SQLPrepare("select id from link_media where passage_id = $1")
	if err != nil {
		return 0, err
	}
	linkMedia, err = Database.Prepare("insert into link_media (passage_id) VALUES ($1) returning id")
	if err != nil {
		return 1, err
	}
	return 0, nil
}

type MultiMediaMetadata struct {
	Id      int64 `json:"id"`
	Length  int64 `json:"length"`
	Type    uint8 `json:"type"`
	DataIds []int64
}

func LoadMultiMediaMetadata(passageId int64) (*MultiMediaMetadata, error) {
	r := ifMedia.QueryRow(passageId)
	var id int64
	err := r.Scan(&id)
	if err != nil {
		return nil, nil
	}
	f, err := os.Open(fmt.Sprintf(mediaMetaFileName, id))
	defer func() {
		err2 := f.Close()
		if err2 != nil {
			log.Println(err2)
		}
	}()
	if err != nil {
		return nil, err
	}
	stat, err := f.Stat()
	if err != nil {
		return nil, err
	}
	goal := &MultiMediaMetadata{}
	err = binary.Read(f, binary.LittleEndian, &(goal.Type))
	if err != nil {
		return nil, err
	}
	err = binary.Read(f, binary.LittleEndian, &(goal.Length))
	if err != nil {
		return nil, err
	}
	buffer := make([]byte, stat.Size() - 5)
	_, err = f.Read(buffer)
	if err != nil {
		return nil, err
	}
	(*reflect.SliceHeader)(unsafe.Pointer(&buffer)).Len /= 8
	(*reflect.SliceHeader)(unsafe.Pointer(&buffer)).Cap /= 8
	goal.DataIds = *((*[]int64)(unsafe.Pointer(&buffer)))
	goal.Id = id
	return goal, nil
}

func SaveMultiMediaMetadata(tx *sql.Tx, fileToDelete *[]string, passageId int64, media *MultiMediaMetadata)  error {
	var linkMedia = linkMedia
	if tx != nil {
		linkMedia = tx.Stmt(linkMedia)
	}
	var id int64
	r := tx.Stmt(linkMedia).QueryRow(passageId)
	err := r.Scan(&id)
	if err != nil {
		return err
	}
	fileName := fmt.Sprintf(mediaMetaFileName, id)
	f, err := os.Create(fileName)
	if err != nil {
		return err
	}
	if fileToDelete != nil {
		*fileToDelete = append(*fileToDelete, fileName)
	}
	err = binary.Write(f, binary.LittleEndian, media.Type)
	if err != nil {
		return err
	}
	err = binary.Write(f, binary.LittleEndian, media.Length)
	if err != nil {
		return err
	}
	err = binary.Write(f, binary.LittleEndian, media.DataIds)
	if err != nil {
		return err
	}
	media.Id = id
	return nil
}

type MediaPiece data

var nextMediaPieceId int64
var mediaPieceIdLock = new(sync.Mutex)

func LoadMediaPiece(id int64) (*MediaPiece, error) {
	goal, err := loadData(fmt.Sprintf(mediaDataFileName, id), id)
	return (*MediaPiece)(goal), err
}

func SaveMediaPiece(m *MediaPiece) error {
	return saveData(fmt.Sprintf(mediaDataFileName, m.Id), m.Data)
}

func DeleteMediaPiece(id int64) error {
	return os.Remove(fmt.Sprintf(mediaDataFileName, id))
}

func NewMediaPiece(d []byte) (*MediaPiece, error) {
	mediaPieceIdLock.Lock()
	id := nextMediaPieceId
	nextMediaPieceId++
	mediaPieceIdLock.Unlock()
	return (*MediaPiece)(&data{Id:id, Data:d}), nil
}


func init() {
	RegisterPrepare(func() {
		PrepareMultiMediaDir()
		PrepareMultiMediaSQL()
	})
}
