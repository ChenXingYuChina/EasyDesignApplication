package user

import (
	"EasyDesignApplication/server/base"
	"EasyDesignApplication/server/base/ComplexString"
	"encoding/json"
	"fmt"
	"testing"
	"time"
)

func TestMain(m *testing.M) {
	err := base.SqlInit("postgres", "easyDesignNew", "easyDesign2019", "easyDesigner", "127.0.0.1")
	if err != nil {
		panic(err)
	}
	base.DataDir = "../../testData/"
	base.Prepare()
	m.Run()
}

func TestSignUp_Public(t *testing.T) {
	u := &UserBase{Email:Email("1234@123.com"), Password:Password(GenPasswordInBack("123")), Identity:&Public{Industry:0, Position:0}}
	s := u.SignUp()
	if s != 0 {
		t.Fail()
	}
	t.Log(u.ID)
}

func TestSignUp_Designer(t *testing.T) {
	u, s := SignUpADesigner(Email("designer@123.com"))
	if s != 0 {
		t.Fail()
	}
	t.Log(u.ID)
}

func SignUpADesigner(email Email) (*UserBase, uint8) {
	u := &UserBase{Email:email, Password:Password(GenPasswordInBack("123")), Identity:&Designer{Works:[]Work{{Industry:0, Position:0}}}}
	return u, u.SignUp()
}

func TestSignUp_Student(t *testing.T) {
	u := &UserBase{Email:Email("student@123.com"),
		Password:Password(GenPasswordInBack("123")),
		Identity:&Student{Schools:[]School{
			{
				Public:true,
				Diploma:1,
				//Country:"china",
				//Name:"tjhs",
			},
			{
				Public:false,
				Diploma:2,
				//Country:"china",
				//Name:"hebut",
			},
		}}}
	s := u.SignUp()
	if s != 0 {
		t.Fail()
	}
	t.Log(u.ID)
}

func TestLoginById(t *testing.T) {
	u, s := LoginById(48, Password(GenPasswordInBack("123")))
	if s != 0 {
		t.Error(s)
		return
	}
	fmt.Println(u)
}

func TestLoginByEmail(t *testing.T) {
	u, s := LoginByEmail(Email("designer@123.com"), Password(GenPasswordInBack("123")))
	if s != 0 {
		t.Error(s)
		return
	}
	fmt.Println(u)
}

func TestSaveUsersLongDescription(t *testing.T) {
	err := SaveUsersLongDescription(&ComplexString.ComplexString{
		Content:"description",
	}, 48)
	if err != nil {
		t.Error(err)
	}
}

func TestUserBase_Update(t *testing.T) {
	u := &UserBase{ID: 48, Email:Email("designer@123.com"), Password:Password(GenPasswordInBack("123")), UserName: "test", Identity:&Designer{Works:[]Work{{Industry:0, Position:0}}}}
	s := u.Update()
	if s != 0 {
		t.Error(s)
	}
}

func TestUserBase_ChangeIdentity(t *testing.T) {
	u := &UserBase{ID: 48, Email:Email("designer@123.com"), Password:Password(GenPasswordInBack("123")), UserName: "test", Identity:&Designer{Works:[]Work{{Industry:0, Position:0}}}}
	if !u.UpdateIdentity(&Designer{Works: []Work{{Industry: 0, Position:1, Company:"a"}, {Industry: 0, Position:0, Company:"x"}}}) {
		t.Fail()
	}
}

func TestLoadUserBase(t *testing.T) {
	u, s := LoadUserBase(48)
	if s != 0 {
		t.Error(s)
		return
	}
	fmt.Println(u.Identity)
}


func TestAuthorized(t *testing.T) {
	if !Authorized(48) {
		t.Fail()
	}
}

func TestUnauthorized(t *testing.T) {
	if !Unauthorized(48) {
		t.Fail()
	}
}

func TestUserBase_ToMini(t *testing.T) {
	u := &UserBase{Email:Email("abc@abc.com"), Password:Password(GenPasswordInBack("123")), Identity:&Designer{Works:[]Work{{Industry:0, Position:0}}}}
	fmt.Println(u.ToMini())
}

func TestGetUserMini(t *testing.T) {
	g := GetUserMini([]int64{48, 50})
	if g == nil {
		t.Fail()
		return
	}
	fmt.Println(g[0])
	fmt.Println(g[1])
	one, ok := GetOneUserMini(48)
	if ok {
		goal, err := json.Marshal(one)
		if err != nil {
			t.Error(err)
			return
		}
		fmt.Println(string(goal))
	} else {
		t.Fail()
	}
}

func TestDateJson(t *testing.T) {
	d := `{"t":"2020-02-14"}`
	var g struct{T TimeJson `json:"t"`}
	err := json.Unmarshal([]byte(d), &g)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(time.Time(g.T).Format("2006-01-02"))
}
