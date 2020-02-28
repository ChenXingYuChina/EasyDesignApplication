package action

import (
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/action/session"
	"EasyDesignApplication/server/base/Passage"
	"encoding/json"
	"log"
	"net/http"
)

func loadStarPassage(w http.ResponseWriter, r *http.Request) {
	log.Println("call load star passage")
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	uId, has := httpTools.GetInt64FromForm(r.Form, "id")
	if !has {
		w.WriteHeader(400)
		return
	}
	length, has := httpTools.GetInt64FromForm(r.Form, "len")
	if !has {
		w.WriteHeader(400)
		return
	}
	begin, has := httpTools.GetInt64FromForm(r.Form, "begin")
	if !has {
		w.WriteHeader(400)
		return
	}

	list, err := Passage.LoadStarPassage(uId, uint8(length), begin)
	if err != nil {
		w.WriteHeader(500)
		return
	}

	if len(list) == 0 {
		_, err = w.Write(nilPassage)
		if err != nil {
			log.Println(err)
		}
		return
	}
	passageList := fullPassageList{PassageList: list}
	goal, err := json.Marshal(passageList)
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

func starPassage(s *session.Session, w http.ResponseWriter, r *http.Request) {
	log.Println("call star passage")
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	pid, has := httpTools.GetInt64FromForm(r.Form, "pid")
	if !has {
		w.WriteHeader(400)
	}
	if Passage.StarPassage(pid, s.User.ID) {
		w.WriteHeader(200)
	} else {
		w.WriteHeader(500)
	}
}
