package action

import (
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/base/user"
	"encoding/json"
	"log"
	"net/http"
)

func signUp(w http.ResponseWriter, r *http.Request) {
	log.Println("call sign up")
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	log.Println(r.Form)
	email, has := httpTools.GetEmailFromForm(r.Form, "email")
	if !has {
		w.WriteHeader(400)
		return
	}
	pw, has := httpTools.GetPasswordFromForm(r.Form, "pw")
	if !has {
		w.WriteHeader(400)
		return
	}
	name, has := httpTools.GetDataFromForm(r.Form, "name")
	if !has {
		w.WriteHeader(400)
		return
	}
	identityType, has := httpTools.GetInt64FromForm(r.Form, "identityType")
	if !has {
		w.WriteHeader(400)
		return
	}
	quick, has := httpTools.GetBoolFromForm(r.Form, "quick")
	if !has {
		w.WriteHeader(400)
		return
	}
	log.Println("parse success")
	u := user.UserBase{
		Password:pw,
		Email:email,
		UserName:name,
	}
	if !quick {
		identityJson, has := httpTools.GetDataFromForm(r.Form, "identity")
		if !has {
			w.WriteHeader(400)
			return
		}
		switch identityType {
		case user.DesignerType:
			var designer user.Designer
			err = json.Unmarshal([]byte(identityJson), &designer)
			if err != nil {
				w.WriteHeader(400)
				return
			}
			u.Identity = &designer
		case user.StudentType:
			var student user.Student
			err = json.Unmarshal([]byte(identityJson), &student)
			if err != nil {
				log.Println(err)
				w.WriteHeader(400)
				return
			}
			u.Identity = &student
		case user.PublicType:
			var public user.Public
			err = json.Unmarshal([]byte(identityJson), &public)
			if err != nil {
				w.WriteHeader(400)
				return
			}
			u.Identity = &public
		}

		log.Println("full sign up success")
	} else {
		switch identityType {
		case user.PublicType:
			u.Identity = &user.Public{
				Industry: "未填写",
				Position: "未填写"}
		case user.StudentType:
			u.Identity = &user.Student{}
		case user.DesignerType:
			u.Identity = &user.Designer{}
		}
	}
	s := u.SignUp()
	message := ""
	switch s {
	case 0:
		message = "注册成功，欢迎到用户页编辑资料"
	case 1:
		message = "该邮箱已被注册"
	case 255:
		message = "内部错误"
	default:
		w.WriteHeader(500)
		return
	}
	_, err = w.Write([]byte(message))
	if err != nil {
		w.WriteHeader(500)
		log.Println(err)
		return
	}
	log.Println("sign up success")
}
