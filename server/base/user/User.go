package user

import (
	. "EasyDesignApplication/server/base"
	"database/sql"
	"github.com/lib/pq"
	"log"
	"strings"
	"time"
)

const (
	StudentType = iota
	DesignerType
	PublicType
)

type Identity interface {
	Type() uint8
}

var (
	signUp              *sql.Stmt
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
	loadUser            *sql.Stmt
	newDesigner         *sql.Stmt
)

func userSQLPrepare() (uint8, error) {
	// todo use it before start
	position := uint8(0)
	var err error
	signUp, err = SQLPrepare("insert into Users (name, Email, password, identity_type) values ($1, $2, $3, $4) returning id")
	if err != nil {
		return position, err //0
	}
	position++

	updateInformation, err = Database.Prepare("update Users set name = $1, head_image = $2, back_image = $3 where id = $4")
	if err != nil {
		return position, err //1
	}
	position++

	changePassword, err = Database.Prepare("update Users set password = $1 where id = $2")
	if err != nil {
		return position, err //2
	}
	position++

	loginByEmail, err = Database.Prepare("select id, name, coin, fans_number, follower_number, passage_number, head_image, back_image, identity_type from Users where Email = $1 and password = $2")
	if err != nil {
		return position, err //3
	}
	position++

	loginById, err = Database.Prepare("select Email, name, coin, fans_number, follower_number, passage_number, head_image, back_image, identity_type from Users where id = $1 and password = $2")
	if err != nil {
		return position, err //4
	}
	position++

	getStudentIdentity, err = Database.Prepare("select school_country, school_name, public, diploma from school_link where school_link.user_id = $1")
	if err != nil {
		return position, err //5
	}
	position++

	getPublicIdentity, err = Database.Prepare("select industry, position from public_link where public_link.user_id = $1")
	if err != nil {
		return position, err //6
	}
	position++

	getDesignerIdentity, err = Database.Prepare("select start_Time, end_time, company, industry, position from work where work.user_id = $1")
	if err != nil {
		return position, err //7
	}
	position++

	newPublic, err = Database.Prepare("insert into public_link (user_id, industry, position) values ($1, $2, $3)")
	if err != nil {
		return position, err //8
	}
	position++

	newDesignerInfo, err = Database.Prepare("insert into work (user_id, start_time, end_time, company, industry, position) values ($1, $2, $3, $4, $5, $6)")
	if err != nil {
		return position, err //9
	}
	position++

	newStudentInfo, err = Database.Prepare("insert into school_link (school_country, school_name, public, diploma, user_id) values ($1, $2, $3, $4, $5)")
	if err != nil {
		return position, err //10
	}
	position++

	deleteDesignerInfo, err = Database.Prepare("delete from work where user_id = $1")
	if err != nil {
		return position, err //11
	}
	position++

	deleteStudentInfo, err = Database.Prepare("delete from school_link where user_id = $1")
	if err != nil {
		return position, err //12
	}
	position++

	updatePublicInfo, err = Database.Prepare("update public_link set industry = $1, position = $2 where user_id = $3")
	if err != nil {
		return position, err //13
	}
	position++

	loadUser, err = Database.Prepare("select Email, name, coin, fans_number, follower_number, passage_number, head_image, back_image, identity_type from Users where id = $1")
	if err != nil {
		return position, err //14
	}
	position++

	newDesigner, err = Database.Prepare("insert into unauthorized (user_id) values ($1)")
	if err != nil {
		return position, err //15
	}
	position++
	return 0, nil
}

type UserBase struct {
	ID             int64  `json:"id"`
	UserName       string `json:"name"`
	Password       Password
	Email          Email    `json:"email"`
	Coin           uint64   `json:"coin"`
	FansNumber     uint64   `json:"fans_number"`    // 粉丝
	FollowerNumber uint64   `json:"follow_number"`  // 关注
	PassageNumber  uint64   `json:"passage_number"` // 文章总数
	HeadImage      int64    `json:"head_image"`
	BackImage      int64    `json:"back_image"`
	Identity       Identity `json:"identity"`
}

func (u *UserBase) UserType() uint8 {
	return u.Identity.Type()
}

func (u *UserBase) ChangePassword(oldPassword, newPassword Password) bool {
	if !u.Password.checkEqual(oldPassword) {
		_, err := changePassword.Exec(string(newPassword), u.ID)
		if err != nil {
			log.Println(err)
			return false
		}
		return true
	}
	return false
}

func (u *UserBase) SignUp() uint8 {
	//r, err := signUp.Query(u.UserName, string(u.Email), string(u.Phone), string(u.Password), u.Coin, u.HeadImage, u.Identity.Type())
	tx, err := Database.Begin()
	if err != nil {
		return 255
	}
	r := tx.Stmt(signUp).QueryRow(u.UserName, string(u.Email), string(u.Password), u.Identity.Type())
	if err != nil {
		if strings.Compare(string(err.(*pq.Error).Code), "23505") == 0 {
			_ = tx.Rollback()
			return 1 // email 重复
		}
		// todo add other check
		log.Println(err)
		_ = tx.Rollback()
		return 255
	} else {
		err = r.Scan(&(u.ID))
		if err != nil {
			if strings.Compare(string(err.(*pq.Error).Code), "23505") == 0 {
				_ = tx.Rollback()
				return 1 // email 重复
			}
			log.Println(err)
			_ = tx.Rollback()
			return 255
		}
	}
	switch i := u.Identity.(type) {
	case *Student:
		q := tx.Stmt(newStudentInfo)
		for _, v := range i.Schools {
			_, err = q.Exec(v.Country, v.Name, v.Public, v.Diploma, u.ID) // pdunc
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
			_, err = q.Exec(u.ID, time.Time(v.Start), time.Time(v.End), v.Company, v.Industry, v.Position) // pdunc
			if err != nil {
				log.Println(err)
				err = tx.Rollback()
				if err != nil {
					log.Println(err)
				}
				return 255
			}
		}
		q = tx.Stmt(newDesigner)
		_, err = q.Exec(u.ID)
		if err != nil {
			log.Println(err)
			err = tx.Rollback()
			if err != nil {
				log.Println(err)
			}
			return 255
		}
	}
	err = tx.Commit()
	if err != nil {
		log.Println(err)
		return 255
	}
	return 0
}

func (u *UserBase) Update() uint8 {
	//r, err := updateInformation.Query(u.UserName, string(u.Phone), u.HeadImage, u.BackImage, u.Identity.Type())
	r, err := updateInformation.Exec(u.UserName, u.HeadImage, u.BackImage, u.ID)
	if err != nil {
		log.Println(err)
		return 255 // 未知原因
	}
	line, err := r.RowsAffected()
	if err != nil {
		log.Println(err)
		return 255
	}
	if line != 1 {
		return 2 // 账户异常
	}
	return 0
}

func (u *UserBase) UpdateInTransition(tx *sql.Tx) error {
	updateInformation := tx.Stmt(updateInformation)
	r, err := updateInformation.Exec(u.UserName, u.HeadImage, u.BackImage, u.ID)
	if err != nil {
		return err // 未知原因
	}
	line, err := r.RowsAffected()
	if err != nil {
		return err
	}
	if line != 1 {
		return err // 账户异常
	}
	return nil
}

func (u *UserBase) UpdateIdentity() bool {
	return InTransaction(func(tx *sql.Tx) (i []string, e error) {
		return nil, u.changeIdentity(tx)
	}) == nil
}

func (u *UserBase) changeIdentity(tx *sql.Tx) (err error) {
	switch identity := u.Identity.(type) {
	case *Student:
		_, err = tx.Stmt(deleteStudentInfo).Exec(u.ID)
		if err != nil {
			return
		}
		isi := tx.Stmt(newStudentInfo)
		for _, v := range identity.Schools {
			_, err = isi.Exec(v.Country, v.Name, v.Public, v.Diploma, u.ID)
			if err != nil {
				return
			}
		}
	case *Public:
		_, err = tx.Stmt(updatePublicInfo).Exec(identity.Industry, identity.Position, u.ID)
		if err != nil {
			log.Println(err)
			err = tx.Rollback()
			if err != nil {
				log.Println(err)
			}
			return
		}
	case *Designer:
		_, err = tx.Stmt(deleteDesignerInfo).Exec(u.ID)
		if err != nil {
			return
		}
		isi := tx.Stmt(newDesignerInfo)
		for _, v := range identity.Works {
			_, err = isi.Exec(u.ID, v.Start, v.End, v.Company, v.Industry, v.Position)
			if err != nil {
				return
			}

		}
	}
	return nil
}

func (u *UserBase) loadIdentity(identityType uint8) uint8 {
	var state uint8
	u.Identity, state = loadIdentity(identityType, u.ID)
	return state
}

func (u *UserBase) ToMini() *UserMini {
	return &UserMini{Identity: u.Identity.Type(), UserId: u.ID, HeadImage: u.HeadImage, UserName: u.UserName}
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
			var country, name int32
			var public bool
			var diploma uint8
			err = r.Scan(&country, &name, &public, &diploma)
			if err != nil {
				log.Println(err)
				return nil, 255
			}
			student.Schools = append(student.Schools, School{Country: country, Name: name, Public: public, Diploma: diploma})
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
			var company string
			var industry, position int32
			err = r.Scan(&startTime, &endTime, &company, &industry, &position)
			if err != nil {
				log.Println(err)
				return nil, 255
			}
			designer.Works = append(designer.Works, Work{
				Start:    TimeJson(startTime),
				End:      TimeJson(endTime),
				Company:  company,
				Industry: industry,
				Position: position,
			})
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
	goal := &UserBase{}
	var identityType uint8
	if r.Next() {
		err = r.Scan(&(goal.ID), &(goal.UserName), &(goal.Coin), &(goal.FansNumber), &(goal.FollowerNumber), &(goal.PassageNumber), &(goal.HeadImage), &(goal.BackImage), &identityType)
		if err != nil {
			log.Println(err)
			return nil, 255
		}
	} else {
		return nil, 3 // 账户不存在
	}
	if goal.loadIdentity(identityType) != 0 {
		return nil, 255 // 未知错误
	}
	goal.Email = email
	return goal, 0
}

func LoginById(id int64, password Password) (*UserBase, uint8) {
	r := loginById.QueryRow(id, string(password))
	goal := &UserBase{}
	var identityType uint8
	err := r.Scan(&(goal.Email), &(goal.UserName), &(goal.Coin), &(goal.FansNumber), &(goal.FollowerNumber), &(goal.PassageNumber), &(goal.HeadImage), &(goal.BackImage), &identityType)
	if err != nil {
		log.Println(err)
		return nil, 255
	}
	goal.ID = id
	state := goal.loadIdentity(identityType)
	if state != 0 {
		log.Println(state)
		return nil, 255 // 未知错误
	}
	return goal, 0
}

func LoadUserBase(id int64) (*UserBase, uint8) {
	r, err := loadUser.Query(id)
	if err != nil {
		log.Println(err)
		return nil, 255 // 未知错误
	}
	goal := &UserBase{ID: id}
	var identityType uint8
	if r.Next() {
		err = r.Scan(&(goal.Email), &(goal.UserName), &(goal.Coin), &(goal.FansNumber), &(goal.FollowerNumber), &(goal.PassageNumber), &(goal.HeadImage), &(goal.BackImage), &identityType)
		if err != nil {
			log.Println(err)
			return nil, 255
		}
	} else {
		return nil, 3 // 账户不存在
	}
	if goal.loadIdentity(identityType) != 0 {
		return nil, 255 // 未知错误
	}
	return goal, 0
}

type School struct {
	Public  bool  `json:"public"`
	Diploma uint8 `json:"diploma"`
	Country int32 `json:"country"`
	Name    int32 `json:"name"`
}

type Student struct {
	Schools []School `json:"schools"`
}

func (*Student) Type() uint8 {
	return StudentType
}

type Designer struct {
	Works []Work `json:"works"`
}

type Work struct {
	Start    TimeJson `json:"start"`
	End      TimeJson `json:"end"`
	Company  string   `json:"company"`
	Industry int32    `json:"industry"`
	Position int32    `json:"position"`
}

type TimeJson time.Time

func (t *TimeJson) UnmarshalJSON(m []byte) (err error) {
	goal, err := time.Parse(`"2006-01-02"`, string(m))
	*t = TimeJson(goal)
	return
}

func (t *TimeJson) MarshalJSON() ([]byte, error) {
	return []byte(time.Time(*t).Format(`"2006-01-02"`)), nil
}

func (*Designer) Type() uint8 {
	return DesignerType
}

type Public struct {
	Industry int32 `json:"industry"`
	Position int32 `json:"position"`
}

func (*Public) Type() uint8 {
	return PublicType
}

// Deprecated: just for test
