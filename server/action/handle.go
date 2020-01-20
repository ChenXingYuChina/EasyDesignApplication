package action

import (
	"EasyDesignApplication/server/action/session"
	"net/http"
)

func Init(){
	http.HandleFunc("/loginId", session.LoginWithId)
	http.HandleFunc("/loginEmail", session.LoginByEmail)
	http.HandleFunc("logout", session.Logout)
	http.HandleFunc("/image", getImage)
	http.HandleFunc("/firstPageImage", firstPage)
}
