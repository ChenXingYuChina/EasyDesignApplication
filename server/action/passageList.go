package action

import (
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/base/Passage"
	"EasyDesignApplication/server/base/user"
	"EasyDesignApplication/server/middle"
	"encoding/json"
	"log"
	"net/http"
	"strconv"
)

type fullPassageList struct {
	PassageList Passage.PassageList `json:"passage_list"`
	UserMiniList []*user.UserMini `json:"user_mini_list"`
}

var nilPassage = func() []byte {
	list := fullPassageList{PassageList: Passage.PassageList{}}
	goal, err := json.Marshal(list)
	if err != nil {
		panic(err)
	}
	return goal
}()

func passageList(w http.ResponseWriter, r *http.Request) {
	log.Println("call passage list")
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	log.Println(r.Form)
	lengthString := r.Form.Get("len")
	if len(lengthString) == 0 {
		w.WriteHeader(400)
		return
	}
	length, err := strconv.ParseInt(lengthString, 10, 8)
	if err != nil {
		w.WriteHeader(400)
		return
	}
	var begin int64
	refresh, isRefresh := httpTools.GetInt64FromForm(r.Form, "refresh")
	if isRefresh {
		begin = 0
	} else {
		beginString, has := r.Form["begin"]
		if !has || len(beginString) == 0 {
			w.WriteHeader(400)
			return
		}
		begin, err = strconv.ParseInt(beginString[0], 10, 32)
		if err != nil {
			w.WriteHeader(400)
			return
		}
	}

	var passageList Passage.PassageList
	var userMinis []*user.UserMini
	if keyword := r.Form.Get("keyword"); len(keyword) != 0 {
		// search by keyword
		passageList, err = Passage.SearchPassageByTitle(keyword, uint8(length), begin)
		if err != nil {
			w.WriteHeader(500)
			return
		}
		
	} else if workshop, has := httpTools.GetInt64FromForm(r.Form, "workshop"); has {
		// load passage by workshop
		passageList, err = Passage.LoadPassageListByWorkshopLast(workshop, uint8(length), begin)
	} else {
		passageType := r.Form.Get("type")
		if len(lengthString) == 0 {
			w.WriteHeader(400)
			return
		}
		t, err := strconv.ParseInt(passageType, 10, 16)
		if err != nil {
			return
		}

		if id, has := httpTools.GetInt64FromForm(r.Form, "id"); has {
			// load passages by user id and type
			beginString := r.Form.Get("begin")
			if len(beginString) == 0 {
				w.WriteHeader(400)
				return
			}
			beginInt, err := strconv.ParseInt(beginString, 10, 32)
			if err != nil {
				w.WriteHeader(400)
				return
			}

			passageList, err = Passage.LoadPassageListByUserAndTypeLast(id, int16(t), uint8(length), beginInt)
			if err != nil {
				w.WriteHeader(500)
				return
			}
		} else {
			// load passages by type
			hot, err := strconv.ParseBool(r.Form.Get("hot"))
			if err != nil {
				w.WriteHeader(400)
				return
			}
			if isRefresh {
				if !middle.CheckUpdate(refresh, int16(t), hot) {
					_, _ = w.Write(nilPassage)
					return
				}
			}
			if hot {
				passageList, userMinis, err = middle.LoadHotPassageList(int16(t), int64(begin), uint8(length))
			} else {
				passageList, userMinis, err = middle.LoadLastPassageListByType(int16(t), int64(begin), uint8(length))
			}
			if err != nil {
				w.WriteHeader(500)
				return
			}
		}
	}
	goal, err := json.Marshal(fullPassageList{PassageList: passageList, UserMiniList:userMinis})
	if err != nil {
		w.WriteHeader(500)
		return
	}
	log.Println(string(goal))
	_, _ = w.Write(goal)
}
