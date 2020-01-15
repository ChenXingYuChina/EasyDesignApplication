package middle

import (
	"EasyDesignApplication/server/base"
	"database/sql"
	"fmt"
	"log"
)

const (
	userLongDescriptionDir = "/uld"
	userLongDescriptionFileName = "/uld/%x.cs"
)

var (
	userLongDescriptionRealFileName string
	loadDesignerWorkshopPosition *sql.Stmt
)

func PrepareUserSQL() (uint8, error) {
	var err error
	loadDesignerWorkshopPosition, err = base.SQLPrepare("select position, workshop_id from workshop_member_link where user_id = $1")
	if err != nil {
		return 0, err
	}
	return 0, nil
}

func PrepareUserDir() {
	userLongDescriptionRealFileName = base.DataDir + userLongDescriptionFileName
	base.CheckAndMakeDir(base.DataDir + userLongDescriptionDir)
}

type NotDesigner struct {
	*base.UserBase
	LongDescription *base.ComplexString
}

func loadNotDesigner(id int64) (*NotDesigner, error) {
	userBase, state := base.LoadUser(id)
	if state == 3 {
		return nil, NoAccountError
	}
	c, err := base.LoadComplexStringFromFile(fmt.Sprintf(userLongDescriptionRealFileName, id))
	if err != nil {
		return nil, err
	}
	return &NotDesigner{UserBase:userBase, LongDescription:c}, nil
}


type Designer struct {
	*base.UserBase
	LongDescription *base.ComplexString
	WorkShop uint32
	Position uint16
}

func loadDesigner(id int64) (*Designer, error) {
	r, err := loadDesignerWorkshopPosition.Query(id)
	if err != nil {
		return &Designer{}, err
	}
	goal := &Designer{}
	if r.Next() {
		err = r.Scan(&(goal.Position), &(goal.WorkShop))
		if err != nil {
			err = r.Close()
			if err != nil {
				log.Println(err)
			}
			return nil, err
		}
	}
	var state uint8
	goal.UserBase, state = base.LoadUser(id)
	if state == 3 {
		return nil, NoAccountError
	}
	goal.LongDescription, err = base.LoadComplexStringFromFile(fmt.Sprintf(userLongDescriptionRealFileName, id))
	if err != nil {
		return nil, err
	}
	return goal, nil
}


func ChangeUsersLongDescription(new *base.ComplexString, who int64) error {
	err := new.SaveComplexStringToFile(fmt.Sprintf(userLongDescriptionRealFileName, who))
	if err != nil {
		return err
	}
	return nil
}
