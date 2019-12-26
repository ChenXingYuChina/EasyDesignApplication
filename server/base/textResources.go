package base

import (
	"encoding/json"
	"flag"
	"fmt"
	"log"
	"os"
	"sync"
)

const (
	imageModel           = `{"type":0, "url":"getImage?id=%x"}`
	hyperlinkModel       = `{"type":1, "url":"%s"}`
	underLineModel       = `{"type":2}`
	fontSizeModel        = `{"type":3,"value":%d}`
	textColorModel       = `{"type":4,"value":%d}`
	backgroundColorModel = `{"type":5,"value":%d}`
	strikethroughModel   = `{"type":6}`
	superscriptModel     = `{"type":7}`
	subscriptModel       = `{"type":8}`
	varyResourceDirName  = `/vr`
	varyResourcesFileName = `/vr/%x.res`
)

const (
	underLineId = iota
	strikethroughID
	superscriptId
	subscriptId
	fontBase
	textColorBase       = fontBase + 3
	backgroundColorBase = textColorBase + ColorNumber
	varyResourceBase = backgroundColorBase + ColorNumber
)

const ColorNumber = 5

var resFileName string
var DataDir string

func init() {
	flag.StringVar(&DataDir, "o", "./", "数据文件存储的目录 默认当前文件夹")
	resFileName = DataDir + varyResourcesFileName
	CheckAndMakeDir(DataDir + varyResourceDirName)
}


type Resource interface {
	ResourceID() int64
	json.Marshaler
}

// 图片
type ImageInText int64

func (r ImageInText) ResourceID() int64 {
	return int64(r)
}

func (r ImageInText) MarshalJSON() ([]byte, error) {
	return []byte(fmt.Sprintf(imageModel, int64(r))), nil
}

// 下划线
type UnderLine struct{}

func (UnderLine) ResourceID() int64 {
	return underLineId
}

func (UnderLine) MarshalJSON() ([]byte, error) {
	return []byte(underLineModel), nil
}

// 字体大小
type FontSize int8

func (r FontSize) ResourceID() int64 {
	return int64(r) + fontBase
}

func (r FontSize) MarshalJSON() ([]byte, error) {
	return []byte(fmt.Sprintf(fontSizeModel, r)), nil
}

// 文字颜色
type TextColor int8

func (r TextColor) ResourceID() int64 {
	return int64(r) + textColorBase
}

func (r TextColor) MarshalJSON() ([]byte, error) {
	return []byte(fmt.Sprintf(textColorModel, r)), nil
}

//背景色
type BackgroundColor int8

func (r BackgroundColor) ResourceID() int64 {
	return int64(r) + backgroundColorBase
}

func (r BackgroundColor) MarshalJSON() ([]byte, error) {
	return []byte(fmt.Sprintf(backgroundColorModel, r)), nil
}

//超级链接
type HyperLink struct {
	Url string
	Id  int64
}

func (r *HyperLink) ResourceID() int64 {
	return r.Id
}

func (r *HyperLink) MarshalJSON() ([]byte, error) {
	return []byte(fmt.Sprintf(hyperlinkModel, r.Url)), nil
}


// 删除线
type Strikethrough struct {}

func (Strikethrough) ResourceID() int64 {
	return strikethroughID
}

func (Strikethrough) MarshalJSON() ([]byte, error) {
	return []byte(strikethroughModel), nil
}



// 上角标
type Superscript struct {}

func (Superscript) ResourceID() int64 {
	return superscriptId
}

func (Superscript) MarshalJSON() ([]byte, error) {
	return []byte(superscriptModel), nil
}


// 下角标
type Subscript struct {}

func (Subscript) ResourceID() int64 {
	return subscriptId
}

func (Subscript) MarshalJSON() ([]byte, error) {
	return []byte(subscriptModel), nil
}

var LoadTextResources func(id int64) Resource
var staticMap = map[int8]Resource {
	underLineId : UnderLine{},
	strikethroughID: Strikethrough{},
	superscriptId: Superscript{},
	subscriptId: Subscript{},
	fontBase: FontSize(0),
	fontBase+1:FontSize(1),
	fontBase+2:FontSize(2),
	textColorBase:TextColor(0),
	textColorBase+1:TextColor(1),
	textColorBase+2:TextColor(2),
	backgroundColorBase:BackgroundColor(0),
	backgroundColorBase+1:BackgroundColor(1),
	backgroundColorBase+2:BackgroundColor(2),
}

func LoadResourcesFromDisk(id int64) (Resource, error) {
	if id < varyResourceBase {
		if id < 0 {
			return ImageInText(id), nil
		}
		return staticMap[int8(id)], nil
	}
	f, err := os.Open(fmt.Sprintf(resFileName, id))
	if err != nil {
		return nil, err
	}
	stat, err := f.Stat()
	if err != nil {
		return nil, err
	}
	buffer := make([]byte, stat.Size())
	_, err = f.Read(buffer)
	if err != nil {
		return nil, err
	}
	err = f.Close()
	if err != nil {
		log.Println(err)
	}
	return &HyperLink{string(buffer), id}, nil
}

var nextIdForImage int64 = -1
var nextIdForUrl int64 = 1
var lockImage = new(sync.Mutex)
var lockUrl = new(sync.Mutex)

func getNextImageId() int64 {
	lockImage.Lock()
	goal := nextIdForImage
	nextIdForImage--
	lockImage.Unlock()
	return goal
}

func getNextUrlId() int64 {
	lockUrl .Lock()
	goal := nextIdForUrl
	nextIdForUrl++
	lockUrl.Unlock()
	return goal
}


const (
	TypeImage = iota
	TypeUrl

)

func NewResource(Type uint8, url string) Resource {
	switch Type {
	case TypeImage:
		return ImageInText(getNextImageId())
	case TypeUrl:
		return &HyperLink{Url:url, Id:getNextUrlId()}
	}
	panic("no this type")
}

func SaveResourceToDisk(r Resource) error {
	id := r.ResourceID()
	if id < varyResourceBase {
		return nil
	}
	link := r.(*HyperLink)
	fileName := fmt.Sprintf(resFileName, id)
	f, err := os.Create(fileName)
	if err != nil {
		return err
	}
	_, err = f.WriteString(link.Url)
	if err != nil {
		_ = f.Close()
		return err
	}
	return f.Close()
}