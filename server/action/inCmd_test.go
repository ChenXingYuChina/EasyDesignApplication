package action

import (
	"EasyDesignApplication/server/base"
	. "EasyDesignApplication/server/base/user"
	"fmt"
	"testing"
)

func TestMain(m *testing.M) {
	err := base.SqlInit("postgres", "easyDesign", "easyDesign2019", "easyDesigner", "127.0.0.1")
	if err != nil {
		fmt.Println(err)
	}
	base.DataDir = "../testData"
	base.Prepare()
	m.Run()
}

func TestSignUpPublicInCmd(t *testing.T) {
	t.Log(signUpPublicInCmd(Email("abc@abc.com"), "hello world"))
}

func TestSignUpStudentInCmd(t *testing.T) {
	t.Log(signUpStudentInCmd(Email("abc1@abc.com"), "hello world"))
}

func TestSignUpDesignerInCmd(t *testing.T) {
	t.Log(signUpDesignerInCmd(Email("abc2@abc.com"), "hello world"))
}

func TestLogin(t *testing.T) {
	pw := Password(GenPasswordInBack("hello world"))
	u, state := LoginById(36, pw)
	if state != 0 {
		t.Fatal(state)
	}
	t.Log(u)
}

func TestLoginByEmail(t *testing.T) {
	pw := Password(GenPasswordInBack("hello world"))
	e := Email("abc@abc.com")
	u, s := LoginByEmail(e, pw)
	if s != 0 {
		t.Log(s)
		return
	}
	t.Log(u)
}

func TestUpdateUser(t *testing.T) {
	pw := Password(GenPasswordInBack("hello world"))
	e := Email("abc@abc.com")
	u, _ := LoginByEmail(e, pw)
	u.HeadImage = 1
	u.BackImage = 2
	u.FansNumber = 10
	s := u.Update()
	if s != 0 {
		t.Error(s)
	}
}

func TestChangePassword(t *testing.T) {
	pw := Password(GenPasswordInBack("hello world"))
	e := Email("abc@abc.com")
	u, _ := LoginByEmail(e, pw)
	if !u.ChangePassword(pw, Password(GenPasswordInBack("hello world2"))) {
		t.Error("fall")
	}

	pw = Password(GenPasswordInBack("hello world2"))
	e = Email("abc@abc.com")
	if !u.ChangePassword(pw, Password(GenPasswordInBack("hello world"))) {
		t.Error("fall")
	}
}
