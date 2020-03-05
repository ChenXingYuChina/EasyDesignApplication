package user

import (
	"EasyDesignApplication/server/base"
	"fmt"
)

func init() {
	base.RegisterPrepare(func() {
		PrepareUserDir()
		p, err := userSQLPrepare()
		if err != nil {
			panic(fmt.Sprint("user base sql fail: ", err, p))
		}
		p, err = prepareUserSQL()
		if err != nil {
			panic(fmt.Sprint("user full sql fail: ", err, p))
		}
		PrepareWorkshopDir()
		p, err = prepareWorkshopSQL()
		if err != nil {
			panic(fmt.Sprint("workshop sql fail: ", err, p))
		}
		p, err = userMiniSQLPrepare()
		if err != nil {
			panic(fmt.Sprint("user mini sql fail: ", err, p))
		}
		p, err = prepareUserAction()
		if err != nil {
			panic(fmt.Sprint("user base sql fail: ", err, p))
		}
	})
}
