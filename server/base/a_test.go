package base

import (
	"encoding/json"
	"fmt"
	"strings"
	"sync"
	"testing"
	"time"
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

func TestChange(t *testing.T) {
	var x = 1
	var y = 4
	go a(x)()
	go a(y)()
	y = 2
	<-time.Tick(1 *time.Second)
}

func a(one int) func() error {
	return func() error {
		<-time.Tick(1 * time.Second)
		fmt.Println(one)
		return nil
	}
}

func TestReadMap(t *testing.T) {
	m := map[int]bool{1:true, 2: false, 3: true}
	go read(m, 1)
	go read(m, 2)
	<-time.Tick(2 * time.Second)
}

func read(m map[int]bool, start int) {
	for i := start; i < 1000; i++ {
		fmt.Println(m[i%3 +1])
	}
}

func TestLock(t *testing.T) {
	lock := new(sync.RWMutex)
	go lock.RLock()
	lock.Lock()
	fmt.Println("ok")
}

func BenchmarkLock(b *testing.B) {
	lock := new(sync.RWMutex)
	for i := 0; i < b.N; i++ {
		lock.RLock()
		go func() {
			fmt.Println("b", i)
			lock.RUnlock()
			fmt.Println("c", i)
		}()
		fmt.Println("a", i)
		lock.Lock()
		lock.Unlock()
	}
	fmt.Println("ok")
}

func TestPush(t *testing.T) {
	c := make(chan error, 1)
	go func() {
		c <- nil
	}()
	fmt.Println(<-c)
}

func TestMakeComplexString(t *testing.T) {
	LoadTextResources = func(id int64) Resource {
		r, _ := LoadResourcesFromDisk(id)
		return r
	}
	cs := NewComplexString()
	cs.Content = "I am writing the code now"
	cs.Positions = []int32{2}
	cs.Widths = []int32{1}
	cs.ResourcesId = resources{[]int64{1}}
	goal, err := json.Marshal(cs)
	if err != nil {
		t.Fatal(err)
	}
	fmt.Println(string(goal))
}

func TestUnMarshalComplexString(t *testing.T) {
	data := `{"content":"I am writing the code now","position":[2],"width":[1],"resources":[6]}`
	cs := NewComplexString()
	err := json.Unmarshal([]byte(data), cs)
	if err != nil {
		t.Fatal(err)
	}
	fmt.Println(cs)
}
