package httpTools

import (
	"EasyDesignApplication/server/base/ComplexString"
	"EasyDesignApplication/server/base/MultiMedia"
	"encoding/json"
	"fmt"
	"log"
	"net/url"
)

const complexStringImageFieldNameModel = "image%d"

func BuildComplexStringFromForm(form url.Values) (goal *ComplexString.ComplexString, has bool) {
	complexBase, has := GetDataFromForm(form, "complexBase")
	if !has {
		return
	}
	log.Println(string(complexBase))
	goal = &ComplexString.ComplexString{}
	err := json.Unmarshal([]byte(complexBase), goal)
	if err != nil {
		log.Println(err)
		return nil, false
	}
	for i, res := range goal.ResourcesId.Res {
		if res >= ComplexString.TypeImage {
			res -= ComplexString.TypeImage
			data, has := GetDataFromForm(form, fmt.Sprintf(complexStringImageFieldNameModel, res))
			if !has {
				continue
			}
			img := MultiMedia.NewImage([]byte(data))
			goal.ResourcesId.Res[i] = img.Id
			err := MultiMedia.SaveImageData(img)
			if err != nil {
				log.Println(err)
			}
		}
	}
	return
}
