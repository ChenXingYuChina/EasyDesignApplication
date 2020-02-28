package action

import (
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/action/session"
	"EasyDesignApplication/server/base/user"
	"EasyDesignApplication/server/middle"
	"encoding/json"
	"log"
	"net/http"
)

func follow(s *session.Session, w http.ResponseWriter, r *http.Request) {
	log.Println("call follow")
	err := r.ParseForm()
	if err != nil {
		return
	}
	log.Println(r.Form)
	doee, has := httpTools.GetInt64FromForm(r.Form, "who")
	if !has {
		w.WriteHeader(400)
		return
	}
	if user.Follow(s.User.ID, doee) {
		log.Println("follow success")
		w.WriteHeader(200)
	} else {
		w.WriteHeader(400)
	}
}

type userMiniIfHas struct {
	UserMini *user.UserMini `json:"user_mini"`
	Id       int64          `json:"id"`
}

func loadFollow(w http.ResponseWriter, r *http.Request) {
	log.Println("call load Follow")
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	log.Println(r.Form)
	who, has := httpTools.GetInt64FromForm(r.Form, "who")
	if !has {
		w.WriteHeader(400)
		return
	}
	ids, err := user.LoadFollow(who)
	if err != nil {
		w.WriteHeader(500)
		return
	}
	followList := make([]userMiniIfHas, len(ids))
	for i, id := range ids {
		followList[i].Id = id
		u, err := middle.LoadUserMiniNow(id)
		if err != nil {
			return
		}
		followList[i].UserMini = u
	}
	goal, err := json.Marshal(followList)
	if err != nil {
		w.WriteHeader(500)
		return
	}
	log.Println(string(goal))
	_, err = w.Write(goal)
	if err != nil {
		log.Println(err)
	}
}
