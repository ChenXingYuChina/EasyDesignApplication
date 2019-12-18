package base

import (
	"database/sql"
	"log"
	"strings"
)

var (
	addPeopleToWorkshopStepInsert      *sql.Stmt
	addPeopleToWorkshopStepUpdate      *sql.Stmt
	removePeopleFromWorkshopStepDelete *sql.Stmt
	removePeopleFromWorkshopStepUpdate *sql.Stmt
	changeWorkshop                     *sql.Stmt
	loadWorkshop                       *sql.Stmt
	loadPosition                       *sql.Stmt
)

func PrepareWorkshopSQL() (uint8, error) {
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
	changeWorkshop, err = SQLPrepare("update workshop set level = $1, head_image = $2 where workshop_id = $3")
	if err != nil {
		return 2, err
	}
	loadWorkshop, err = SQLPrepare("select level, people_number, passage_number, fans_number, head_image name from workshop where id = $1")
	if err != nil {
		return 3, err
	}
	loadPosition, err = SQLPrepare("select position, user_id from workshop_member_link where workshop_id = $1")
	if err != nil {
		return 4, err
	}
	return 0, nil
}

type WorkShopBase struct {
	Member        []int64
	Position      []int16
	Name          string
	Id            int64
	HeadImage     int64
	FansNumber    int32
	PassageNumber int32
	Level         int16
}

func OpenWorkShop(who int64, name string, headImage int64) (*WorkShopBase, uint8) {
	row := Database.QueryRow("select true from unauthorized, users where (id = $1 and identity_type = 1) and user_id = $1", who)
	var s bool
	err := row.Scan(&s)
	if err == nil && s {
		return nil, 1  //  账户未经认证
	}
	if strings.Compare(err.Error(), "sql: no rows in result set") != 0 {
		return nil, 255  //  未知错误
	}
	tx, err := Database.Begin()
	row = tx.QueryRow("insert into workshop (name, head_image) values ($1, $2) returning id", name, headImage)
	var workshopId int64
	err = row.Scan(&workshopId)
	if err != nil {
		err = tx.Rollback()
		if err != nil {
			log.Println(err)
		}
		return nil, 255
	}

	r, err := tx.Exec("insert into workshop_member_link (workshop_id, user_id, position) values ($1, $2, 1)", workshopId, who)
	line, err := r.RowsAffected()
	if err != nil || line != 1 {
		err = tx.Rollback()
		if err != nil {
			log.Println(err)
		}
		return nil, 255
	}

	err = tx.Commit()
	if err != nil {
		return nil, 255  // 未知原因
	}
	return &WorkShopBase{Id:workshopId, Member:[]int64{who}, Position:[]int16{1}, Name:name, HeadImage:headImage, FansNumber:0, PassageNumber:0, Level: 0}, 0
}

func AddPeopleToWorkShop(who int64, workshop int64, position int16) error {
	tx, err := Database.Begin()
	r, err := tx.Stmt(addPeopleToWorkshopStepInsert).Exec(workshop, who, position)
	if err != nil {
		err = tx.Rollback()
		if err != nil {
			log.Println(err)
		}
		return err
	}
	line, err := r.RowsAffected()
	if err != nil || line != 1 {
		err = tx.Rollback()
		if err != nil {
			log.Println(err)
		}
		return err
	}
	r, err = tx.Stmt(addPeopleToWorkshopStepUpdate).Exec(workshop)
	if err != nil {
		err = tx.Rollback()
		if err != nil {
			log.Println(err)
		}
		return err
	}
	line, err = r.RowsAffected()
	if err != nil || line != 1 {
		err = tx.Rollback()
		if err != nil {
			log.Println(err)
		}
		return err
	}
	err = tx.Commit()
	if err != nil {
		return err
	}
	return nil
}

func RemovePeopleFromWorkshop(who int64, workshop int64) error {
	tx, err := Database.Begin()
	r, err := tx.Stmt(removePeopleFromWorkshopStepDelete).Exec(who, workshop)
	if err != nil {
		err = tx.Rollback()
		if err != nil {
			log.Println(err)
		}
		return err
	}
	line, err := r.RowsAffected()
	if err != nil || line != 1 {
		err = tx.Rollback()
		if err != nil {
			log.Println(err)
		}
		return err
	}
	r, err = tx.Stmt(removePeopleFromWorkshopStepUpdate).Exec(workshop)
	if err != nil {
		err = tx.Rollback()
		if err != nil {
			log.Println(err)
		}
		return err
	}
	line, err = r.RowsAffected()
	if err != nil || line != 1 {
		err = tx.Rollback()
		if err != nil {
			log.Println(err)
		}
		return err
	}
	err = tx.Commit()
	if err != nil {
		return err
	}
	return nil
}

func ChangeWorkshop(workshop int64, level int16, headImage int64) error {
	r, err := changeWorkshop.Exec(level, headImage, workshop)
	if err != nil {
		return err
	}
	line, err := r.RowsAffected()
	if err != nil || line != 1 {
		return err
	}
	return nil
}

func LoadWorkshopBase(id int64) (*WorkShopBase, error) {
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
