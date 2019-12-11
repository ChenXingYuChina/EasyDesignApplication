package base

import (
	"database/sql"
	"sync"
)

var (
	loadCommentBase *sql.Stmt
	likeComment *sql.Stmt
	makeCommentStepInsert *sql.Stmt
	makeCommentStepUpdate *sql.Stmt
)

type CommentBase struct {
	Passage int64
	Owner int64
	Like uint32
	Position uint32
	SubCommentNumber uint16
}

var commentBasePool = new(sync.Pool)

func init() {
	commentBasePool.New = func() interface{} {
		return &CommentBase{}
	}
}

func PrepareCommentSQL() (uint8, error) {
	var err error
	loadCommentBase, err = SQLPrepare("select position, like_number, sub_comment, owner from comment where passage_id = $1 sort by position asc offset $2 limit $3")
	if err != nil {
		return 0, err
	}
	likeComment, err = SQLPrepare("update comment set like_number = like_number + 1 where passage_id = $1 and position = $2")
	if err != nil {
		return 1, err
	}
	makeCommentStepInsert, err = SQLPrepare("insert into comment (passage_id, position, owner) select $1, comment_number + 1, $2 from passages where id = $1")
	if err != nil {
		return 2, err
	}
	makeCommentStepUpdate, err = SQLPrepare("update passages set comment_number = comment_number + 1 where id = $1")
	if err != nil {
		return 3, err
	}
	return 0, nil
}

func GetCommentBase() *CommentBase {
	return commentBasePool.Get().(*CommentBase)
}

func RecycleCommentBase(c *CommentBase) {
	commentBasePool.Put(c)
}

func LoadCommentBase(passageId int64, begin uint32, length uint32) ([]*CommentBase, error) {
	r, err := loadCommentBase.Query(passageId, begin, length)
	if err != nil {
		return nil, err
	}
	goal := make([]*CommentBase, 0, length)
	for r.Next() {
		c := GetCommentBase()
		err = r.Scan(&(c.Position), &(c.Like), &(c.SubCommentNumber), &(c.Owner))
		if err != nil {
			for _, v := range goal {
				RecycleCommentBase(v)
			}
			return nil, err
		}
		c.Passage = passageId
		goal = append(goal, c)
	}
	return goal, nil
}

func LikeComment(passageID int64, position uint32) error {
	_, err := likeComment.Exec(passageID, position)
	return err
}

func MakeComment(passageId int64, who int64) error {
	tx, err := Database.Begin()
	if err != nil {
		return err
	}
	r, err := tx.Stmt(makeCommentStepInsert).Exec(passageId, who)
	if err != nil {
		_ = tx.Rollback()
		return err
	}
	var line int64
	if line, err = r.RowsAffected(); err != nil {
		_ = tx.Rollback()
		return err
	} else if line != 1 {
		_ = tx.Rollback()
		return UnknownError
	}
	r, err = tx.Stmt(makeCommentStepUpdate).Exec(passageId)
	if err != nil {
		_ = tx.Rollback()
		return err
	}
	if line, err = r.RowsAffected(); err != nil {
		_ = tx.Rollback()
		return err
	} else if line != 1 {
		_ = tx.Rollback()
		return UnknownError
	}
	return tx.Commit()
}
