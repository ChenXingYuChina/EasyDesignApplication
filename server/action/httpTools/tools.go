package httpTools

import (
	. "EasyDesignApplication/server/base/user"
	"net/url"
	"strconv"
)

func GetDataFromForm(form url.Values, name string) (string, bool) {
	if vs, has := form[name]; has {
		if len(vs) > 0 {
			return vs[0], true
		}
	}
	return "", false
}

func GetInt64FromForm(form url.Values, name string) (int64, bool) {
	nameString, has := GetDataFromForm(form, name)
	if !has {
		return 0, false
	}
	goal, err := strconv.ParseInt(nameString, 10, 64)
	if err != nil {
		return 0, false
	}
	return goal, true
}

func GetEmailFromForm(form url.Values, name string) (Email, bool) {
	e, has := GetDataFromForm(form, name)
	if !has {
		return "", false
	}
	email := Email(string(e))
	if !email.CheckRight() {
		return "", false
	}
	return email, true
}

func GetPasswordFromForm(form url.Values, name string) (Password, bool) {
	p, has := GetDataFromForm(form, name)
	if !has {
		return "", false
	}
	pw := Password(p)
	if !pw.CheckRight() {
		return "", false
	}
	return pw, true
}
