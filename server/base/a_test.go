package base

import (
	"fmt"
	"gitea.com/xorm/xorm"
	"strings"
	"testing"
)

func TestA(t *testing.T) {
	err := SqlInit("postgres", "easyDesign", "easyDesign2019", "easyDesigner", "127.0.0.1")
	if err != nil {
		t.Fatal(err)
	}
	r := Database.QueryRow("insert into workshop (name, head_image) values ('a', 1) returning id")
	var s int64
	err = r.Scan(&s)
	if err != nil {
		if strings.Compare(err.Error(), "sql: no rows in result set") == 0 {
			fmt.Println("right")
		}
		fmt.Println(err)
	}
	fmt.Println(s)
}

func TestXorm(t *testing.T) {
	e, err := xorm.NewEngine()
	e.Table().Where().And()
}
