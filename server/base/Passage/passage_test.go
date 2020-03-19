package Passage

import (
	. "EasyDesignApplication/server/base"
	"EasyDesignApplication/server/base/ComplexString"
	"EasyDesignApplication/server/base/MultiMedia"
	"database/sql"
	"encoding/json"
	"fmt"
	"testing"
)

const remote = false

func TestMain(m *testing.M) {
	var err error
	if !remote {
		err = SqlInit("postgres", "easyDesignNew", "easyDesign2019", "easyDesigner", "127.0.0.1")
	} else  {
		err = SqlInit("postgres", "appdb", "12345678", "app_dev", "175.24.76.161")
	}
	if err != nil {
		panic(err)
	}
	DataDir = "../../testData"
	Prepare()
	m.Run()
}

func TestNewPassage(t *testing.T) {
	p, err := NewPassage(&ComplexString.ComplexString{Content:"content"}, 0, 49, "test", 0, nil, 0)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(p.Body)
	fmt.Println(p.Id)
}

func TestLoadPassageBase(t *testing.T) {
	r := Database.QueryRow("select title, comment_number, like_number, owner, id, list_image, type from passages where id = 3")
	p, err := LoadPassageBase(r)
	if err != nil {
		t.Error(err)
	}
	t.Log(p)
}

func TestLikePassage(t *testing.T) {
	err := LikePassage(nil, 3)
	if err != nil {
		t.Error(err)
	}
}

func TestChangeTitle(t *testing.T) {
	err := InTransaction(func(tx *sql.Tx) ([]string, error) {
		return nil, ChangeTitle(tx, 3, "考试改变命运")
	})
	if err != nil {
		t.Error(err)
	}
}

func TestLoadPassageFromDisk(t *testing.T) {
	p, err := LoadPassageFromDisk(3)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(p.Body, p.Id)
}

func TestNewPassage2(t *testing.T) {
	p, err := NewPassage(&ComplexString.ComplexString{Content:"content"}, 0, 49, "test", 0, &MultiMedia.MultiMediaMetadata{Length:10, Type:1, DataIds:[]int64{0}}, 0)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(p.Body)
	fmt.Println(p.Id)
	fmt.Println(p.MediaLinked)
}

func TestLoadPassageFromDisk2(t *testing.T) {
	p, err := LoadPassageFromDisk(6)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(p.Body, p.Id, p.MediaLinked)
}

func TestLoadHottestPassageListByType(t *testing.T) {
	list, err := LoadHottestPassageListByType(0)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(list, list[0].PassageBase)
}

func TestLoadPassageListByWorkshopLast(t *testing.T) {
	list, err := LoadPassageListByWorkshopLast(3, 1, 1)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(list, list[0].PassageBase)
}

func TestLoadPassageListByTypeLast(t *testing.T) {
	list, err := LoadPassageListByTypeLast(0, 10, 0)
	if err != nil {
		t.Error(err)
		return
	}
	goal, err := json.Marshal(list)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(string(goal))
}

func TestLoadPassageListByUserAndTypeLast(t *testing.T) {
	list, err := LoadPassageListByUserAndTypeLast(48, 0, 1, 1)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(list, list[0].PassageBase)
}

func TestSearchPassageByTitle(t *testing.T) {
	list, err := SearchPassageByTitle("t", 1, 0)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(list, list[0].PassageBase)
}

func TestPrintPassages(t *testing.T) {

}
