package Passage

import (
	"EasyDesignApplication/server/base"
	"database/sql"
)

var (
	starPassage *sql.Stmt
)

func preparePassageAction() (uint8, error) {
	var err error
	starPassage, err = base.Database.Prepare("insert into star_passage (passage_id, user_id) VALUES ($1, $2)")
	if err != nil {
		return 0, err
	}
	return 0, nil
}

func StarPassage(pid int64, uid int64) bool {
	_, err := starPassage.Exec(pid, uid)
	return err == nil
}
