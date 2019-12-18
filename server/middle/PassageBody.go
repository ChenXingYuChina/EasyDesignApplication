package middle

import (
	"EasyDesignApplication/server/base"
	"database/sql"
	"fmt"
	"log"
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

func NewPassage(cs *base.ComplexString, t int16, owner int64, title string, workshop int64, media *base.MultiMediaMetadata) (*Passage, error) {
	tx, pid, err := base.MakePassage(owner, title)
	if err != nil {
		return nil, err
	}
	if workshop != 0 {
		_, err = tx.Stmt(newPassageStepWorkshop).Exec(pid, workshop)
		if err != nil {
			err = tx.Rollback()
			log.Println(err)
			return nil, err
		}
	}
	_, err = tx.Stmt(newPassageStepType).Exec(pid, t)
	if err != nil {
		err = tx.Rollback()
		log.Println(err)
		return nil, err
	}
	_, err = tx.Stmt(newPassageStepWorkshop).Exec()
	if err != nil {
		err = tx.Rollback()
		log.Println(err)
		return nil, err
	}
	err = base.SaveMultiMediaMetadata(tx, pid, media)
	if err != nil {
		return nil, err
	}
	err = cs.SaveComplexStringToFile(fmt.Sprintf(passageBodyRealFilename, pid))
	if err != nil {
		_ = tx.Rollback()
		return nil, err
	}
	err = tx.Commit()
	if err != nil {
		return nil, err
	}
	return &Passage{Id:pid, Body:cs, MediaLinked:media}, nil
}


