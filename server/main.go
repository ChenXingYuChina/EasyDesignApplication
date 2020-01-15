package main

import (
	"EasyDesignApplication/server/action"
	"EasyDesignApplication/server/action/session"
	"EasyDesignApplication/server/base"
	"fmt"
	"log"
	"net/http"
	"os"
	"runtime"
	"time"
)

func main()  {
	err := base.SqlInit("postgres", "easyDesign", "easyDesign2019", "easyDesigner", "127.0.0.1")
	if err != nil {
		panic(err)
	}
	p, err := base.UserSQLPrepare()
	if err != nil {
		panic(fmt.Sprintf("user sql prepare fail with %e in position %d", err, p))
	}
	session.InitSessionDir()
	session.InitSessionTable(session.Config{int64(5*time.Hour), 12324})
	action.Init()
	http.HandleFunc("/testhost", func(w http.ResponseWriter, r *http.Request) {
		log.Println("test Success")
		fmt.Fprint(w, "success")
	})
	for {
		var f *os.File
		if runtime.GOOS == "linux" {
			f, err := os.Create(time.Now().String() + ".log")
			if err != nil {
				panic(err)
			}
			log.SetOutput(f)
		}
		err := http.ListenAndServe("0.0.0.0:80", nil)
		log.Println(err)
		if f != nil {
			_ = f.Close()
		}
	}
}
