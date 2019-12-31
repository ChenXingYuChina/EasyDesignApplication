package base

import (
	"encoding/json"
	"flag"
	"fmt"
	"sync"
)

const (
	imageModel           = `{"id":"%x"}`
	hyperlinkModel       = `{"id":`+ string(TypeUrl) +`, "url":"%s"}`
	underLineModel       = `{"id":` + string(underLineId) + `}`
	fontSizeModel        = `{"id":%d}`
	textColorModel       = `{"id":%d}`
	backgroundColorModel = `{"id":%d}`
	strikethroughModel   = `{"id":` + string(strikethroughID) + `}`
	superscriptModel     = `{"id":` + string(superscriptId) + `}`
	subscriptModel       = `{"id":` + string(subscriptId) + `}`
)

const (
	underLineId = iota
	strikethroughID
	superscriptId
	subscriptId
	fontBase
	textColorBase       = fontBase + 3
	backgroundColorBase = textColorBase + ColorNumber
	TypeUrl = backgroundColorBase + ColorNumber
	TypeImage = TypeUrl + 1
)

// rad, blue, yellow, green, purple
const ColorNumber = 5

var DataDir string

func init() {
	flag.StringVar(&DataDir, "r", "./", "数据文件存储的目录 默认当前文件夹")
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
	return []byte(fmt.Sprintf(fontSizeModel, r+fontBase)), nil
}

// 文字颜色
type TextColor int8

func (r TextColor) ResourceID() int64 {
	return int64(r) + textColorBase
}

func (r TextColor) MarshalJSON() ([]byte, error) {
	return []byte(fmt.Sprintf(textColorModel, r+textColorBase)), nil
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
type HyperLink string

func (r HyperLink) ResourceID() int64 {
	return TypeUrl
}

func (r HyperLink) MarshalJSON() ([]byte, error) {
	return []byte(fmt.Sprintf(hyperlinkModel, string(r))), nil
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
	textColorBase+3:TextColor(3),
	textColorBase+4:TextColor(4),
	backgroundColorBase:BackgroundColor(0),
	backgroundColorBase+1:BackgroundColor(1),
	backgroundColorBase+2:BackgroundColor(2),
	backgroundColorBase+3:BackgroundColor(3),
	backgroundColorBase+4:BackgroundColor(4),
}

func LoadResourcesExceptUrl(id int64) Resource {
	if uint64(id) < TypeUrl {
		return staticMap[int8(id)]
	} else if uint64(id) >= TypeImage {
		return ImageInText(id)
	}
	panic("don't load url from here")
}

var nextIdForImage int64 = TypeImage
var lockImage = new(sync.Mutex)

func NewImage(d []byte) *ImageData {
	lockImage.Lock()
	goal := nextIdForImage
	nextIdForImage++
	lockImage.Unlock()
	return (*ImageData)(&data{goal, d})
}
