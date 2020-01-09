package base

import (
	"database/sql"
)

var (
	likeSubComment *sql.Stmt
	loadSubComment *sql.Stmt
	loadSubCommentHot *sql.Stmt
	makeSubCommentStepInsert *sql.Stmt
	makeSubCommentStepUpdate *sql.Stmt

)

type SubComment struct {
	Passage int64 `json:"passage"`
	Owner int64 `json:"owner"`
	Content string `json:"content"`
	Like uint32 `json:"like"`
	Father uint32 `json:"father"`
	Position uint16 `json:"position"`
}

func PrepareSubCommentSQL() (uint8, error) {
	var err error
	loadSubComment, err = SQLPrepare("select content, position, like_number from subcomment where passage_id = $1 and father = $2 order by position ASC limit $4 offset $3")
	if err != nil {
		return 0, err
	}
	loadSubCommentHot, err = Database.Prepare("select content, position, like_number from subcomment where passage_id = $1 and father = $2 order by like_number desc limit 3")
	if err != nil {
		return 3, err
	}
	likeSubComment, err = SQLPrepare("update subcomment set like_number = like_number + 1 where passage_id = $1 and father = $2 and position = $3")
	if err != nil {
		return 1, err
	}
	makeSubCommentStepInsert, err = SQLPrepare("insert into subcomment (passage_id, father, position, content, owner) select $1, $2, c.sub_comment + 1, $3, $4 from comment c where c.passage_id = $1 and c.position = $2")
	if err != nil {
		return 2, err
	}
	makeSubCommentStepUpdate, err = SQLPrepare("update comment set sub_comment = sub_comment + 1 where passage_id = $1 and position = $2")
	return 0, nil
}

func LikeSubComment(tx *sql.Tx, passage int64, father uint32, position uint16) bool {
	var likeSubComment = likeSubComment
	if tx != nil {
		likeSubComment = tx.Stmt(likeSubComment)
	}
	r, err := likeSubComment.Exec(passage, father, position)
	if err != nil {
		return false
	}
	if n, err := r.RowsAffected(); err != nil || n != 1 {
		return false
	}
	return true
}

func LoadSubCommentByPosition(passageId int64, fatherPosition uint32, begin uint16, length uint16) ([]*SubComment, error) {
	r, err := loadSubComment.Query(passageId, fatherPosition, begin, length)
	if err != nil {
		return nil, err
	}
	goal := make([]*SubComment, 0, length)
	for r.Next() {
		c := &SubComment{}
		err = r.Scan(&(c.Content), &(c.Position), &(c.Like))
		if err != nil {
			return nil, err
		}
		c.Father = fatherPosition
		c.Passage = passageId
		goal = append(goal, c)
	}
	return goal, nil
}

func LoadHotSubComment(passageId int64, fatherPosition uint32) ([]*SubComment, error) {
	r, err := loadSubCommentHot.Query(passageId, fatherPosition)
	if err != nil {
		return nil, err
	}
	goal := make([]*SubComment, 0, 3)
	for r.Next() {
		c := &SubComment{}
		err = r.Scan(&(c.Content), &(c.Position), &(c.Like))
		if err != nil {
			return nil, err
		}
		c.Father = fatherPosition
		c.Passage = passageId
		goal = append(goal, c)
	}
	return goal, nil
}

func CommentTo(tx *sql.Tx, passage int64, commentPosition uint32, content string, who int64) error {
	var makeSubCommentStepInsert = makeSubCommentStepInsert
	var makeSubCommentStepUpdate = makeSubCommentStepUpdate
	if tx != nil {
		makeSubCommentStepInsert = tx.Stmt(makeSubCommentStepInsert)
		makeSubCommentStepUpdate = tx.Stmt(makeSubCommentStepUpdate)
	}
	_, err := tx.Stmt(makeSubCommentStepInsert).Exec(passage, commentPosition, content, who)
	if err != nil {
		return err
	}
	r, err := tx.Stmt(makeSubCommentStepUpdate).Exec(passage, commentPosition)
	if err != nil {
		return err
	}
	if line, err := r.RowsAffected(); err != nil{
		return err
	} else if line != 1 {
		return UnknownError
	}
	return nil
}
