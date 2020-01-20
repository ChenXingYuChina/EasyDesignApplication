package MultiMedia

import (
	"EasyDesignApplication/server/base"
	"EasyDesignApplication/server/base/ComplexString"
	"fmt"
	"testing"
)

func TestMain(m *testing.M) {
	err := base.SqlInit("postgres", "easyDesign", "easyDesign2019", "easyDesigner", "127.0.0.1")
	if err != nil {
		fmt.Println(err)
		return
	}
	base.DataDir = "../../testData/"
	base.Prepare()
	m.Run()
}

func TestNewImage(t *testing.T) {
	i := NewImage([]byte{1, 2, 3})
	err := SaveImageData(i)
	if err != nil {
		t.Error(err)
	}
}

func TestLoadImageData(t *testing.T) {
	i, err := LoadImageData(ComplexString.TypeImage)
	if err != nil {
		t.Error(err)
		return
	}
	if len(i.Data) != 3 {
		t.Error(err)
	}
}

func TestDeleteImageData(t *testing.T) {
	err := DeleteImageData(ComplexString.TypeImage)
	if err != nil {
		t.Error(err)
	}
}
