package action

import (
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/action/session"
	"EasyDesignApplication/server/base"
	"EasyDesignApplication/server/base/MultiMedia"
	"EasyDesignApplication/server/base/user"
	"database/sql"
	"encoding/json"
	"log"
	"net/http"
)

func changePassword(s *session.Session, w http.ResponseWriter, r *http.Request) {
	oldPassword, has := httpTools.GetPasswordFromForm(r.Form, "old")
	if !has {
		w.WriteHeader(400)
		return
	}
	newPassword, has := httpTools.GetPasswordFromForm(r.Form, "new")
	if !has {
		w.WriteHeader(400)
		return
	}
	if s.User.ChangePassword(oldPassword, newPassword) {
		w.WriteHeader(200)
	} else {
		w.WriteHeader(401)
	}
}

func changeUserName(s *session.Session, w http.ResponseWriter, r *http.Request) {
	name, has := httpTools.GetDataFromForm(r.Form, "name")
	if !has {
		w.WriteHeader(400)
		return
	}
	s.User.UserName = name
	result := s.User.Update()
	if result == 0 {
		w.WriteHeader(200)
	} else if result == 2 {
		log.Println("账户异常操作", r.Form)
		w.WriteHeader(200)
	} else if result == 255 {
		w.WriteHeader(500)
	}
}

func updateIdentity(s *session.Session, w http.ResponseWriter, r *http.Request) {
	identity, has := httpTools.GetDataFromForm(r.Form, "identity")
	if !has {
		w.WriteHeader(400)
		return
	}
	err := json.Unmarshal([]byte(identity), s.User.Identity)
	if err != nil {
		w.WriteHeader(400)
		return
	}
	if !s.User.UpdateIdentity() {
		w.WriteHeader(200)
	} else {
		w.WriteHeader(400)
	}
}

const (
	head = iota
	back
)

func setHeadImage(s *session.Session, w http.ResponseWriter, r * http.Request) {
	log.Println("call set head image")
	setImageToUser(w, r, s.User, head)
}

func setBackImage(s *session.Session, w http.ResponseWriter, r * http.Request) {
	setImageToUser(w, r, s.User, back)
}

func setImageToUser(w http.ResponseWriter, r *http.Request, u *user.UserBase, where int) {
	i := MultiMedia.NewImage([]byte(r.Form["userImage"][0]))
	switch where {
	case head:
		u.HeadImage = i.Id
	case back:
		u.BackImage = i.Id
	}
	err := base.InTransaction(func(tx *sql.Tx) ([]string, error) {
		err := u.UpdateInTransition(tx)
		if err != nil {
			return nil, err
		}
		err = MultiMedia.SaveImageData(i)
		if err != nil {
			return nil, err
		}
		return nil, nil
	})
	if err != nil {
		w.WriteHeader(500)
	} else {
		w.WriteHeader(200)
	}
}
