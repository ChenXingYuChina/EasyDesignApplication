package main

import (
	"EasyDesignApplication/server/action"
	"EasyDesignApplication/server/action/session"
	"EasyDesignApplication/server/base"
	"EasyDesignApplication/server/dataNet"
	"EasyDesignApplication/server/middle"
	"log"
	"net/http"
	"os"
	"runtime"
	"time"
)

func main()  {
	// prepare all the package.
	err := base.SqlInit("postgres", "easyDesign", "easyDesign2019", "easyDesigner", "127.0.0.1")
	if err != nil {
		panic(err)
	}
	base.Prepare()
	middle.Prepare()
	dataNet.Prepare("0.0.0.0:9090")
	session.InitSessionDir()
	session.InitSessionTable(session.Config{int64(5*time.Hour), 12324})
	action.Init()

	// start listen.
	var x = false
	go func() {
		<-time.Tick(5 * time.Second)
		x = true
	}()
	for x {
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
