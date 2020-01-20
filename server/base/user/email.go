package user

import "regexp"

type Email string
var emailReg = regexp.MustCompile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")

func (e *Email) CheckRight() bool {
	return emailReg.MatchString(string(*e))
}
