package action

import (
	"encoding/json"
	"log"
	"net/http"
	"sync"
	"sync/atomic"
	"unsafe"
)

type exampleConfig struct {
	NameList []string            `json:"names"`
	Config   []passageListConfig `json:"config"`
}

type passageListConfig struct {
	Type   int16  `json:"type"`
	Layout int16  `json:"layout"`
	Card   string `json:"card"`
	Head   string `json:"head"`
}

const (
	titleMainCard = "title_main_card"
	ImageMainCard = "image_main_card"
	linear        = 0
	grid          = 1
	nilHead       = "nil_head"
)

var defaultExampleConfig = &exampleConfig{
	NameList: []string{"1", "2"},
	Config:   []passageListConfig{{Type: 0, Card: titleMainCard, Layout: linear, Head: nilHead}, {Type: 1, Card: titleMainCard, Layout: linear, Head: nilHead}},
}
var defaultExampleConfigJson []byte
var pDefaultExampleConfigJson = &defaultExampleConfigJson

func getExampleList(w http.ResponseWriter, r *http.Request) {
	log.Println("call get example list")
	var err error
	if defaultExampleConfigJson == nil {
		defaultExampleConfigJson, err = json.Marshal(defaultExampleConfig)
		if err != nil {
			log.Fatal(err)
		}
	}
	_, _ = w.Write(defaultExampleConfigJson)

}

var lock sync.Mutex

func setExampleList(w http.ResponseWriter, r *http.Request) {

	switch r.Method {
	case http.MethodPost:
		if config, has := r.PostForm["config"]; has {
			if len(config) > 0 {
				var exampleConfig exampleConfig
				err := json.Unmarshal([]byte(config[0]), &exampleConfig)
				if err == nil {
					configJson, err := json.Marshal(exampleConfig)
					if err == nil {
						lock.Lock()
						atomic.SwapPointer((*unsafe.Pointer)(unsafe.Pointer(&defaultExampleConfig)), unsafe.Pointer(&exampleConfig))
						atomic.SwapPointer((*unsafe.Pointer)(unsafe.Pointer(&pDefaultExampleConfigJson)), unsafe.Pointer(&configJson))
						lock.Unlock()
					}
				}
			}
		}
		w.WriteHeader(400)
	case http.MethodGet:

	}
}
