package action

import (
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/middle"
	"encoding/json"
	"net/http"
)

type FullPassage struct {
	*middle.FullPassage
	Full bool `json:"full"`
}

func passage(w http.ResponseWriter, r *http.Request) {
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
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
	fullPassage := FullPassage{FullPassage:p}
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
	_, _ = w.Write(goal)
}
