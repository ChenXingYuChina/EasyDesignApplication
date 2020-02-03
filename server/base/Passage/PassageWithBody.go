package Passage

import (
	"EasyDesignApplication/server/base"
	"EasyDesignApplication/server/base/ComplexString"
	"EasyDesignApplication/server/base/MultiMedia"
	"database/sql"
	"fmt"
)

const (
	passageBodyDir = "/pb"
	passageBodyFilename = "/pb/%x.cs"
)

var (
	passageBodyRealFilename string
	newPassageStepWorkshop *sql.Stmt
)

func preparePassageBodySQL() (uint8, error) {
	var err error
	newPassageStepWorkshop, err = base.Database.Prepare("insert into workshop_passage (passage_id, workshop_id) VALUES ($1, $2)")
	if err != nil {
		return 1, err
	}
	return 0, nil
}

func preparePassageDir() {
	passageBodyRealFilename = base.DataDir + passageBodyFilename
	base.CheckAndMakeDir(base.DataDir +passageBodyDir)
}

type Passage struct {
	Body        *ComplexString.ComplexString `json:"body"`
	Id          int64 `json:"id"`
	MediaLinked *MultiMedia.MultiMediaMetadata `json:"media"`
}


func LoadPassageFromDisk(id int64) (*Passage, error) {
	goal := &Passage{Id:id}
	var err1 error
	var err2 error
	goal.Body, err1 = ComplexString.LoadComplexStringFromFile(fmt.Sprintf(passageBodyRealFilename, id))
	goal.MediaLinked, err2 = MultiMedia.LoadMultiMediaMetadata(id)
	if err1 != nil && err2 != nil {
		return nil, err1
	}
	return goal, nil
}

func NewPassage(cs *ComplexString.ComplexString, t int16, owner int64, title string, workshop int64, media *MultiMedia.MultiMediaMetadata, listImage int64) (*Passage, error) {
	var pid int64
	err := base.InTransaction(func(tx *sql.Tx) ([]string, error) {
		var err error
		pid, err = makePassage(tx, owner, title, listImage, t)
		if err != nil {
			return nil, err
		}
		if workshop != 0 {
			_, err = tx.Stmt(newPassageStepWorkshop).Exec(pid, workshop)
			if err != nil {
				return nil, err
			}
		}
		fileToDelete := make([]string, 0, 2)
 		if media != nil {
			err = MultiMedia.SaveMultiMediaMetadata(tx, &fileToDelete, pid, media)
			if err != nil {
				return fileToDelete, err
			}
		}
		var fileName string
		if cs != nil {
			fileName = fmt.Sprintf(passageBodyRealFilename, pid)
			err = cs.SaveComplexStringToFile(fileName)
			if err != nil {
				return append(fileToDelete, fileName), err
			}
		}
		return nil, err
	})
	if err != nil {
		return nil, err
	}
	return &Passage{Id:pid, Body:cs, MediaLinked:media}, nil
}


