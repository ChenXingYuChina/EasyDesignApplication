package action

import (
	"EasyDesignApplication/server/middle"
	"fmt"
	"go/src/strconv"
	"net/http"
)

func firstPage(w http.ResponseWriter, r *http.Request) {
	_, err := fmt.Fprintf(w, "%d", 0)
	if err != nil {
		w.WriteHeader(400)
	}
}

func getImage(w http.ResponseWriter, r *http.Request) {
	ids, has := r.URL.Query()["id"]
	if !has {
		w.WriteHeader(400)
		return
	}
	if len(ids) == 0 {
		w.WriteHeader(400)
		return
	}
	id, err := strconv.ParseInt(ids[0], 10, 64)
	if err != nil {
		w.WriteHeader(400)
		return
	}
	image, err := middle.LoadImageDataNow(id)
	if err != nil {
		w.WriteHeader(404)
		return
	}
	_, err = w.Write(image.Data)
	if err != nil {
		w.WriteHeader(500)
		return
	}
}
