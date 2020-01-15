package session

import (
	"log"
	"os"
	"sync"
	"testing"
)

func TestDoubleRead(t *testing.T) {
	f, err := os.Open("a.txt")
	if err != nil {
		t.Fatal(err)
	}
	wg := new(sync.WaitGroup)
	for i := 0; i < 10; i ++ {
		wg.Add(1)
		go read(f, wg, int64(i * 2))
	}
	wg.Wait()
}

func read(f *os.File, wg *sync.WaitGroup, offset int64) {
	goal := make([]byte, 2)
	_, err := f.ReadAt(goal, offset)
	if err != nil {
		log.Panic(err)
	} else {
		log.Println(goal)
	}
	wg.Done()
}
