package user

import (
	"EasyDesignApplication/server/base"
	"EasyDesignApplication/server/base/ComplexString"
	"fmt"
	"testing"
)

func TestMain(m *testing.M) {
	err := base.SqlInit("postgres", "easyDesign", "easyDesign2019", "easyDesigner", "127.0.0.1")
	if err != nil {
		panic(err)
	}
	base.DataDir = "../../testData/"
	base.Prepare()
	m.Run()
}

func TestSignUp_Public(t *testing.T) {
	u := &UserBase{Email:Email("1234@123.com"), Password:Password(GenPasswordInBack("123")), Identity:&Public{Industry:"it", Position:"tester"}}
	s := u.SignUp()
	if s != 0 {
		t.Fail()
	}
	t.Log(u.ID)
}

func TestSignUp_Designer(t *testing.T) {
	u := &UserBase{Email:Email("designer@123.com"), Password:Password(GenPasswordInBack("123")), Identity:&Designer{Works:[]Work{{Industry:"it", Position:"tester"}}}}
	s := u.SignUp()
	if s != 0 {
		t.Fail()
	}
	t.Log(u.ID)
}

func TestSignUp_Student(t *testing.T) {
	u := &UserBase{Email:Email("student@123.com"),
		Password:Password(GenPasswordInBack("123")),
		Identity:&Student{Schools:[]School{
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
	u := &UserBase{ID: 48, Email:Email("designer@123.com"), Password:Password(GenPasswordInBack("123")), UserName: "test", Identity:&Designer{Works:[]Work{{Industry:"it", Position:"tester"}}}}
	s := u.Update()
	if s != 0 {
		t.Error(s)
	}
}

func TestUserBase_ChangeIdentity(t *testing.T) {
	u := &UserBase{ID: 48, Email:Email("designer@123.com"), Password:Password(GenPasswordInBack("123")), UserName: "test", Identity:&Designer{Works:[]Work{{Id:8, Industry:"it", Position:"tester"}}}}
	if !u.ChangeIdentity(&Designer{Works:[]Work{{Industry:"it", Position:"programmer", Company:"a"}, {Industry:"it", Position:"tester", Company:"x"}}}) {
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

func TestLoadDesigner(t *testing.T) {
	u := &UserBase{ID: 48, Email:Email("designer@123.com"), Password:Password(GenPasswordInBack("123")), UserName: "test"}
	f, err := LoadDesigner(u)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(f.LongDescription, f.Authorized)
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
