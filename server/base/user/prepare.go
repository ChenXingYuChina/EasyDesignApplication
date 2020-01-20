package user

import (
	"EasyDesignApplication/server/base"
	"fmt"
)

func init() {
	base.RegisterPrepare(func() {
		PrepareUserDir()
		p, err := PrepareUserSQL()
		if err != nil {
			panic(fmt.Sprint("user full sql fail: ", err, p))
		}
		PrepareWorkshopDir()
		p, err = PrepareWorkshopSQL()
		if err != nil {
			panic(fmt.Sprint("workshop sql fail: ", err, p))
		}
		p, err = UserMiniSQLPrepare()
		if err != nil {
			panic(fmt.Sprint("user mini sql fail: ", err, p))
		}
		p, err = UserSQLPrepare()
		if err != nil {
			panic(fmt.Sprint("user base sql fail: ", err, p))
		}
	})
}
