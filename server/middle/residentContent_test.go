package middle

import (
	"EasyDesignApplication/server/base"
	"encoding/json"
	"fmt"
	"testing"
)

func TestMain(m *testing.M) {
	err := base.SqlInit("postgres", "easyDesign", "easyDesign2019", "easyDesigner", "127.0.0.1")
	if err != nil {
		panic(err)
	}
	base.DataDir = "../testData/"
	base.Prepare()
	m.Run()
}

func TestLoadFullPassage(t *testing.T) {
	p, err := loadFullPassage(3)
	if err != nil {
		t.Error(err)
	}
	g, err := json.Marshal(p)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(string(g))
}

func TestLoadHotPassageList(t *testing.T) {
	pl, um, err := LoadHotPassageList(0, 0, 2)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(pl[1], pl[1].PassageBase, um[1])
}

func TestLoadLastPassageListByType(t *testing.T) {
	pl, um, err := LoadLastPassageListByType(0, 0, 2)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(pl[1], pl[1].PassageBase, um[1])
}

func TestLoadFullPassageFromHot(t *testing.T) {
	fp, g := LoadFullPassageFromHot(0, 3)
	if g != nil {
		p, err := GetPassageFromFunction(g)
		if err != nil {
			t.Error(err)
			return
		}
		fmt.Println(p)
	} else {
		fmt.Println(fp)
	}
}

func TestLoadFullPassageFromHot2(t *testing.T) {
	TestLoadHotPassageList(t)
	TestLoadFullPassageFromHot(t)
}
