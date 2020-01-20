package session

import (
	"encoding/binary"
	"log"
	"os"
	"time"
)

const (
	readPassage = iota
	likePassage
	lastOne
)

// when use ignore the last one.
var finishNum = []uint8{3, lastOne:1}
var lastUpdate = []int64{time.Now().Unix() % int64(24 * time.Hour), lastOne:1}
var updateTime = []int64{1, lastOne:1}


type missions []uint8

func (m missions) Do(num uint8) bool {
	m[num]++
	f := finishNum[num]
	if m[num] == f {
		return true
	} else if m[num] > f {
		m[num] = f
	}
	return false
}

func (m missions) update(t int64) {
	for i := 0; i < lastOne; i++ {
		if t > lastUpdate[i] {
			continue
		}
		m[i] = 0
	}
}

// todo: test first
func (m missions) load(f *os.File) {
	err := binary.Read(f, binary.LittleEndian, m)
	if err != nil {
		log.Println(err)
	}
}

func (m missions) saveTo(f *os.File) {
	err := binary.Write(f, binary.LittleEndian, m)
	if err != nil {
		log.Println(err)
	}
}
