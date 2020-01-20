package base

import (
	"database/sql"
	"flag"
	"fmt"
	"log"
	"os"
)

var DataDir string

func init() {
	flag.StringVar(&DataDir, "r", "./", "数据文件存储的目录 默认当前文件夹")
}

func DeleteFile(name string) {
	err := os.Remove(name)
	if err != nil {
		log.Println(err)
	}
}

func InTransaction(action func(tx *sql.Tx) ([]string, error)) error {
	tx, err := Database.Begin()
	if err != nil {
		return err
	}
	files, err := action(tx)
	if err != nil {
		err2 := tx.Rollback()
		if err2 != nil {
			log.Println(err2)
		}
		if files != nil {
			for _, name := range files {
				DeleteFile(name)
			}
		}
		return err
	}
	return tx.Commit()
}

func CheckAndMakeDir(name string) {
	_, err := os.Stat(name)
	if err != nil && os.IsNotExist(err) {
		err = os.Mkdir(name, os.ModePerm)
		if err != nil {
			panic(fmt.Sprintf("init fail: %e", err))
		}
	}
}

func OpenWhenExistOrCreate(name string) *os.File {
	goal, err := os.Open(name)
	if err != nil && os.IsNotExist(err) {
		goal, err = os.Create(name)
		if err != nil {
			panic(err)
		}
		return goal
	}
	return goal
}

