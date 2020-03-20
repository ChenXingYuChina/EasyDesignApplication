package comment

import (
	"EasyDesignApplication/server/base"
	"EasyDesignApplication/server/base/ComplexString"
	"database/sql"
	"fmt"
	"log"
)

const (
	commentBodyDir = "/cb"
	commentBodyFilename = "/cb/%x_%x.cs"
)

type Comment struct {
	Content *ComplexString.ComplexString `json:"content"`
	*CommentBase
}
var commentBodyRealFilename string

func prepareCommentDir() {
	commentBodyRealFilename = base.DataDir + commentBodyFilename
	base.CheckAndMakeDir(base.DataDir + commentBodyDir)
}

func LoadComment(cb *CommentBase) (*Comment, error) {
	cs, err := ComplexString.LoadComplexStringFromFile(fmt.Sprintf(commentBodyRealFilename, cb.Passage, cb.Position))
	if err != nil {
		return nil, err
	}
	return &Comment{cs, cb}, nil
}

func MakeCommentTo(passageId int64, cs *ComplexString.ComplexString, actor int64) bool {
	err := base.InTransaction(func(tx *sql.Tx) (strings []string, e error) {
		cp, err := makeComment(tx, passageId, actor)
		if err != nil {
			log.Println(err)
			return nil, err
		}
		fileName := fmt.Sprintf(commentBodyRealFilename, passageId, cp)
		err = cs.SaveComplexStringToFile(fileName)
		if err != nil {
			log.Println(err)
			return []string{fileName}, err
		}
		return nil, nil
	})
	return err == nil
}
