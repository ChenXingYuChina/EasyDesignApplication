package ComplexString

import (
	"encoding/json"
	"fmt"
	"testing"
)

var testCS = &ComplexString{
	Content:"abcdefghijklmnopqrstuvwxyz",
	Positions:[]int32{0, 1, 2, 3, 4, 5, 6, 7, 8},
	Widths:[]int32{1, 1, 1, 1, 1, 1, 1, 1, 1},
	ResourcesId:resources{
		Res:[]int64{underLineId,
			strikethroughID,
			superscriptId,
			subscriptId,
			fontBase,
			textColorBase,
			backgroundColorBase,
			TypeUrl,
			0x12,
		},
		Urls:[]string{"test"},
	},
}

func TestSaveStringsToFile(t *testing.T) {
	err := testCS.SaveComplexStringToFile("test.cs")
	if err != nil {
		t.Error(err)
	}
}

func TestLoadComplexStringFromFile(t *testing.T) {
	cs, err := LoadComplexStringFromFile("test.cs")
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(cs)
}

func TestMarshal(t *testing.T) {
	goal, err := json.Marshal(testCS)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(string(goal))
}
var csJson = `{"content":"中文测试测试测试测试测试\r\n图片正在加载中\r\n图片后","position":[14,8,4],"width":[7,2,2],"resources":{"res":[24,0,17],"urls":[null,null,"http:\/\/www.baidu.com"]}}`
func TestUnMarshal(t *testing.T) {
	var cs = &ComplexString{}
	err := json.Unmarshal([]byte(csJson), cs)
	if err != nil {
		t.Error(err)
		return
	}
	t.Log(cs)
}
