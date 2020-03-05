package action

import (
	"EasyDesignApplication/server/action/session"
	"EasyDesignApplication/server/base/user"
	"encoding/json"
	"fmt"
	"net/http"
	"net/url"
	"strconv"
	"testing"
)

func TestLikeSubComment(t *testing.T) {
	r := &http.Request{
		Method:"POST",
		PostForm:url.Values{"id":{"50"},"pw":{user.MakePasswordInFront("hello world")}},
	}
	mid := testTool(t, "login", r, session.LoginWithId)
	data := &struct {
		SK int64 `json:"session_key"`
	}{}
	err := json.Unmarshal([]byte(mid), &data)
	if err != nil {
		t.Error(err)
		return
	}
	sessionKey := strconv.FormatInt(data.SK, 10)
	fmt.Println(sessionKey)
	r = &http.Request{
		Method:"POST",
		PostForm:url.Values{"pid":{"3"}, "father":{"1"}, "position":{"1"}, "userId":{"50"}, "sessionId":{sessionKey}},
	}
	testTool(t, "likeSubComment", r, session.NeedLogin(likeSubComment))
}

func TestSignUp(t *testing.T) {
	r := &http.Request{
		Method: "POST",
		PostForm: url.Values{
			"name":         {"test"},
			"pw":           {user.MakePasswordInFront("hello world")},
			"email":        {"test@test.com"},
			"identityType": {"0"},
			"quick":        {"false"},
			"identity": {`{"schools": [{"public":true, "diploma":3, "country": "中国", "name": "河北工业大学"}]}`},
		},
	}
	testTool(t, "signUp", r, signUp)
}
