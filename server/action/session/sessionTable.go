package session

import (
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/base"
	"encoding/binary"
	"encoding/json"
	"fmt"
	"log"
	"math/rand"
	"net/http"
	"os"
	"strconv"
	"sync"
	"time"
)

var st *sessionTable
type sessionTable struct{
	table [256]map[int64]*Session
	lock [256]*sync.RWMutex
	keepTime int64
	stopTime int64
}

func (t *sessionTable) get(id int64) *Session {
	p := uint8(id)
	t.lock[p].RLock()
	defer t.lock[p].RUnlock()
	m := t.table[p]
	if s, ok := m[id]; ok {
		return s
	}
	return nil
}

func (t *sessionTable) put(session *Session) {
	p := uint8(session.User.ID)
	t.lock[p].RLock()
	t.table[p][session.User.ID] = session
	t.lock[p].RUnlock()
}

func (t *sessionTable) del(id int64) {
	p := uint8(id)
	t.lock[p].RLock()
	delete(t.table[p], id)
	t.lock[p].RUnlock()
}

func (t *sessionTable) cleaner() {
	i := 0
	ticker := time.NewTicker(time.Duration(t.stopTime))
	buffer := make([]*Session, 0 ,10)
	for {
		<-ticker.C
		t.lock[i].Lock()
		m := t.table[i]
		now := time.Now().Unix()
		for k, v := range m {
			if now - v.lastActive > t.keepTime {
				delete(m, k)
				buffer = append(buffer, v)
			}
		}
		t.lock[i].Unlock()
		for i, v := range buffer {
			err := v.saveDiskPart()
			if err != nil {
				log.Println(err)
			}
			if v.con != nil {
				err = v.con.Finish()
				if err != nil {
					log.Println(err)
				}
				v.con = nil
			}
			buffer[i] = nil
			recycleSession(v)
		}
		if len(buffer) <= 100 {
			buffer = buffer[:0]
		} else {
			buffer = make([]*Session, 0, 50)
		}
	}
}


func LoginWithId(w http.ResponseWriter, r *http.Request) {
	log.Println("login with id")
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	id, ok := httpTools.GetInt64FromForm(r.Form, "id")
	if !ok {
		w.WriteHeader(400)
		return
	}
	pw, ok := httpTools.GetPasswordFromForm(r.Form, "pw")
	if !ok {
		w.WriteHeader(400)
		return
	}
	session := st.get(id)
	if session == nil {
		u, state := base.LoginById(id, pw)
		if state != 0 {
			switch state {
			case 255:
				w.WriteHeader(500)
			case 3:
				w.WriteHeader(401)
			}
			return
		}
		session = getNewSession()
		session.User = u
		err = session.loadDiskPart()
		if err != nil {
			log.Println(err)
		}
		st.put(session)
	} else {
		session.lastActive = time.Now().Unix()
	}
	session.SessionKey = int64(rand.Uint64())
	user, err := json.Marshal(session)
	if err != nil {
		w.WriteHeader(500)
		return
	}
	_, err = w.Write(user)
	if err != nil {
		log.Println(err)
		w.WriteHeader(500)
		return
	}
	http.SetCookie(w, &http.Cookie{Name:"sessionKey", Value:strconv.FormatInt(session.SessionKey, 16), HttpOnly:true})
}

func LoginByEmail(w http.ResponseWriter, r *http.Request) {
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	email, ok := httpTools.GetEmailFromForm(r.Form, "email")
	if !ok {
		w.WriteHeader(400)
		return
	}
	pw, ok := httpTools.GetPasswordFromForm(r.Form, "pw")
	if !ok {
		w.WriteHeader(400)
		return
	}
	u, state := base.LoginByEmail(email, pw)
	if state != 0 {
		switch state {
		case 255:
			w.WriteHeader(500)
		case 3:
			w.WriteHeader(401)
		}
		return
	}
	session := st.get(u.ID)
	if session == nil {
		session = getNewSession()
		session.User = u
		err = session.loadDiskPart()
		if err != nil {
			log.Println(err)
		}
		st.put(session)
	} else {
		session.lastActive = time.Now().Unix()
	}
	session.SessionKey = int64(rand.Uint64())
	user, err := json.Marshal(session)
	if err != nil {
		w.WriteHeader(500)
		return
	}
	_, err = w.Write(user)
	if err != nil {
		log.Println(err)
		w.WriteHeader(500)
		return
	}
	http.SetCookie(w, &http.Cookie{Name:"sessionKey", Value:strconv.FormatInt(session.SessionKey, 16), HttpOnly:true})
}

func Logout(w http.ResponseWriter, r *http.Request) {
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	if id, ok := httpTools.GetInt64FromForm(r.Form, "id"); ok {
		st.del(id)
	} else {
		w.WriteHeader(400)
	}
}

func CreateFileForSignUp(id int64) error {
	f, err := os.Create(fmt.Sprintf(userSessionDataRealFileName, id))
	if err != nil {
		return err
	}
	err = binary.Write(f, binary.LittleEndian, time.Now().Unix())
	if err != nil {
		if err2 := f.Close(); err2 != nil {
			log.Println(err2)
		}
		return err
	}
	m := make(missions, lastOne)
	m.saveTo(f)
	return f.Close()
}

type Config struct {
	/*
	the max time one session will be kept after used.
	 */
	KeepTime int64

	/*
	the seed to gain a session key.
	 */
	SessionKeySeed int64
}

func InitSessionTable(config Config) {
	st = &sessionTable{}
	st.keepTime = config.KeepTime
	rand.Seed(config.SessionKeySeed)
	for i := 0; i < 256; i++ {
		st.table[i] = make(map[int64]*Session)
		st.lock[i] = new(sync.RWMutex)
	}
	st.stopTime = config.KeepTime >> 8
}
