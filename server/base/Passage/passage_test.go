package Passage

import (
	. "EasyDesignApplication/server/base"
	"database/sql"
	"fmt"
	"testing"
)

func TestMain(m *testing.M) {
	err := SqlInit("postgres", "easyDesign", "easyDesign2019", "easyDesigner", "127.0.0.1")
	if err != nil {
		panic(err)
	}
	p, err := PreparePassageSQL()
	if err != nil {
		panic(fmt.Sprintf("%e at %d", err, p))
	}
	m.Run()
}

func TestMakePassage(t *testing.T) {
	err := InTransaction(func(tx *sql.Tx) (strings []string, e error) {
		p, err := MakePassage(tx, 36, "title", 0, 0)
		if err != nil {
			return nil, err
		}
		fmt.Println(p)
		return nil, nil
	})
	if err != nil {
		t.Error(err)
	}
}

func TestLoadPassageBase(t *testing.T) {
	r := Database.QueryRow("select title, comment_number, like_number, owner, id, list_image, type from passages where id = 2")
	p, err := LoadPassageBase(r)
	if err != nil {
		t.Error(err)
	}
	if !(p.Id == 3 && p.Title == "title" && p.Owner == 36) {
		t.Fatal()
	}
	t.Log(p)
}

func TestLikePassage(t *testing.T) {
	err := LikePassage(nil, 2)
	if err != nil {
		t.Error(err)
	}
}
