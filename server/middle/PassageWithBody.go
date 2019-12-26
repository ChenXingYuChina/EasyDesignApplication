package middle

import (
	"EasyDesignApplication/server/base"
	"database/sql"
	"fmt"
)

const (
	passageBodyDir = "/pb"
	passageBodyFilename = "/pb/%x.cs"
)

var (
	passageBodyRealFilename string
	newPassageStepType *sql.Stmt
	newPassageStepWorkshop *sql.Stmt
)

func PreparePassageBodySQL() (uint8, error) {
	var err error
	newPassageStepType, err = base.Database.Prepare("insert into passage_type (passage_id, passage_type) VALUES ($1, $2)")
	if err != nil {
		return 0, err
	}
	newPassageStepWorkshop, err = base.Database.Prepare("insert into workshop_passage (passage_id, workshop_id) VALUES ($1, $2)")
	if err != nil {
		return 1, err
	}
	return 0, nil
}

func PreparePassageDir() {
	passageBodyRealFilename = base.DataDir + passageBodyFilename
	base.CheckAndMakeDir(base.DataDir +passageBodyDir)
}

type Passage struct {
	Body        *base.ComplexString
	Id          int64
	MediaLinked *base.MultiMediaMetadata
}


func loadPassage(id int64) (*Passage, error) {
	goal := &Passage{Id:id}
	var err error
	goal.Body, err = base.LoadComplexStringFromFile(fmt.Sprintf(passageBodyRealFilename, id))
	if err != nil {
		return nil, err
	}
	goal.MediaLinked, err = base.LoadMultiMediaMetadata(id)
	if err != nil {
		return nil, err
	}
	return goal, nil
}

func NewPassage(cs *base.ComplexString, t int16, owner int64, title string, workshop int64, media *base.MultiMediaMetadata, listImage int64) (*Passage, error) {
	var pid int64
	err := base.InTransaction(func(tx *sql.Tx) ([]string, error) {
		var err error
		pid, err = base.MakePassage(tx, owner, title, listImage)
		if err != nil {
			return nil, err
		}
		if workshop != 0 {
			_, err = tx.Stmt(newPassageStepWorkshop).Exec(pid, workshop)
			if err != nil {
				return nil, err
			}
		}
		_, err = tx.Stmt(newPassageStepType).Exec(pid, t)
		if err != nil {
			return nil, err
		}
		_, err = tx.Stmt(newPassageStepWorkshop).Exec()
		if err != nil {
			return nil, err
		}
		fileToDelete := make([]string, 0, 2)
		err = base.SaveMultiMediaMetadata(tx, &fileToDelete, pid, media)
		if err != nil {
			return fileToDelete, err
		}
		fileName := fmt.Sprintf(passageBodyRealFilename, pid)
		err = cs.SaveComplexStringToFile(fileName)
		if err != nil {
			return append(fileToDelete, fileName), err
		}
		return nil, err
	})
	if err != nil {
		return nil, err
	}
	return &Passage{Id:pid, Body:cs, MediaLinked:media}, nil
}


