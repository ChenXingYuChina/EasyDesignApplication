package middle

import (
	"EasyDesignApplication/server/base"
	"fmt"
	"testing"
	"time"
)

func BenchmarkTime1(b *testing.B) {
	t := int64(0)
	for i := 0; i < b.N; i++ {
		t += time.Now().Unix()
	}
	fmt.Println(t)
}

func BenchmarkTime2(b *testing.B) {
	t := int64(0)
	for i := 0; i < b.N; i++ {
		t += time.Now().UnixNano()
	}
	fmt.Println(t)
}

func TestCol(t *testing.T) {
	err := base.SqlInit("postgres", "easyDesign", "easyDesign2019", "easyDesigner", "127.0.0.1")
	if err != nil {
		t.Fatal(err)
	}
	r, err := base.Database.Query("select id, identity_type from users")
	if err != nil {
		t.Fatal(err)
	}
	var x, y int
	cols := []interface{}{&x, &y}
	r.Next()
	err = r.Scan(cols...)
	r.ColumnTypes()
	if err != nil {
		t.Fatal(err)
	}
	fmt.Println(x, y)
}
