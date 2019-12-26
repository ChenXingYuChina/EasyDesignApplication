package base

import (
	"database/sql"
	"github.com/lib/pq"
	"log"
	"strings"
	"sync"
	"time"
)

const (
	StudentType = iota
	DesignerType
	PublicType
)


type Password string

func (p Password) CheckRight() bool {
	// todo finish it
	return true
}

func (p Password) checkEqual(p2 Password) bool {
	return strings.EqualFold(string(p), string(p2))
}

type Email string

func (e *Email) CheckRight() bool {
	// todo finish it
	return true
}

type PhoneNumber string

func (p  PhoneNumber) CheckRight() bool {
	// todo finish it
	return true
}

type Identity interface {
	Type() uint8
}

var (
	signUp              *sql.Stmt
	changeEmail         *sql.Stmt
	updateInformation   *sql.Stmt
	changePassword      *sql.Stmt
	loginByEmail        *sql.Stmt
	loginById           *sql.Stmt
	getStudentIdentity  *sql.Stmt
	getPublicIdentity   *sql.Stmt
	getDesignerIdentity *sql.Stmt
	newStudentInfo      *sql.Stmt
	newPublic           *sql.Stmt
	newDesignerInfo     *sql.Stmt
	deleteStudentInfo   *sql.Stmt
	deleteDesignerInfo  *sql.Stmt
	updatePublicInfo    *sql.Stmt
	loadUser *sql.Stmt
)


func UserSQLPrepare() (int, error) {
	// todo use it before start
	position := 0
	var err error
	signUp, err = SQLPrepare("insert into Users (name, Email, phone_number, password, coin, head_image, identity_type) values ($1, $2, $3, $4, $5, $6, $7)")
	if err != nil {
		return position, err
	}
	position++

	changeEmail, err = SQLPrepare("update Users set Email = $1 where id = $2")
	if err != nil {
		return position, err
	}
	position++

	updateInformation, err = SQLPrepare("update Users set name = $1, phone_number = $2, head_image = $3, back_image = $4 where id = $5")
	if err != nil {
		return position, err
	}
	position++

	changePassword, err = SQLPrepare("update Users set password = $1 where id = $2")
	if err != nil {
		return position, err
	}
	position++

	loginByEmail, err = SQLPrepare("select id, name, phone_number, coin, fans_number, follower_number, passage_number, head_image, back_image, identity_type from Users where Email = $1 and password = $2")
	if err != nil {
		return position, err
	}
	position++

	loginById, err = SQLPrepare("select Email, name, phone_number, coin, fans_number, follower_number, passage_number, head_image, back_image, identity_type from Users where id = $1 and password = $2")
	if err != nil {
		return position, err
	}
	position++

	getStudentIdentity, err = SQLPrepare("select school.country, school.name, school_link.public from school_link, school where school_link.user_id = $1 and school_link.id = school.id")
	if err != nil {
		return position, err
	}
	position++

	getPublicIdentity, err = SQLPrepare("select industry, position from public_link, occupation where public_link.user_id = $1 and public_link.occupation_id = occupation.id")
	if err != nil {
		return position, err
	}
	position++

	getDesignerIdentity, err = SQLPrepare("select id, start_Time, end_time, company, Industry, position from work, occupation where work.user_id = $1 and work.occupation = occupation.id")
	if err != nil {
		return position, err
	}
	position++

	newPublic, err = SQLPrepare("insert into public_link (user_id, occupation_id) select $1, id from occupation where industry = $2 and position = $3")
	if err != nil {
		return position, err
	}
	position++

	newDesignerInfo, err = SQLPrepare("insert into work (user_id, start_time, end_time, company, occupation) select $1, $2, $3, $4, id from occupation where industry = $5 and position = $6")
	if err != nil {
		return position, err
	}
	position++

	newStudentInfo, err = SQLPrepare("insert into school_link (school_id, public, diploma, user_id) select id, $1, $2, $3 from school where name = $4 and country = $5")
	if err != nil {
		return position, err
	}
	position++

	deleteDesignerInfo, err = SQLPrepare("delete from work where id = $1")
	if err != nil {
		return position, err
	}
	position++

	deleteStudentInfo, err = SQLPrepare("delete from school_link where school_id in (select id from school where name = $1 and country = $2) and diploma = $3 and user_id = $4")
	if err != nil {
		return position, err
	}
	position++

	updatePublicInfo, err = SQLPrepare("update public_link set (occupation_id) = (select id from occupation where industry = $1 and position = $2) where user_id = $3")
	if err != nil {
		return position, err
	}
	position++

	loadUser, err = SQLPrepare("select Email, name, phone_number, coin, fans_number, follower_number, passage_number, head_image, back_image, identity_type from Users where id = $1")
	if err != nil {
		return position, err
	}
	position++

	return 0, nil
}

type UserBase struct {
	ID int64
	UserName string
	Password Password
	Phone PhoneNumber
	Email Email
	Coin uint64
	FansNumber uint64  // 粉丝
	FollowerNumber uint64  // 关注
	PassageNumber uint64  // 文章总数
	HeadImage int64
	BackImage int64
	Identity Identity
}

func (u *UserBase) UserType() uint8 {
	return u.Identity.Type()
}

func (u *UserBase) ChangePassword(oldPassword, newPassword Password) bool {
	if u.Password.checkEqual(oldPassword) && newPassword.CheckRight() {
		_, err := changePassword.Exec(string(newPassword), u.ID)
		if err != nil {
			log.Println(err)
			return false
		}
		return true
	}
	return false
}

func (u *UserBase) ChangeEmail(email Email) uint8 {
	//r, err := changeEmail.Query(string(email), u.ID)
	r, err := changeEmail.Exec(string(email), u.ID)
	if err != nil {
		if strings.Compare(string(err.(*pq.Error).Code) ,"23505") == 0 {
			return 1  // email 重复
		}
		log.Println(err)
		return 255
	}
	line, err := r.RowsAffected()
	if err != nil || line > 1 {
		log.Println(err)
		return 255  // 未知错误
	}
	if line != 1 {
		return 2  // 账户不存在
	}
	return 0
}

func (u *UserBase) SignUp() uint8 {
	//r, err := signUp.Query(u.UserName, string(u.Email), string(u.Phone), string(u.Password), u.Coin, u.HeadImage, u.Identity.Type())
	tx, err := Database.Begin()
	if err != nil {
		return 255
	}
	_, err = tx.Stmt(signUp).Exec(u.UserName, string(u.Email), string(u.Phone), string(u.Password), u.Coin, u.HeadImage, u.Identity.Type())
	if err != nil {
		if strings.Compare(string(err.(pq.Error).Code) ,"23505") == 0 {
			_ = tx.Rollback()
			return 1  // email 重复
		}
		// todo add other check
		log.Println(err)
		_ = tx.Rollback()
		return 255
	}
	switch i := u.Identity.(type) {
	case *Student:
		q := tx.Stmt(newStudentInfo)
		for _, v := range i.Schools {
			_, err = q.Exec(v.public, v.Diploma, u.ID, v.Name, v.Country)  // pdunc
			if err != nil {
				log.Println(err)
				err = tx.Rollback()
				if err != nil {
					log.Println(err)
				}
				return 255
			}
		}
	case *Public:
		q := tx.Stmt(newPublic)
		_, err = q.Exec(u.ID, i.Industry, i.Position)
		if err != nil {
			log.Println(err)
			err = tx.Rollback()
			if err != nil {
				log.Println(err)
			}
			return 255
		}
	case *Designer:
		q := tx.Stmt(newDesignerInfo)
		for _, v := range i.Works {
			_, err = q.Exec(u.ID, v.Start, v.End, v.Company, v.Industry, v.Position)  // pdunc
			if err != nil {
				log.Println(err)
				err = tx.Rollback()
				if err != nil {
					log.Println(err)
				}
				return 255
			}
		}
	}
	return 0
}

func (u *UserBase) Update() uint8 {
	//r, err := updateInformation.Query(u.UserName, string(u.Phone), u.HeadImage, u.BackImage, u.Identity.Type())
	r, err := updateInformation.Exec(u.UserName, string(u.Phone), u.HeadImage, u.BackImage, u.ID)
	if err != nil {
		log.Println(err)
		return 255  // 未知原因
	}
	line, err := r.RowsAffected()
	if err != nil {
		log.Println(err)
		return 255
	}
	if line != 1 {
		return 2  // 账户异常
	}
	return 0
}

func (u *UserBase) ChangeIdentity(new Identity, keepLocation map[uint8]uint8) bool {
	IdentityType := new.Type()
	if u.Identity.Type() == IdentityType {
		tx, err := Database.Begin()
		if err != nil {
			return false
		}
		switch i := u.Identity.(type) {
		case *Student:
			dsi := tx.Stmt(deleteStudentInfo)
			isi := tx.Stmt(newStudentInfo)
			s := new.(*Student)
			notAdd := map[uint8]struct{}{}
			for i, v := range i.Schools {
				if position, ok := keepLocation[uint8(i)]; !ok {
					_, err := dsi.Exec(v.Name, v.Country, v.Diploma, u.ID)
					if err != nil {
						log.Println(err)
						err = tx.Rollback()
						if err != nil {
							log.Println(err)
						}
						return false
					}
				} else {
					notAdd[position] = struct{}{}
				}
			}
			for i, v := range s.Schools {
				if _, ok := notAdd[uint8(i)]; !ok {
					_, err = isi.Exec(v.public, v.Diploma, u.ID, v.Name, v.Country)
					if err != nil {
						log.Println(err)
						err = tx.Rollback()
						if err != nil {
							log.Println(err)
						}
						return false
					}
				}
			}
		case *Public:
			p := u.Identity.(*Public)
			_, err = tx.Stmt(updatePublicInfo).Exec(p.Industry, p.Position, u.ID)
			if err != nil {
				log.Println(err)
				err = tx.Rollback()
				if err != nil {
					log.Println(err)
				}
				return false
			}
		case *Designer:
			dsi := tx.Stmt(deleteDesignerInfo)
			isi := tx.Stmt(newDesignerInfo)
			s := new.(*Designer)
			notAdd := map[uint8]struct{}{}
			for i, v := range i.Works {
				if position, ok := keepLocation[uint8(i)]; !ok {
					_, err := dsi.Exec(v.Id)
					if err != nil {
						log.Println(err)
						err = tx.Rollback()
						if err != nil {
							log.Println(err)
						}
						return false
					}
				} else {
					notAdd[position] = struct{}{}
				}
			}
			for i, v := range s.Works {
				if _, ok := notAdd[uint8(i)]; !ok {
					_, err = isi.Exec(u.ID, v.Start, v.End, v.Company, v.Industry, v.Position)
					if err != nil {
						log.Println(err)
						err = tx.Rollback()
						if err != nil {
							log.Println(err)
						}
						return false
					}
				}
			}
		}
		err = tx.Commit()
		if err != nil {
			return false
		}
		u.Identity = new
		return true
	}
	return false
}

func (u *UserBase) loadIdentity(identityType uint8) uint8 {
	var state uint8
	u.Identity, state = loadIdentity(identityType, u.ID)
	return state
}

func (u *UserBase) ToMini() *UserMini {
	um := MakeUserMini()
	um.Identity = u.Identity
	um.HeadImage = u.HeadImage
	um.UserName = u.UserName
	um.UserId = u.ID
	return um
}

func loadIdentity(identityType uint8, id int64) (goal Identity, state uint8) {
	var r *sql.Rows
	var err error
	switch identityType {
	case StudentType:
		r, err = getStudentIdentity.Query(id)
		if err != nil {
			log.Println(err)
			return nil, 255
		}
		student := &Student{make([]School, 0, 3)}
		for r.Next() {
			var country, name string
			var public bool
			err = r.Scan(&country, &name, &public)
			if err != nil {
				log.Println(err)
				return nil, 255
			}
			student.Schools = append(student.Schools, School{Country:country, Name:name, public:public})
		}
		goal = student
	case PublicType:
		r, err = getPublicIdentity.Query(id)
		if err != nil {
			log.Println(err)
			return nil, 255
		}
		public := &Public{}
		if r.Next() {
			err = r.Scan(&(public.Industry), &(public.Position))
			if err != nil {
				log.Println(err)
				return nil, 255
			}
		} else {
			log.Println(err)
			return nil, 255
		}
		err = r.Close()
		if err != nil {
			log.Println(err)
			return nil, 255
		}
		goal = public
	case DesignerType:
		r, err = getDesignerIdentity.Query(id)
		if err != nil {
			log.Println(err)
			return nil, 255
		}
		designer := &Designer{make([]Work, 0, 3)}
		for r.Next() {
			var startTime, endTime time.Time
			var company, industry, position string
			var id int64
			err = r.Scan(&id, &startTime, &endTime, &company, &industry, &position)
			if err != nil {
				log.Println(err)
				return nil, 255
			}
			designer.Works = append(designer.Works, Work{id, startTime, endTime, company, industry, position})
		}
		goal = designer
	}
	return goal, 0
}

func LoginByEmail(email Email, password Password) (*UserBase, uint8) {
	r, err := loginByEmail.Query(string(email), string(password))
	if err != nil {
		log.Println(err)
		return nil, 255 // 未知错误
	}
	goal := GetUser()
	var identityType uint8
	if r.Next() {
		err = r.Scan(&(goal.ID), &(goal.UserName), &(goal.Phone), &(goal.Coin), &(goal.FansNumber), &(goal.FollowerNumber), &(goal.PassageNumber), &(goal.HeadImage), &(goal.BackImage), &identityType)
		if err != nil {
			log.Println(err)
			return nil, 255
		}
	} else {
		return nil, 3  // 账户不存在
	}
	if goal.loadIdentity(identityType) != 0 {
		RecycleUser(goal)
		return nil, 255  // 未知错误
	}
	goal.Email = email
	return goal, 0
}

func LoginById(id int64, password Password) (*UserBase, uint8) {
	r, err := loginById.Query(id, string(password))
	if err != nil {
		log.Println(err)
		return nil, 255 // 未知错误
	}
	goal := GetUser()
	var identityType uint8
	if r.Next() {
		err = r.Scan(&(goal.Email), &(goal.UserName), &(goal.Phone), &(goal.Coin), &(goal.FansNumber), &(goal.FollowerNumber), &(goal.PassageNumber), &(goal.HeadImage), &(goal.BackImage), &identityType)
		if err != nil {
			log.Println(err)
			return nil, 255
		}
	} else {
		return nil, 3  // 账户不存在
	}
	if goal.loadIdentity(identityType) != 0 {
		RecycleUser(goal)
		return nil, 255  // 未知错误
	}
	goal.ID = id
	return goal, 0
}

func LoadUser(id int64) (*UserBase, uint8) {
	r, err := loadUser.Query(id)
	if err != nil {
		log.Println(err)
		return nil, 255 // 未知错误
	}
	goal := GetUser()
	var identityType uint8
	if r.Next() {
		err = r.Scan(&(goal.Email), &(goal.UserName), &(goal.Phone), &(goal.Coin), &(goal.FansNumber), &(goal.FollowerNumber), &(goal.PassageNumber), &(goal.HeadImage), &(goal.BackImage), &identityType)
		if err != nil {
			log.Println(err)
			return nil, 255
		}
	} else {
		return nil, 3  // 账户不存在
	}
	if goal.loadIdentity(identityType) != 0 {
		RecycleUser(goal)
		return nil, 255  // 未知错误
	}
	goal.ID = id
	return goal, 0
}


var UserPool = new(sync.Pool)

func init() {
	UserPool.New = func() interface{} {
		return &UserBase{}
	}
}

func GetUser() *UserBase {
	return UserPool.Get().(*UserBase)
}

func RecycleUser(u *UserBase) {
	UserPool.Put(u)
}


type School struct {
	public bool
	Diploma uint8
	Country string // 需要在移动端转化成字符串
	Name string
}

type Student struct {
	Schools []School
}

func (*Student) Type() uint8 {
	return StudentType
}

type Designer struct {
	Works []Work
}

type Work struct {
	Id int64
	Start, End time.Time
	Company, Industry, Position string
}

func (*Designer) Type() uint8 {
	return DesignerType
}

type Public struct {
	Industry, Position string
}

func (*Public) Type() uint8 {
	return PublicType
}

