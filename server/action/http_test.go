package action

import (
	"EasyDesignApplication/server/middle"
	"bytes"
	"fmt"
	"image"
	_ "image/gif"
	_ "image/jpeg"
	_ "image/png"
	"io/ioutil"
	"mime"
	"net/http"
	"net/http/httptest"
	"net/url"
	"testing"
)


func TestMultipartRead(t *testing.T) {
	http.ListenAndServe("localhost:80", nil)
}

func TestImageType(t *testing.T) {
	i, err := middle.LoadImageDataNow(18)
	if err != nil {
		t.Error(i)
		return
	}
	_, name, err := image.DecodeConfig(bytes.NewBuffer(i.Data))
	if err != nil {
		t.Error(err)
		return
	}
	t.Log(mime.TypeByExtension("." + name))
}

func TestHotPassageList(t *testing.T) {
	w := &httptest.ResponseRecorder{Body: bytes.NewBuffer(nil)}
	r := &http.Request{
		Method: "POST",
		URL:    &url.URL{Scheme: "http", Host: "localhost", Path: "passageList"},
		PostForm: url.Values{
			"begin": []string{"0"},
			"type":  []string{"0"},
			"len":   []string{"10"},
			"hot":   []string{"true"},
		},
	}
	passageList(w, r)
	response := w.Result()
	fmt.Println(response.StatusCode)
	goal, err := ioutil.ReadAll(response.Body)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(string(goal))
}

func TestLastPassageList(t *testing.T) {
	w := &httptest.ResponseRecorder{Body: bytes.NewBuffer(nil)}
	r := &http.Request{
		Method: "POST",
		URL:    &url.URL{Scheme: "http", Host: "localhost", Path: "passageList"},
		PostForm: url.Values{
			"begin": []string{"0"},
			"type":  []string{"0"},
			"len":   []string{"10"},
			"hot":   []string{"false"},
		},
	}
	passageList(w, r)
	response := w.Result()
	fmt.Println(response.StatusCode)
	goal, err := ioutil.ReadAll(response.Body)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(string(goal))
}

func TestRefreshHotPassage(t *testing.T) {
	w := &httptest.ResponseRecorder{Body: bytes.NewBuffer(nil)}
	r := &http.Request{
		Method: "POST",
		URL:    &url.URL{Scheme: "http", Host: "localhost", Path: "passageList"},
		PostForm: url.Values{
			"refresh": []string{"0"},
			"type":    []string{"0"},
			"len":     []string{"10"},
			"hot":     []string{"true"},
		},
	}
	passageList(w, r)
	response := w.Result()
	fmt.Println(response.StatusCode)
	goal, err := ioutil.ReadAll(response.Body)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(string(goal))
}

func TestSearch(t *testing.T) {
	w := &httptest.ResponseRecorder{Body: bytes.NewBuffer(nil)}
	r := &http.Request{
		Method: "POST",
		URL:    &url.URL{Scheme: "http", Host: "localhost", Path: "passageList"},
		PostForm: url.Values{
			"begin":   []string{"0"},
			"type":    []string{"0"},
			"len":     []string{"10"},
			"keyword": []string{"考试"},
		},
	}
	passageList(w, r)
	response := w.Result()
	fmt.Println(response.StatusCode)
	goal, err := ioutil.ReadAll(response.Body)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(string(goal))
}

func TestUserAndType(t *testing.T) {
	w := &httptest.ResponseRecorder{Body: bytes.NewBuffer(nil)}
	r := &http.Request{
		Method: "POST",
		URL:    &url.URL{Scheme: "http", Host: "localhost", Path: "passageList"},
		PostForm: url.Values{
			"begin":   []string{"0"},
			"type":    []string{"0"},
			"len":     []string{"10"},
			"id": []string{"48"},
		},
	}
	passageList(w, r)
	response := w.Result()
	fmt.Println(response.StatusCode)
	goal, err := ioutil.ReadAll(response.Body)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(string(goal))
}

func TestLoadUserMini(t *testing.T) {
	w := &httptest.ResponseRecorder{Body: bytes.NewBuffer(nil)}
	u, err := url.Parse("userMini?id=48")
	if err != nil {
		t.Error(err)
		return
	}
	r := &http.Request{
		Method: "GET",
		URL:    u,
	}
	userMini(w, r)
	response := w.Result()
	fmt.Println(response.StatusCode)
	goal, err := ioutil.ReadAll(response.Body)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(string(goal))
}

func TestPassage(t *testing.T) {
	w := &httptest.ResponseRecorder{Body: bytes.NewBuffer(nil)}
	u, err := url.Parse("passage")
	if err != nil {
		t.Error(err)
		return
	}
	r := &http.Request{
		Method:   "POST",
		URL:      u,
		PostForm: url.Values{"id": {"3"}, "type": {"0"}},
	}
	passage(w, r)
	response := w.Result()
	fmt.Println(response.StatusCode)
	goal, err := ioutil.ReadAll(response.Body)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(string(goal))
}

func TestLoadUserLD(t *testing.T) {
	w := &httptest.ResponseRecorder{Body: bytes.NewBuffer(nil)}
	u, err := url.Parse("userLD?id=48")
	if err != nil {
		t.Error(err)
		return
	}
	r := &http.Request{
		Method:   "GET",
		URL:      u,
	}
	loadUserDescription(w, r)
	response := w.Result()
	fmt.Println(response.StatusCode)
	goal, err := ioutil.ReadAll(response.Body)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(string(goal))
}

func TestLoadStar(t *testing.T) {
	w := &httptest.ResponseRecorder{Body: bytes.NewBuffer(nil)}
	u, err := url.Parse("starPassage")
	if err != nil {
		t.Error(err)
		return
	}
	r := &http.Request{
		Method:   "GET",
		URL:      u,
		PostForm: url.Values{"id":{"50"}, "len":{"10"}, "begin": {"0"}},
	}
	loadStarPassage(w, r)
	response := w.Result()
	fmt.Println(response.StatusCode)
	goal, err := ioutil.ReadAll(response.Body)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(string(goal))
}

func TestComment(t *testing.T) {
	w := &httptest.ResponseRecorder{Body: bytes.NewBuffer(nil)}
	u, err := url.Parse("comment")
	if err != nil {
		t.Error(err)
		return
	}
	r := &http.Request{
		Method:   "GET",
		URL:      u,
		PostForm: url.Values{"id":{"3"}, "len":{"10"}, "begin": {"0"}, "hot": {"false"}},
	}
	loadComment(w, r)
	response := w.Result()
	fmt.Println(response.StatusCode)
	goal, err := ioutil.ReadAll(response.Body)
	if err != nil {
		t.Error(err)
		return
	}
	fmt.Println(string(goal))
}

func TestSubComment(t *testing.T) {
	r := &http.Request{
		Method:   "GET",
		PostForm: url.Values{"pid":{"3"}, "position":{"1"}},
	}
	testTool(t, "subComment", r, loadSubComment)
}

func testTool(t *testing.T, urlString string, r *http.Request, handlerFunc http.HandlerFunc) string {
	w := &httptest.ResponseRecorder{Body: bytes.NewBuffer(nil)}
	u, err := url.Parse(urlString)
	if err != nil {
		t.Error(err)
		return ""
	}
	r.URL = u
	handlerFunc(w, r)
	response := w.Result()
	fmt.Println(response.StatusCode)
	goal, err := ioutil.ReadAll(response.Body)
	if err != nil {
		t.Error(err)
		return ""
	}
	fmt.Println(string(goal))
	return string(goal)
}
