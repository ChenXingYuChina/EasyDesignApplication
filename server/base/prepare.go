package base


/*
call after SQLInit
 */
func Prepare() {
	for _, f := range prepares {
		f()
	}
}
var prepares = make([]func(), 0, 10)
func RegisterPrepare(p func()) {
	prepares = append(prepares, p)
}


