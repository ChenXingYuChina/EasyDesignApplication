package action

import (
	"EasyDesignApplication/server/action/controlPlatform"
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/action/session"
	"log"
	"net"
	"net/http"
)

func Init() {
	// product part
	http.HandleFunc("/loginId", session.LoginWithId)
	http.HandleFunc("/loginEmail", session.LoginByEmail)
	http.HandleFunc("/logout", session.Logout)
	http.HandleFunc("/image", getImage)
	http.HandleFunc("/firstPageImage", firstPage)
	http.HandleFunc("/passageList", passageList)
	http.HandleFunc("/userMini", userMini)
	http.HandleFunc("/exampleList", getExampleList)
	http.HandleFunc("/userLD", loadUserDescription)
	http.HandleFunc("/user", loadUser)
	http.HandleFunc("/starPassage", loadStarPassage)
	http.HandleFunc("/passage", passage)
	http.HandleFunc("/signUp", signUp)
	http.HandleFunc("/comment", loadComment)
	http.HandleFunc("/subComment", loadSubComment)

	http.HandleFunc("/likeSubComment", session.NeedLogin(likeSubComment))
	http.HandleFunc("/likeComment", session.NeedLogin(likeComment))
	http.HandleFunc("/likePassage", session.NeedLogin(likePassage))
	http.HandleFunc("/starThePassage", session.NeedLogin(starPassage))
	http.HandleFunc("/follow", session.NeedLogin(follow))
	http.HandleFunc("/loadFollow", loadFollow)
	http.HandleFunc("/subCommentTo", session.NeedLogin(subCommentTo))
	http.HandleFunc("/changePassword", session.NeedLogin(changePassword))
	http.HandleFunc("/updateIdentity", session.NeedLogin(updateIdentity))
	http.HandleFunc("/changeUserName", session.NeedLogin(changeUserName))
	http.HandleFunc("/setHeadImage", httpTools.TransformMultiPartToNormalForm(session.NeedLogin(setHeadImage)))
	//http.HandleFunc("/setHeadImage", httpTools.JustRead)
	http.HandleFunc("/setBackImage", httpTools.TransformMultiPartToNormalForm(session.NeedLogin(setBackImage)))
	http.HandleFunc("/changeLongDescription", httpTools.TransformMultiPartToNormalForm(session.NeedLogin(changeLongDescription)))
	http.HandleFunc("/commentToPassage", httpTools.TransformMultiPartToNormalForm(session.NeedLogin(commentTo)))
	http.HandleFunc("/publicPassage", httpTools.TransformMultiPartToNormalForm(session.NeedLogin(publicPassage)))

	// manage part
	managerServer.HandleFunc("/firstPageImage", SetFirstPageImage)
	managerServer.HandleFunc("/setExampleList", setExampleList)
	go func() {
		for {
			err := managerServer.ListenAndServer()
			log.Println(err)
		}
	}()
}

var managerServer = &controlPlatform.HttpServer{Address: net.TCPAddr{IP: net.ParseIP("127.0.0.1"), Port: 55555}}
