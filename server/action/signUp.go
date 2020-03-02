package action

import (
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/base/user"
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
	identity, has := httpTools.GetInt64FromForm(r.Form, "identityType")
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
	if !quick {
		switch identity {
		case user.DesignerType:
		case user.StudentType:
		case user.PublicType:
		}
	} else {
		_, err = w.Write([]byte(signUpQuick(pw, email, name, identity)))
		if err != nil {
			w.WriteHeader(500)
			log.Println(err)
			return
		}
		log.Println("quick sign up success")
	}
}

func signUpQuick(pw user.Password, email user.Email, name string, identity int64) string {
	u := user.UserBase{
		Password:pw,
		Email:email,
		UserName:name,
	}
	switch identity {
	case user.PublicType:
		u.Identity = &user.Public{
			Industry: "未填写",
			Position: "未填写"}
	case user.StudentType:
		u.Identity = &user.Student{}
	case user.DesignerType:
		u.Identity = &user.Designer{}
	}
	s := u.SignUp()
	log.Println(s)
	switch s {
	case 0:
		return "注册成功，欢迎到用户页编辑资料"
	case 1:
		return "该邮箱已被注册"
	case 255:
		return "内部错误"
	}
	return "非法"
}
