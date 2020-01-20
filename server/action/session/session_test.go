package session

import (
	"EasyDesignApplication/server/base"
	"net/http"
	"net/http/httptest"
	"net/url"
	"testing"
	"time"
)
var first = true
func TestMain(m *testing.M) {
	err := base.SqlInit("postgres", "easyDesign", "easyDesign2019", "easyDesigner", "127.0.0.1")
	if err != nil {
		panic(err)
	}
	base.Prepare()
	InitSessionDir()
	InitSessionTable(Config{int64(1000000), 12324})
	m.Run()
}

func TestSessionTable_Put (t *testing.T) {
	st.put(&Session{User: &base.UserBase{
		ID: 1,
	}})
	id := st.table[1][1].User.ID
	if id != 1 {
		t.Fatal(id)
	}
}

func TestSessionTable_Get(t *testing.T) {
	if first {
		TestSessionTable_Put(t)
		first = false
	}
	st.put(&Session{User: &base.UserBase{
		ID: 256,
	},
	lastActive:time.Now().Unix(),
	})
	s := st.get(256)
	id := s.User.ID
	if id != 256 {
		t.Fatal(id)
	}
}

func TestSessionTable_Delete(t *testing.T) {
	if first {
		TestSessionTable_Put(t)
		first = false
	}
	st.del(1)
	if len(st.table[1]) != 0 {
		t.Error()
	}
}

func TestMissions_Do(t *testing.T) {
	finishNum = []uint8{3, 3, 3}
	m := missions{1, 2, 3}
	missions.Do(m, 1)
	if m[1] != 3 {
		t.Error(m)
	}
}

func TestSessionTable_Clean(t *testing.T) {
	if first {
		TestSessionTable_Put(t)
		st.stopTime = 1
		st.keepTime = 0
		st.cleaner()
	} else {
		t.Log("skip because it will loop for ever")
		t.Skip()
	}
}


func TestLoginByEmail(t *testing.T) {
	w := new(httptest.ResponseRecorder)
	r := &http.Request{
		URL:&url.URL{
			Scheme:"http",
			Host:"localhost",
			Path:"LoginEmail",
		},
		PostForm:url.Values{"email":{"abc@abc.com"},"pw":{base.MakePasswordInFront("hello world")}},
	}
	LoginByEmail(w, r)
	if w.Code != 200 {
		t.Error(w.Code)
	}
}

func TestLoginWithId(t *testing.T) {
	w := new(httptest.ResponseRecorder)
	r := &http.Request{
		URL:&url.URL{
			Scheme:"http",
			Host:"localhost",
			Path:"LoginId",
		},
		PostForm:url.Values{"id":{"36"},"pw":{base.MakePasswordInFront("hello world")}},
	}
	LoginWithId(w, r)
	if w.Code != 200 {
		t.Error(w.Code)
	}
}

func TestLogout(t *testing.T) {
	if first {
		TestLoginByEmail(t)
	}
	w := new(httptest.ResponseRecorder)
	r := &http.Request{
		URL:&url.URL{
			Scheme:"http",
			Host:"localhost",
			Path:"Logout",
		},
		PostForm:url.Values{"id":{"36"}},
	}
	Logout(w, r)
	if w.Code != 200 {
		t.Error(w.Code)
	}
}