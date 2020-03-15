package MultiMedia

import (
	"EasyDesignApplication/server/base/ComplexString"
	"fmt"
	"os"
	"sync"
)

type ImageData data

func LoadImageData(id int64) (*ImageData, error) {
	goal, err := loadData(fmt.Sprintf(imageFileName, id), id)
	return (*ImageData)(goal), err
}

func SaveImageData(i *ImageData) error {
	return saveData(fmt.Sprintf(imageFileName, i.Id), i.Data)
}

func DeleteImageData(id int64) error {
	return os.Remove(fmt.Sprintf(imageFileName, id))
}

// fixme 这个地方的15需要删掉
var nextIdForImage int64 = ComplexString.TypeImage + 15
var lockImage = new(sync.Mutex)

func NewImage(d []byte) *ImageData {
	lockImage.Lock()
	goal := nextIdForImage
	nextIdForImage++
	lockImage.Unlock()
	return (*ImageData)(&data{goal, d})
}
