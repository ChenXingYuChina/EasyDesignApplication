package middle

import "EasyDesignApplication/server/base"

func init() {
	base.RegisterPrepare(func() {
		dataManagerInit()
		prepareResidentContentTable()
	})
}
