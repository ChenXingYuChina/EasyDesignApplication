package session

import (
	"EasyDesignApplication/server/base"
	"EasyDesignApplication/server/dataNet"
	"encoding/binary"
	"fmt"
	"log"
	"os"
	"sync"
	"time"
)

type Session struct {
	User       *base.UserBase `json:"user"`
	SessionKey int64 `json:"session_key"`
	Mission    missions
	con        *dataNet.Conn
	lastActive int64
}

const userSessionDataFileName = "/ss/%x.ss"
const userSessionDataDir = "/ss"
const Day = 24*60*60*1000
var userSessionDataRealFileName string

func InitSessionDir() {
	userSessionDataRealFileName = base.DataDir + userSessionDataFileName
	base.CheckAndMakeDir(base.DataDir + userSessionDataDir)
	sessionPool.New = func() interface{} {
		return &Session{Mission: make(missions, lastOne)}
	}
}

// the mission, lastActive will load by this
func (u *Session) loadDiskPart() error {
	f, err := os.Open(fmt.Sprintf(userSessionDataRealFileName, u.User.ID))
	if err != nil {
		return err
	}
	err = binary.Read(f, binary.LittleEndian, &(u.lastActive))
	if err != nil {
		if err2 := f.Close(); err2 != nil {
			log.Println(err2)
		}
		return err
	}
	u.Mission.load(f)
	err = f.Close()
	if err != nil {
		log.Println(err)
	}
	t := time.Now().Unix()
	if u.lastActive % Day != t % Day {
		u.Mission.update(t)
	}
	u.lastActive = t
	return nil
}

func (u *Session) saveDiskPart() error {
	f, err := os.Create(fmt.Sprintf(userSessionDataRealFileName, u.User.ID))
	if err != nil {
		return err
	}
	err = binary.Write(f, binary.LittleEndian, u.lastActive)
	if err != nil {
		if err2 := f.Close(); err2 != nil {
			log.Println(err2)
		}
		return err
	}
	u.Mission.saveTo(f)
	return f.Close()
}

var sessionPool = new(sync.Pool)

func getNewSession() *Session {
	return sessionPool.Get().(*Session)
}

func recycleSession(s *Session) {
	s.User = nil
	sessionPool.Put(s)
}