package user

import (
	"EasyDesignApplication/server/base/ComplexString"
	"fmt"
	"testing"
)

func TestOpenWorkshop(t *testing.T) {
	w, err := OpenWorkshop("test", 0, &ComplexString.ComplexString{Content:"test"}, 48)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(w)
}

func TestLoadWorkshop(t *testing.T) {
	w, err := LoadWorkshop(3)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(w.LongDescription, w.WorkShopBase)
}

func TestAddPeopleToWorkShop(t *testing.T) {
	u, s := SignUpADesigner(Email("2@designer.com"))
	if s > 1 {
		t.Error(s)
		return
	} else if s == 1 {
		u.ID = 50
	}
	if !Authorized(u.ID) {
		t.Fail()
		return
	}
	s = AddPeopleToWorkShop(48 , u.ID, 3, Manager)
	if s != 0 {
		t.Error(s)
	}
}

func TestRemovePeopleFromWorkshop(t *testing.T) {
	if s := RemovePeopleFromWorkshop(48, 50, 3); s != 0 {
		t.Error(s)
		return
	}
}

func TestChangeWorkshop(t *testing.T) {
	s := ChangeWorkshop(50, 3, 1)
	if s != 0 {
		t.Error(s)
		return
	}
}

func TestChangeWorkshopLevel(t *testing.T) {
	s := ChangeWorkshopLevel(3, 1)
	if s != 0 {
		t.Error(s)
	}
}

func TestChangeMemberPosition(t *testing.T) {
	s := ChangeMemberPosition(3, 48, 50, 2)
	if s != 0 {
		t.Error(s)
	}
}

func TestListWorkshopMember(t *testing.T) {
	ms, err := ListWorkshopMember(3)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(ms)
}

func TestUserBase_ChangePassword(t *testing.T) {
	u := &UserBase{ID:50, Email:"2@designer.com"}
	if !u.ChangePassword(Password(GenPasswordInBack("123")), Password(GenPasswordInBack("hello"))) {
		t.Fail()
	}
}

func TestLoginById2(t *testing.T) {
	u, s := LoginById(50, Password(GenPasswordInBack("hello")))
	if s != 0 {
		t.Error(s)
		return
	}
	fmt.Println(u)
}
