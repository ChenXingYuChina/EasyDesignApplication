package middle

import (
	"EasyDesignApplication/server/base"
	"database/sql"
	"fmt"
)

const (
	commentBodyDir = "/cb"
	commentBodyFilename = "/cb/%x_%x.cs"
)

type Comment struct {
	Content *base.ComplexString `json:"content"`
	*base.CommentBase
}
var commentBodyRealFilename string

func PrepareCommentDir() {
	commentBodyRealFilename = base.DataDir + commentBodyFilename
	base.CheckAndMakeDir(base.DataDir + commentBodyDir)
}

func LoadComment(cb *base.CommentBase) (*Comment, error) {
	cs, err := base.LoadComplexStringFromFile(fmt.Sprintf(commentBodyRealFilename, cb.Passage, cb.Position))
	if err != nil {
		return nil, err
	}
	return &Comment{cs, cb}, nil
}

func MakeCommentTo(passageId int64, cs *base.ComplexString, actor int64) bool {
	err := base.InTransaction(func(tx *sql.Tx) (strings []string, e error) {
		cp, err := base.MakeComment(tx, passageId, actor)
		if err != nil {
			return nil, err
		}
		fileName := fmt.Sprintf(commentBodyRealFilename, passageId, cp)
		err = cs.SaveComplexStringToFile(fileName)
		if err != nil {
			return []string{fileName}, err
		}
		return nil, nil
	})
	return err == nil
}
