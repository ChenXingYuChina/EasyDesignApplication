package action

import (
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/action/session"
	"EasyDesignApplication/server/base"
	"EasyDesignApplication/server/base/comment"
	"database/sql"
	"encoding/json"
	"errors"
	"log"
	"net/http"
)

type FullComment struct {
	Comment *comment.Comment `json:"com"`
	SubComment []*comment.SubComment `json:"sub_com"`
}

func loadComment(w http.ResponseWriter, r *http.Request) {
	log.Println("call load comment")
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	log.Println(r.Form)
	hot, has := httpTools.GetBoolFromForm(r.Form, "hot")
	if !has {
		w.WriteHeader(400)
		return
	}
	id, has := httpTools.GetInt64FromForm(r.Form, "id")
	if !has {
		w.WriteHeader(400)
		return
	}
	var bases []*comment.CommentBase
	if hot {
		bases, err = comment.LoadCommentBaseHot(id)
		if err != nil {
			w.WriteHeader(500)
			return
		}
	} else {
		begin, has := httpTools.GetInt64FromForm(r.Form, "begin")
		if !has {
			w.WriteHeader(400)
			return
		}
		length, has := httpTools.GetInt64FromForm(r.Form, "len")
		if !has {
			w.WriteHeader(400)
			return
		}
		bases, err = comment.LoadCommentBase(id, uint32(begin), uint32(length))
	}
	goal := make([]FullComment, len(bases))
	for i, base := range bases {
		goal[i].Comment, err = comment.LoadComment(base)
		if err != nil {
			w.WriteHeader(500)
			return
		}
		goal[i].SubComment, err = comment.LoadHotSubComment(id, base.Position)
		if err != nil {
			w.WriteHeader(500)
			return
		}
	}
	j, err := json.Marshal(goal)
	if err != nil {
		w.WriteHeader(500)
		return
	}
	log.Println(string(j))
	_, err = w.Write(j)
	if err != nil {
		log.Println(err)
	}
}

func loadSubComment(w http.ResponseWriter, r *http.Request) {
	log.Println("call load sub comment")
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	log.Println(r.Form)
	pid, has := httpTools.GetInt64FromForm(r.Form, "pid")
	if !has {
		w.WriteHeader(400)
		return
	}
	position, has := httpTools.GetInt64FromForm(r.Form, "father")
	if !has {
		w.WriteHeader(400)
		return
	}
	subComments, err := comment.LoadSubCommentByPosition(pid, uint32(position), 0, 65535)
	if err != nil {
		w.WriteHeader(500)
		return
	}
	goal, err := json.Marshal(subComments)
	if err != nil {
		w.WriteHeader(500)
		return
	}
	_, err = w.Write(goal)
	if err != nil {
		log.Println(err)
	}
}

func likeSubComment(s *session.Session, w http.ResponseWriter, r *http.Request) {
	log.Println("call like sub comment")
	pid, has := httpTools.GetInt64FromForm(r.Form, "pid")
	if !has {
		w.WriteHeader(400)
		return
	}
	father, has := httpTools.GetInt64FromForm(r.Form, "father")
	if !has {
		w.WriteHeader(400)
		return
	}
	position, has := httpTools.GetInt64FromForm(r.Form, "position")
	if !has {
		w.WriteHeader(400)
		return
	}
	err := base.InTransaction(func(tx *sql.Tx) ([]string, error) {
		if comment.LikeSubComment(tx, pid, uint32(father), uint16(position)) {
			return nil, nil
		}
		return nil, errors.New("")
	})
	if err != nil {
		w.WriteHeader(500)
		return
	}
	w.WriteHeader(200)
	log.Println("like sub comment success")
}

func likeComment(s *session.Session, w http.ResponseWriter, r *http.Request) {
	log.Println("call like comment")
	pid, has := httpTools.GetInt64FromForm(r.Form, "pid")
	if !has {
		w.WriteHeader(400)
		return
	}
	position, has := httpTools.GetInt64FromForm(r.Form, "position")
	if !has {
		w.WriteHeader(400)
		return
	}
	err := base.InTransaction(func(tx *sql.Tx) ([]string, error) {
		return nil, comment.LikeComment(tx, pid, uint32(position))
	})
	if err != nil {
		w.WriteHeader(500)
		return
	}
	w.WriteHeader(200)
	log.Println("like comment success")
}

func subCommentTo(s *session.Session, w http.ResponseWriter, r *http.Request) {
	log.Println("call sub comment to")
	pid, has := httpTools.GetInt64FromForm(r.Form, "pid")
	if !has {
		w.WriteHeader(400)
		return
	}
	father, has := httpTools.GetInt64FromForm(r.Form, "father")
	if !has {
		w.WriteHeader(400)
		return
	}
	contentList, has := r.Form["content"]
	if !has || len(contentList) == 0 {
		w.WriteHeader(400)
		return
	}
	err := base.InTransaction(func(tx *sql.Tx) ([]string, error) {
		return nil, comment.SubCommentTo(tx, pid, uint32(father), contentList[0], s.User.ID)
	})
	if err != nil {
		w.WriteHeader(500)
		return
	}
	w.WriteHeader(200)
	log.Println("sub comment to success")
}
