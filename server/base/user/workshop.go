package user

import (
	. "EasyDesignApplication/server/base"
	"database/sql"
	"errors"
	"log"
	"strings"
)

var (
	addPeopleToWorkshopStepInsert      *sql.Stmt
	addPeopleToWorkshopStepUpdate      *sql.Stmt
	removePeopleFromWorkshopStepDelete *sql.Stmt
	removePeopleFromWorkshopStepUpdate *sql.Stmt
	changeWorkshopImage                *sql.Stmt
	changeWorkshopLevel                *sql.Stmt
	loadWorkshop                       *sql.Stmt
	loadPosition                       *sql.Stmt
	listMember                         *sql.Stmt
	selectPosition                     *sql.Stmt
	changePosition *sql.Stmt
)

func prepareWorkshopSQL() (uint8, error) {
	var err error
	addPeopleToWorkshopStepInsert, err = SQLPrepare("insert into workshop_member_link (workshop_id, user_id, position) values ($1, $2, $3)")
	if err != nil {
		return 0, err
	}
	addPeopleToWorkshopStepUpdate,err = SQLPrepare("update workshop set people_number = people_number + 1 where id = $1")
	if err != nil {
		return 6, err
	}
	removePeopleFromWorkshopStepDelete, err = SQLPrepare("delete from workshop_member_link where user_id = $1 and workshop_id = $2")
	if err != nil {
		return 1, err
	}
	removePeopleFromWorkshopStepUpdate, err = SQLPrepare("update workshop set people_number = people_number - 1 where id = $1")
	if err != nil {
		return 5, err
	}
	changeWorkshopImage, err = SQLPrepare("update workshop set head_image = $1 where id = $2")
	if err != nil {
		return 2, err
	}
	loadWorkshop, err = SQLPrepare("select level, people_number, passage_number, fans_number, head_image, name from workshop where id = $1")
	if err != nil {
		return 3, err
	}
	loadPosition, err = SQLPrepare("select position, user_id from workshop_member_link where workshop_id = $1")
	if err != nil {
		return 4, err
	}
	listMember, err = Database.Prepare("select user_id, position from workshop_member_link where workshop_id = $1")
	if err != nil {
		return 7, err
	}
	selectPosition, err = Database.Prepare(`select position from workshop_member_link where user_id = $1 and workshop_id = $2;`)
	if err != nil {
		return 8, err
	}
	changePosition, err = Database.Prepare("update workshop_member_link set position = $1 where user_id = $2")
	if err != nil {
		return 9, err
	}
	changeWorkshopLevel, err = SQLPrepare("update workshop set level = $1 where id = $2")
	if err != nil {
		return 10, err
	}
	return 0, nil
}

type WorkShopBase struct {
	Member        []int64
	Position      []int16
	Id            int64
	Name          string
	HeadImage     int64
	FansNumber    int32
	PassageNumber int32
	Level         int16
}

const (
	Success = iota
	E发起方不存在
	E越权
	E工作组不存在
	E受邀者未受认证
	E不得增加组长
	E未知错误 = 255
)

func openWorkShop(tx *sql.Tx, who int64, name string, headImage int64) (*WorkShopBase, uint8) {
	row := tx.QueryRow("select true from unauthorized, users where (id = $1 and identity_type = 1) and user_id = $1", who)
	var s bool
	err := row.Scan(&s)
	if err == nil && s {
		return nil, 1  //  账户未经认证
	}
	if strings.Compare(err.Error(), "sql: no rows in result set") != 0 {
		return nil, 255  //  未知错误
	}
	row = tx.QueryRow("insert into workshop (name, head_image) values ($1, $2) returning id", name, headImage)
	var workshopId int64
	err = row.Scan(&workshopId)
	if err != nil {
		return nil, 255
	}

	r, err := tx.Exec("insert into workshop_member_link (workshop_id, user_id, position) values ($1, $2, 2)", workshopId, who)
	line, err := r.RowsAffected()
	if err != nil || line != 1 {
		return nil, 255
	}
	if err != nil {
		return nil, 255  // 未知原因
	}
	return &WorkShopBase{Id:workshopId, Member:[]int64{who}, Position:[]int16{1}, Name:name, HeadImage:headImage, FansNumber:0, PassageNumber:0, Level: 0}, 0
}

func AddPeopleToWorkShop(do, who int64, workshop int32, position int16) uint8 {
	_, s := checkPower(do, workshop, Manager)
	if s != 0 {
		return s
	}
	if !CheckAuthorized(who) {
		return E受邀者未受认证
	}
	if position == Owner {
		return E不得增加组长
	}
	err := InTransaction(func(tx *sql.Tx) ([]string, error) {
		r, err := tx.Stmt(addPeopleToWorkshopStepInsert).Exec(workshop, who, position)
		if err != nil {
			return nil, err
		}
		line, err := r.RowsAffected()
		if err != nil || line != 1 {
			return nil, err
		}
		r, err = tx.Stmt(addPeopleToWorkshopStepUpdate).Exec(workshop)
		if err != nil {
			return nil, err
		}
		line, err = r.RowsAffected()
		if err != nil || line != 1 {
			return nil, err
		}
		return nil, nil
	})
	if err != nil {
		return E未知错误
	}
	return Success
}

func RemovePeopleFromWorkshop(do, who int64, workshop int32) uint8 {
	doerPosition, s := checkPower(do, workshop, Manager)
	if s != 0 {
		return s
	}
	doeePosition, s := checkPower(who, workshop, Normal)
	if s != 0 {
		return s
	}
	if doerPosition <= doeePosition {
		return E越权
	}
	err := InTransaction(func(tx *sql.Tx) (i []string, e error) {
		r, err := tx.Stmt(removePeopleFromWorkshopStepDelete).Exec(who, workshop)
		if err != nil {
			return nil, err
		}
		line, err := r.RowsAffected()
		if err != nil || line != 1 {
			return nil, err
		}
		r, err = tx.Stmt(removePeopleFromWorkshopStepUpdate).Exec(workshop)
		if err != nil {
			return nil, err
		}
		line, err = r.RowsAffected()
		if err != nil || line != 1 {
			return nil, err
		}
		return nil, nil
	})
	if err != nil {
		return E未知错误
	}
	return Success
}

func ChangeWorkshop(do int64, workshop int32, headImage int64) uint8 {
	_, s := checkPower(do, workshop, Manager)
	if s != 0 {
		return s
	}
	r, err := changeWorkshopImage.Exec(headImage, workshop)
	if err != nil {
		return E未知错误
	}
	line, err := r.RowsAffected()
	if err != nil || line != 1 {
		return E未知错误
	}
	return Success
}

/*
just call from the control platform
 */
func ChangeWorkshopLevel(workshop int32, level int16) uint8 {
	r, err := changeWorkshopLevel.Exec(level, workshop)
	if err != nil {
		return E未知错误
	}
	if l, err := r.RowsAffected(); err != nil {
		return E未知错误
	} else if l != 1 {
		return E工作组不存在
	}
	return Success
}

func loadWorkshopBase(id int64) (*WorkShopBase, error) {
	r := loadWorkshop.QueryRow(id)
	b := &WorkShopBase{}
	var length int16
	err := r.Scan(&(b.Level), &length, &(b.PassageNumber), &(b.FansNumber), &(b.HeadImage), &(b.Name))
	if err != nil {
		return nil, err
	}
	b.Member = make([]int64, 0, length)
	b.Position = make([]int16, 0, length)
	var userId int64
	var position int16
	rows, err := loadPosition.Query(id)
	for rows.Next() {
		err = rows.Scan(&position, &userId)
		b.Member = append(b.Member, userId)
		b.Position = append(b.Position, position)
	}
	err = rows.Close()
	if err != nil {
		log.Println(err)
	}
	return b, nil
}

type WorkshopMember struct {
	UserId int64
	Position int16
}
func ListWorkshopMember(id int64) ([]WorkshopMember, error) {
	r, err := listMember.Query(id)
	if err != nil {
		return nil, err
	}
	goal := make([]WorkshopMember, 0, 5)
	for r.Next() {
		m := WorkshopMember{}
		err := r.Scan(&(m.UserId), &(m.Position))
		if err != nil {
			return nil, err
		}
		goal = append(goal, m)
	}
	return goal, nil
}

func ChangeMemberPosition(workshop int32, do int64, who int64, to int16) uint8 {
	_, s := checkPower(do, workshop, Owner)
	if s != 0 {
		return s
	}
	if to == Owner {
		// 转移工作组组长
		err := InTransaction(func(tx *sql.Tx) (i []string, e error) {
			cp := tx.Stmt(changePosition)
			// change the old owner to manager
			r, err := cp.Exec(Manager, do)
			if err != nil {
				return nil, err
			}
			if l, err := r.RowsAffected(); err != nil || l != 1 {
				return nil, errors.New("")
			}
			// set the other user to the owner
			r, err = cp.Exec(Owner, who)
			if err != nil {
				return nil, err
			}
			if l, err := r.RowsAffected(); err != nil || l != 1 {
				return nil, errors.New("")
			}
			return nil, nil
		})
		if err != nil {
			return E未知错误 // 未知错误
		}
	} else {
		// change
		r, err := changePosition.Exec(Manager, who)
		if err != nil {
			return E未知错误
		}
		if l, err := r.RowsAffected(); err != nil || l != 1 {
			return E未知错误
		}
	}

	return Success
}

func checkPower(who int64, workshop int32, needPosition int16) (int16, uint8) {
	r := selectPosition.QueryRow(who, workshop)
	var doerPosition int16
	err := r.Scan(&doerPosition)
	if err != nil {
		return 0, E发起方不存在 // 发起成员不存在
	}
	if doerPosition < needPosition {
		return 0, E越权
	}
	return doerPosition, Success
}
