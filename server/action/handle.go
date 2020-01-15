package action

import (
	"EasyDesignApplication/server/action/session"
	"net/http"
)

func Init(){
	http.HandleFunc("/loginId", session.LoginWithId)
	http.HandleFunc("/loginEmail", session.LoginByEmail)
}
