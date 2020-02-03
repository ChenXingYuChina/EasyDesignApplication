package user

import (
	. "EasyDesignApplication/server/base"
	"database/sql"
)

var (
	getUserMini *sql.Stmt
)

func userMiniSQLPrepare() (uint8, error) {
	var err error
	getUserMini, err = SQLPrepare("select name, head_image, identity_type from users where id = $1")
	if err != nil {
		return 0, err
	}
	return 0, nil
}

type UserMini struct {
	UserId  int64 `json:"id"`
	UserName string `json:"name"`
	HeadImage int64 `json:"head_image"`
	Identity uint8 `json:"identity"`
}

func GetUserMini(ids []int64) []*UserMini {
	goal := make([]*UserMini, len(ids))
	var name string
	var headImage int64
	var identity uint8
	for i, id := range ids {
		r := getUserMini.QueryRow(id)
		err := r.Scan(&name, &headImage, &identity)
		if err != nil {
			continue
		}
		goal[i] = &UserMini{UserName:name, UserId:id, HeadImage:headImage, Identity:identity}
	}
	return goal
}

func GetOneUserMini(id int64) (*UserMini, bool) {
	var name string
	var headImage int64
	var identity uint8
	r := getUserMini.QueryRow(id)
	err := r.Scan(&name, &headImage, &identity)
	if err != nil {
		return nil, false
	}
	return &UserMini{UserName:name, UserId:id, HeadImage:headImage, Identity:identity}, true
}