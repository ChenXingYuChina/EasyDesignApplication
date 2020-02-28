package user

import (
	"EasyDesignApplication/server/base"
	"database/sql"
	"errors"
)

var (
	followUserStepInsert       *sql.Stmt
	followUserStepUpdateFollow *sql.Stmt
	followUserStepUpdateFans   *sql.Stmt
	loadFollow                 *sql.Stmt
)

func prepareUserAction() (uint8, error) {
	var err error
	followUserStepInsert, err = base.Database.Prepare("insert into follow (a, b) VALUES ($1, $2)")
	if err != nil {
		return 0, err
	}
	followUserStepUpdateFollow, err = base.Database.Prepare("update users set follower_number = follower_number + 1 where id = $1")
	if err != nil {
		return 1, err
	}
	followUserStepUpdateFans, err = base.Database.Prepare("update users set fans_number = fans_number + 1 where id = $1")
	if err != nil {
		return 2, err
	}
	loadFollow, err = base.Database.Prepare("select b from follow where a = $1")
	return 0, nil
}

func Follow(doer, doee int64) bool {
	return base.InTransaction(func(tx *sql.Tx) ([]string, error) {
		return nil, follow(tx, doer, doee)
	}) == nil
}

func follow(tx *sql.Tx, doer, doee int64) (err error) {
	if doer == doee {
		return errors.New("")
	}
	stepInsert := tx.Stmt(followUserStepInsert)
	stepFollow := tx.Stmt(followUserStepUpdateFans)
	stepFans := tx.Stmt(followUserStepUpdateFollow)
	_, err = stepInsert.Exec(doer, doee)
	if err != nil {
		return
	}
	_, err = stepFollow.Exec(doer)
	if err != nil {
		return
	}
	_, err = stepFans.Exec(doee)
	return
}

func LoadFollow(doer int64) ([]int64, error) {
	r, err := loadFollow.Query(doer)
	if err != nil {
		return nil, err
	}
	goal := make([]int64, 0, 5)
	var b int64
	for r.Next() {
		err = r.Scan(&b)
		if err != nil {
			return nil, err
		}
		goal = append(goal, b)
	}
	return goal, nil
}
