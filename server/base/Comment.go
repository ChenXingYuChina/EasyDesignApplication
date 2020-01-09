package base

import (
	"database/sql"
)

var (
	loadCommentBase *sql.Stmt
	loadCommentBaseHot *sql.Stmt
	likeComment *sql.Stmt
	makeCommentStepInsert *sql.Stmt
	makeCommentStepUpdate *sql.Stmt
)

type CommentBase struct {
	Passage int64 `json:"passage"`
	Owner int64 `json:"owner"`
	Like uint32 `json:"like"`
	Position uint32 `json:"position"`
	SubCommentNumber uint16 `json:"sub_com_number"`
}

func PrepareCommentSQL() (uint8, error) {
	var err error
	loadCommentBase, err = SQLPrepare("select position, like_number, sub_comment, owner from comment where passage_id = $1 order by position asc limit &3 offset $2")
	if err != nil {
		return 0, err
	}
	loadCommentBaseHot, err = Database.Prepare("select position, like_number, sub_comment, owner from comment where passage_id = $1 order by like_number + sub_comment desc limit 15")
	if err != nil {
		return 4, err
	}
	likeComment, err = SQLPrepare("update comment set like_number = like_number + 1 where passage_id = $1 and position = $2")
	if err != nil {
		return 1, err
	}
	makeCommentStepInsert, err = SQLPrepare("insert into comment (passage_id, position, owner) select $1, comment_number + 1, $2 from passages where id = $1 returning comment_number + 1")
	if err != nil {
		return 2, err
	}
	makeCommentStepUpdate, err = SQLPrepare("update passages set comment_number = comment_number + 1 where id = $1")
	if err != nil {
		return 3, err
	}
	return 0, nil
}

func LoadCommentBase(passageId int64, begin uint32, length uint32) ([]*CommentBase, error) {
	r, err := loadCommentBase.Query(passageId, begin, length)
	if err != nil {
		return nil, err
	}
	goal := make([]*CommentBase, 0, length)
	for r.Next() {
		c := &CommentBase{}
		err = r.Scan(&(c.Position), &(c.Like), &(c.SubCommentNumber), &(c.Owner))
		if err != nil {
			return nil, err
		}
		c.Passage = passageId
		goal = append(goal, c)
	}
	return goal, nil
}

func LoadCommentBaseHot(passageId int64) ([]*CommentBase, error) {
	r, err := loadCommentBaseHot.Query(passageId)
	if err != nil {
		return nil, err
	}
	goal := make([]*CommentBase, 0, 15)
	for r.Next() {
		c := &CommentBase{}
		err = r.Scan(&(c.Position), &(c.Like), &(c.SubCommentNumber), &(c.Owner))
		if err != nil {
			return nil, err
		}
		c.Passage = passageId
		goal = append(goal, c)
	}
	return goal, nil
}

func LikeComment(tx *sql.Tx, passageID int64, position uint32) error {
	var likeComment = likeComment
	if tx != nil {
		likeComment = tx.Stmt(likeComment)
	}
	_, err := likeComment.Exec(passageID, position)
	return err
}

func MakeComment(tx *sql.Tx, passageId int64, who int64) (uint32, error) {
	var makeCommentStepUpdate = makeCommentStepUpdate
	var makeCommentStepInsert = makeSubCommentStepInsert
	if tx != nil {
		makeCommentStepUpdate = tx.Stmt(makeCommentStepUpdate)
		makeCommentStepInsert = tx.Stmt(makeCommentStepInsert)
	}
	tx, err := Database.Begin()
	if err != nil {
		return 0, err
	}
	row := makeCommentStepInsert.QueryRow(passageId, who)
	if err != nil {
		return 0, err
	}
	var commentPosition uint32
	err = row.Scan(&commentPosition)
	if err != nil {
		return 0, err
	}
	var line int64
	r, err := makeCommentStepUpdate.Exec(passageId)
	if err != nil {
		return 0, err
	}
	if line, err = r.RowsAffected(); err != nil {
		return 0, err
	} else if line != 1 {
		return 0, UnknownError
	}
	return commentPosition, nil
}
