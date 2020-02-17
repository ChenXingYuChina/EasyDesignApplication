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
var csJson = `{"content":"abcdefghijklmnopqrstuvwxyz","position":[0,1,2,3,4,5,6,7,8],"width":[1,1,1,1,1,1,1,1,1],"resources":{"res":[0,1,17], "urls":["test"]}}`
func TestUnMarshal(t *testing.T) {
	var cs = &ComplexString{}
	err := json.Unmarshal([]byte(csJson), cs)
	if err != nil {
		t.Error(err)
		return
	}
	t.Log(cs)
}
