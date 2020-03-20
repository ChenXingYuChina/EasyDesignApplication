package action

import (
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/action/session"
	. "EasyDesignApplication/server/base/user"
	"EasyDesignApplication/server/middle"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"strconv"
	"time"
)

func signUpInControlPlatform(w http.ResponseWriter, r *http.Request) {
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	e, ok := httpTools.GetEmailFromForm(r.Form, "email")
	if !ok {
		w.WriteHeader(400)
		return
	}
	u, state := signUpPublicInCmd(e, "hello world")
	switch state {
	case 0:
		w.Write([]byte("success"))
	case 1:
		w.Write([]byte("email has signed"))
	default:
		w.WriteHeader(400)
	}
	session.CreateFileForSignUp(u.ID)
}

func signUpPublicInCmd(e Email, password string) (*UserBase, uint8) {
	u := &UserBase{Email: e, Password: Password(GenPasswordInBack(password)), Identity: &Public{Industry: 0, Position: 0}}
	s := u.SignUp()
	if s != 0 {
		return nil, s
	}
	err := session.CreateFileForSignUp(u.ID)
	if err != nil {
		return nil, 255
	}
	return u, 0
}

func signUpStudentInCmd(e Email, password string) (*UserBase, uint8) {
	u := &UserBase{Email: e,
		Password: Password(GenPasswordInBack(password)),
		Identity: &Student{Schools: []School{
			{
				Public:  true,
				Diploma: 1,
				Country: 0,
				Name:    0,
			},
			{
				Public:  false,
				Diploma: 2,
				Country: 1,
				Name:    0,
			},
		}}}
	s := u.SignUp()
	if s != 0 {
		return nil, s
	}
	err := session.CreateFileForSignUp(u.ID)
	if err != nil {
		return nil, 255
	}
	return u, 0
}

func signUpDesignerInCmd(e Email, password string) (*UserBase, uint8) {
	u := &UserBase{
		Email:    e,
		Password: Password(GenPasswordInBack(password)),
		Identity: &Designer{Works: []Work{
			{
				Start:    TimeJson(time.Now()),
				End:      TimeJson(time.Now()),
				Company:  "abc",
				Industry: 0,
				Position: 0,
			},
			{
				Start:    TimeJson(time.Now()),
				End:      TimeJson(time.Now()),
				Company:  "abcd",
				Industry: 0,
				Position: 0,
			},
		}},
	}
	s := u.SignUp()
	if s != 0 {
		return nil, s
	}
	err := session.CreateFileForSignUp(u.ID)
	if err != nil {
		return nil, 255
	}
	return u, 0
}

func userMini(w http.ResponseWriter, r *http.Request) {
	log.Println("call user mini")
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	log.Println(r.Form)
	if idString, has := r.Form["id"]; has {
		if len(idString) == 0 {
			w.WriteHeader(400)
			return
		}
		id, err := strconv.ParseInt(idString[0], 10, 64)
		if err != nil {
			w.WriteHeader(400)
			return
		}
		userMini, err := middle.LoadUserMiniNow(id)
		if err != nil {
			w.WriteHeader(500)
			return
		}
		goal, err := json.Marshal(userMini)
		if err != nil {
			w.WriteHeader(500)
			return
		}
		log.Println(string(goal))
		_, _ = w.Write(goal)
	} else {
		w.WriteHeader(400)
	}
}

func loadUserDescription(w http.ResponseWriter, r *http.Request) {
	log.Println("call load user desciption")
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	id, has := httpTools.GetInt64FromForm(r.Form, "id")
	if !has {
		w.WriteHeader(400)
		return
	}
	c, err := LoadUserDescription(id)
	log.Println(c)
	goal, err := json.Marshal(c)
	if err != nil {
		w.WriteHeader(400)
		return
	}
	fmt.Println(string(goal))
	_, err = w.Write(goal)
	if err != nil {
		log.Println(err)
		return
	}
}

func loadUser(w http.ResponseWriter, r *http.Request) {
	log.Println("call load user")
	err := r.ParseForm()
	log.Println(r.Form)
	if err != nil {
		w.WriteHeader(400)
		return
	}
	id, has := httpTools.GetInt64FromForm(r.Form, "id")
	if !has {
		w.WriteHeader(400)
		return
	}
	u, s := LoadUserBase(id)
	if s == 0 {
		goal, err := json.Marshal(u)
		if err != nil {
			w.WriteHeader(500)
			return
		}
		log.Println(string(goal))
		_, err = w.Write(goal)
		if err != nil {
			w.WriteHeader(500)
			return
		}
	}else if s == 3 {
		w.WriteHeader(404)
	} else {
		w.WriteHeader(500)
	}
}
