package httpTools

import (
	"bytes"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
)

func LoadMultiPartData(r *http.Request) (map[string][]string, int) {
	log.Println("call parse multi part")
	reader, err := r.MultipartReader()
	if err != nil {
		log.Println(err)
		return nil, 400
	}
	goal := map[string][]string{}
	for {
		part, err := reader.NextPart()
		if err != nil {
			log.Println(err)
			break
		}
		buffer := bytes.NewBuffer(nil)
		_, err = buffer.ReadFrom(part)
		if err != nil {
			log.Println(err)
			return nil, 400
		}
		goal[part.FormName()] = []string{string(buffer.Bytes())}
	}
	log.Println("parse multi part success")
	return goal, 200
}

func JustRead(w http.ResponseWriter, r *http.Request) {
	s, err := ioutil.ReadAll(r.Body)
	if err != nil {
		log.Println(err)
		return
	}
	fmt.Println(string(s))
}

type useMultiPart func(w http.ResponseWriter, r *http.Request)


func TransformMultiPartToNormalForm(action http.HandlerFunc) func(w http.ResponseWriter, r *http.Request) {
	return useMultiPart(action).transformMultiPartToForm
}

func (f useMultiPart) transformMultiPartToForm(w http.ResponseWriter, r *http.Request)  {
	err := r.ParseForm()
	if err != nil {
		w.WriteHeader(400)
		return
	}
	forms, s := LoadMultiPartData(r)
	if s != 200 {
		w.WriteHeader(s)
		return
	}
	for k, v := range forms {
		r.Form[k] = v
	}
	f(w, r)
}
