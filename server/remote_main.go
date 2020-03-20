package main

import (
	"EasyDesignApplication/server/action"
	"EasyDesignApplication/server/action/session"
	"EasyDesignApplication/server/base"
	"EasyDesignApplication/server/dataNet"
	"github.com/lib/pq"
	"log"
	"net/http"
	"os"
	"runtime"
	"time"
)

func main()  {
	// prepare all the package
	pq.Open()
	err := base.SqlInit("postgres", "appdb", "12345678", "app_dev", "175.24.76.161")
	if err != nil {
		panic(err)
	}
	if base.DataDir == "./" {
		base.DataDir = "./testData/"
	}
	base.Prepare()
	dataNet.Prepare("0.0.0.0:9090")
	session.InitSessionDir()
	session.InitSessionTable(session.Config{KeepTime: int64(1*time.Hour), SessionKeySeed: 12324})
	action.Init()
	// start listen.
	var x = true
	go func() {
		x =false
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

