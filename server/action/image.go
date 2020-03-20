package action

import (
	"EasyDesignApplication/server/action/httpTools"
	"EasyDesignApplication/server/base/MultiMedia"
	"EasyDesignApplication/server/middle"
	"bytes"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
)

var firstPageImage int64 = 1

func firstPage(w http.ResponseWriter, r *http.Request) {
	_, err := fmt.Fprintf(w, "%d", firstPageImage)
	if err != nil {
		w.WriteHeader(400)
	}
}

func getImage(w http.ResponseWriter, r *http.Request) {
	log.Println("call image")
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
	i, err := middle.LoadImageDataNow(id)
	if err != nil {
		w.WriteHeader(404)
		return
	}
	_, err = w.Write(i.Data)
	if err != nil {
		w.WriteHeader(500)
		return
	}
	log.Println("finish write image")
}

func SetFirstPageImage(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	case http.MethodPost:
		reader, err := r.MultipartReader()
		if err != nil {
			w.WriteHeader(400)
			return
		}
		part, err := reader.NextPart()
		if err != nil {
			w.WriteHeader(400)
			return
		}
		buffer := bytes.NewBuffer(nil)
		_, err = buffer.ReadFrom(part)
		if err != nil {
			w.WriteHeader(400)
			return
		}
		i := MultiMedia.NewImage(buffer.Bytes())
		firstPageImage = i.Id
		err = MultiMedia.SaveImageData(i)
		if err != nil {
			w.WriteHeader(500)
		}
	case http.MethodGet:
		f, err := os.Open("testPostFile.html")
		if err != nil {
			w.WriteHeader(404)
			return
		}
		_, err = io.Copy(w, f)
		if err != nil {
			w.WriteHeader(500)
			return
		}
		err = f.Close()
		if err != nil {
			log.Println(err)
		}
	}
}
