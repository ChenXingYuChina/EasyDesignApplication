package session

import (
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/base/user"
	"log"
	"net/http"
)

type inSessionAction func (session *Session, w http.ResponseWriter, r *http.Request)

// when use this do not need to parse form.
func NeedLogin(mainAction inSessionAction) http.HandlerFunc {
	return mainAction.needLogin
}

func NeedDesigner(mainAction inSessionAction) http.HandlerFunc {
	return mainAction.needDesigner
}

func (sa inSessionAction) needLogin(w http.ResponseWriter, r *http.Request) {
	s := checkLogin(w, r)
	if s != nil {
		sa(s, w, r)
	}
}

func checkLogin(w http.ResponseWriter, r *http.Request) *Session {
	if r.Form == nil {
		err := r.ParseForm()
		if err != nil {
			log.Println(err)
			w.WriteHeader(400)
			return nil
		}
	}
	uid, has := httpTools.GetInt64FromForm(r.Form, "userId")
	if !has {
		log.Println("no userId")
		w.WriteHeader(400)
		return nil
	}
	sessionId, has := httpTools.GetInt64FromForm(r.Form, "sessionId")
	if !has {
		log.Println("no session id")
		w.WriteHeader(400)
		return nil
	}
	s := st.get(uid)
	if s == nil || s.SessionKey != sessionId {
		w.WriteHeader(401)
		return nil
	}
	return s
}

func (sa inSessionAction) needDesigner(w http.ResponseWriter, r *http.Request) {
	s := checkLogin(w, r)
	if s == nil {
		return
	} else if s.User.Identity.Type() != user.DesignerType {
		w.WriteHeader(403)
	}
	sa(s, w, r)
}

