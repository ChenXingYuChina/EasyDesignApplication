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
