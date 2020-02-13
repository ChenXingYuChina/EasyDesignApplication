package action

import (
	"EasyDesignApplication/server/action/controlPlatform"
	"EasyDesignApplication/server/action/session"
	"log"
	"net"
	"net/http"
)

func Init(){
	// product part
	http.HandleFunc("/loginId", session.LoginWithId)
	http.HandleFunc("/loginEmail", session.LoginByEmail)
	http.HandleFunc("logout", session.Logout)
	http.HandleFunc("/image", getImage)
	http.HandleFunc("/firstPageImage", firstPage)
	http.HandleFunc("/passageList", passageList)
	http.HandleFunc("/userMini", userMini)
	http.HandleFunc("/exampleList", getExampleList)

	// manage part
	managerServer.HandleFunc("/firstPageImage", SetFirstPageImage)
	managerServer.HandleFunc("setExampleList", setExampleList)
	go func() {
		for {
			err := managerServer.ListenAndServer()
			log.Println(err)
		}
	}()
}

var managerServer = &controlPlatform.HttpServer{Address: net.TCPAddr{IP:net.ParseIP("127.0.0.1"), Port:55555}}

