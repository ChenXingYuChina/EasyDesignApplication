package comment

import (
	"EasyDesignApplication/server/base"
	"EasyDesignApplication/server/base/ComplexString"
	"database/sql"
	"errors"
	"fmt"
	"testing"
)

func TestMain(m *testing.M) {
	err := base.SqlInit("postgres", "easyDesign", "easyDesign2019", "easyDesigner", "127.0.0.1")
	if err != nil {
		panic(err)
	}
	base.DataDir = "../../testData"
	base.Prepare()
	m.Run()
}

func TestMakeCommentTo(t *testing.T) {
	if !MakeCommentTo(3, &ComplexString.ComplexString{Content:"comment2"}, 49) {
		t.Error()
	}
}

func TestLoadCommentBase(t *testing.T) {
	list, err := LoadCommentBase(3, 0,2)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(list, list[0])
}

func TestLikeComment(t *testing.T) {
	err := base.InTransaction(func(tx *sql.Tx) ([]string, error) {
		return nil, LikeComment(tx, 3, 2)
	})
	if err != nil {
		t.Error(err)
	}
}

func TestLoadCommentBaseHot(t *testing.T) {
	list, err := LoadCommentBaseHot(3)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(list, list[0])
}

func TestLoadComment(t *testing.T) {
	list, err := LoadCommentBaseHot(3)
	if err != nil {
		t.Error(err)
		return
	}
	c, err := LoadComment(list[0])
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(c.Content, c.CommentBase)
}

func TestSubCommentTo(t *testing.T) {
	err := base.InTransaction(func(tx *sql.Tx) ([]string, error) {
		return nil, SubCommentTo(tx, 3, 1, "subcomment2", 48)
	})
	if err != nil {
		t.Error(err)
	}
}

func TestLoadSubCommentByPosition(t *testing.T) {
	list, err := LoadSubCommentByPosition(3, 1, 0, 2)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(list, list[1])
}

func TestLikeSubComment(t *testing.T) {
	err := base.InTransaction(func(tx *sql.Tx) ([]string, error) {
		if !LikeSubComment(tx, 3, 1, 1) {
			return nil, errors.New("")
		}
		return nil, nil
	})
	if err != nil {
		t.Error()
	}
}

func TestLoadHotSubComment(t *testing.T) {
	list, err := LoadHotSubComment(3, 1)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(list, list[0])
}
