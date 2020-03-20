package action

import (
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/action/session"
	"EasyDesignApplication/server/base"
	"EasyDesignApplication/server/base/MultiMedia"
	"EasyDesignApplication/server/base/Passage"
	"EasyDesignApplication/server/middle"
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
)

type FullPassage struct {
	*middle.FullPassage
	Full bool `json:"full"`
}

func passage(w http.ResponseWriter, r *http.Request) {
	log.Println("call passage")
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	log.Println(r.Form)
	id, has := httpTools.GetInt64FromForm(r.Form, "id")
	if !has {
		w.WriteHeader(400)
		return
	}
	t, has := httpTools.GetInt64FromForm(r.Form, "type")
	if !has {
		w.WriteHeader(400)
		return
	}
	p, f := middle.LoadFullPassageFromCache(int16(t), id)
	fullPassage := FullPassage{FullPassage: p}
	log.Println(p)
	if p != nil {
		fullPassage.Full = true
	} else {
		fullPassage.Passage, err = middle.GetPassageFromFunction(f)
		if err != nil {
			w.WriteHeader(500)
			return
		}
	}
	goal, err := json.Marshal(fullPassage)
	if err != nil {
		w.WriteHeader(500)
		return
	}
	fmt.Println(string(goal))
	_, _ = w.Write(goal)
}

func likePassage(s *session.Session, w http.ResponseWriter, r *http.Request) {
	log.Println("call like passage")
	pid, has := httpTools.GetInt64FromForm(r.Form, "pid")
	if !has {
		w.WriteHeader(400)
		return
	}
	err := base.InTransaction(func(tx *sql.Tx) ([]string, error) {
		return nil, Passage.LikePassage(tx, pid)
	})
	if err != nil {
		w.WriteHeader(500)
		return
	}
	w.WriteHeader(200)
	log.Println("like passage success")
}

func publicPassage(s *session.Session, w http.ResponseWriter, r *http.Request) {
	cs, ok := httpTools.BuildComplexStringFromForm(r.Form)
	if !ok {
		w.WriteHeader(400)
		return
	}
	t, ok := httpTools.GetInt64FromForm(r.Form, "type")
	if !ok {
		w.WriteHeader(400)
		return
	}
	title, ok := httpTools.GetDataFromForm(r.Form, "title")
	if !ok {
		w.WriteHeader(400)
		return
	}
	imgData, ok := httpTools.GetDataFromForm(r.Form, "listImage")
	if !ok {
		w.WriteHeader(400)
		return
	}
	img := MultiMedia.NewImage([]byte(imgData))

	if _, err := Passage.NewPassage(cs, int16(t), s.User.ID, title, 0, nil, img.Id); err == nil {
		err = MultiMedia.SaveImageData(img)
		if err != nil {
			w.WriteHeader(500)
			return
		}
	} else {
		w.WriteHeader(500)
	}
}
