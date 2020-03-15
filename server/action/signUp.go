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
		log.Println("no email")
		w.WriteHeader(400)
		return
	}
	pw, has := httpTools.GetPasswordFromForm(r.Form, "pw")
	if !has {
		log.Println("no password")
		w.WriteHeader(400)
		return
	}
	name, has := httpTools.GetDataFromForm(r.Form, "name")
	if !has {
		log.Println("no name")
		w.WriteHeader(400)
		return
	}
	identityType, has := httpTools.GetInt64FromForm(r.Form, "identityType")
	if !has {
		log.Println("no identity type")
		w.WriteHeader(400)
		return
	}
	u := user.UserBase{
		Password: pw,
		Email:    email,
		UserName: name,
	}
	identityJson, has := httpTools.GetDataFromForm(r.Form, "identity")
	if !has {
		log.Println("no identityJson")
		w.WriteHeader(400)
		return
	}
	switch identityType {
	case user.DesignerType:
		var designer user.Designer
		err = json.Unmarshal([]byte(identityJson), &designer)
		if err != nil {
			log.Println(err)
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
			log.Println(err)
			w.WriteHeader(400)
			return
		}
		u.Identity = &public
	}
	log.Println("parse success")

	s := u.SignUp()
	message := ""
	switch s {
	case 0:
		message = "注册成功，欢迎到用户页编辑资料"
		w.WriteHeader(200)
	case 1:
		message = "该邮箱已被注册"
		w.WriteHeader(200)
	case 255:
		message = "内部错误"
		w.WriteHeader(200)
	default:
		w.WriteHeader(500)
		return
	}
	_, err = w.Write([]byte(message))
	if err != nil {
		log.Println(err)
	}
	log.Println("sign up success")
}
