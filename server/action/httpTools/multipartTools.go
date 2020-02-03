package httpTools

import (
	"bytes"
	"net/http"
)

func LoadMultiPartData(r *http.Request) (map[string]*bytes.Buffer, int) {
	reader, err := r.MultipartReader()
	if err != nil {
		return nil, 400
	}
	goal := map[string]*bytes.Buffer{}
	for {
		part, err := reader.NextPart()
		if err != nil {
			break
		}
		buffer := bytes.NewBuffer(nil)
		_, err = buffer.ReadFrom(part)
		if err != nil {
			return nil, 400
		}
		goal[part.FormName()] = buffer
	}
	return goal, 200
}
