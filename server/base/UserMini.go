package base

import (
	"database/sql"
	"sync"
)

var (
	getUserMini *sql.Stmt
)

func UserMiniSQLPrepare() (int, error) {
	var err error
	getUserMini, err = SQLPrepare("select name, head_image, identity from users where id = $1")
	if err != nil {
		return 0, err
	}
	return 0, nil
}

type UserMini struct {
	UserId  int64
	UserName string
	HeadImage int64
	Identity Identity
}

func (u *UserMini) loadIdentity(identityType uint8) uint8 {
	var state uint8
	u.Identity, state = loadIdentity(identityType, u.UserId)
	return state
}

func GetUserMini(ids []int64) []*UserMini {
	goal := make([]*UserMini, len(ids))
	for i, id := range ids {
		u := MakeUserMini()
		u.UserId = id
		r, err := getUserMini.Query(id)
		if err != nil {
			RecycleUserMini(u)
			continue
		}
		var identityType uint8
		err = r.Scan(&(u.UserName), &(u.HeadImage), &identityType)
		if err != nil {
			RecycleUserMini(u)
			continue
		}
		var state uint8
		u.Identity, state = loadIdentity(identityType, id)
		if state != 0 {
			RecycleUserMini(u)
			continue
		}
		goal[i] = u
	}
	return goal
}


var userMiniPool = new(sync.Pool)
func MakeUserMini() *UserMini {
	return userMiniPool.Get().(*UserMini)
}

func RecycleUserMini(u *UserMini) {
	userMiniPool.Put(u)
}
func init()  {
	userMiniPool.New = func() interface{} {
		return &UserMini{}
	}
}
