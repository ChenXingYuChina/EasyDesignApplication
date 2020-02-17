package user

import (
	. "EasyDesignApplication/server/base"
	"EasyDesignApplication/server/base/ComplexString"
	"database/sql"
	"fmt"
	"github.com/lib/pq"
	"strings"
)

const (
	userLongDescriptionDir = "/uld"
	userLongDescriptionFileName = "/uld/%x.cs"
)

var (
	userLongDescriptionRealFileName string
	loadDesignerState *sql.Stmt
	authorizedDesigner *sql.Stmt
	unauthorizedDesigner *sql.Stmt
)

func prepareUserSQL() (uint8, error) {
	var err error
	loadDesignerState, err = Database.Prepare("select true from unauthorized where unauthorized.user_id = $1")
	if err != nil {
		return 0, err
	}
	authorizedDesigner, err = Database.Prepare("delete from unauthorized where user_id = $1;")
	if err != nil {
		return 1, err
	}
	unauthorizedDesigner, err = Database.Prepare("insert into unauthorized (user_id) values ($1)")
	return 0, nil
}

func PrepareUserDir() {
	userLongDescriptionRealFileName = DataDir + userLongDescriptionFileName
	CheckAndMakeDir(DataDir + userLongDescriptionDir)
}


func LoadUserDescription(userId int64) (*ComplexString.ComplexString, error) {
	return ComplexString.LoadComplexStringFromFile(fmt.Sprintf(userLongDescriptionRealFileName, userId))
}

func CheckAuthorized(id int64) bool {
	var u bool
	r := loadDesignerState.QueryRow(id)
	return r.Scan(&u) != nil
}

func SaveUsersLongDescription(new *ComplexString.ComplexString, who int64) error {
	err := new.SaveComplexStringToFile(fmt.Sprintf(userLongDescriptionRealFileName, who))
	if err != nil {
		return err
	}
	return nil
}

func Authorized(who int64) bool {
	_, err := authorizedDesigner.Exec(who)
	return err == nil
}

func Unauthorized(who int64) bool {
	_, err := unauthorizedDesigner.Exec(who)
	if err != nil {
		return strings.Compare(string(err.(*pq.Error).Code) ,"23505") == 0
	}
	return true
}
