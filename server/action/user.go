package action

import (
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/action/session"
	"EasyDesignApplication/server/base"
	"crypto/md5"
	"encoding/hex"
	"net/http"
	"time"
)

func SignUpInControlPlatform(w http.ResponseWriter, r *http.Request) {
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

func signUpPublicInCmd(e base.Email, password string) (*base.UserBase, uint8) {
	u := &base.UserBase{Email:e, Password:base.Password(GenPasswordInBack(password)), Identity:&base.Public{Industry:"it", Position:"tester"}}
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

func signUpStudentInCmd(e base.Email, password string) (*base.UserBase, uint8) {
	u := &base.UserBase{Email:e,
		Password:base.Password(GenPasswordInBack(password)),
		Identity:&base.Student{Schools:[]base.School{
			{
				Public:true,
				Diploma:1,
				Country:"china",
				Name:"tjhs",
			},
			{
				Public:false,
				Diploma:2,
				Country:"china",
				Name:"hebut",
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

func signUpDesignerInCmd(e base.Email, password string) (*base.UserBase, uint8) {
	u := &base.UserBase{
		Email:e,
		Password:base.Password(GenPasswordInBack(password)),
		Identity:&base.Designer{Works:[]base.Work{
			{
				Start:time.Now(),
				End:time.Now(),
				Company:"abc",
				Industry:"it",
				Position:"tester",
			},
			{
				Start:time.Now(),
				End:time.Now(),
				Company:"abcd",
				Industry:"it",
				Position:"tester",
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

func GenPasswordInBack(pw string) string {
	mid := md5.Sum([]byte(pw))
	mid[15] = 0
	for i := 0; i < 15; i++ {
		mid[15] ^= mid[i]
	}
	mid = md5.Sum(mid[:])
	return hex.EncodeToString(mid[:])
}
