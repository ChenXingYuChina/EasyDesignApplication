package base

import (
	"database/sql"
)

var (
	makePassageStepInsert *sql.Stmt
	makePassageStepUpdate *sql.Stmt
	likePassage           *sql.Stmt
	changePassageTitle    *sql.Stmt
)

func PreparePassageSQL() (uint8, error) {
	var err error
	makePassageStepInsert, err = SQLPrepare("insert into passages (title, owner, list_image, type) values ($1, $2, $3, $4) returning id")
	if err != nil {
		return 0, nil
	}
	makePassageStepUpdate, err = SQLPrepare("update users set passage_number = passage_number + 1 where id = $1")
	if err != nil {
		return 4, err
	}
	likePassage, err = SQLPrepare("update passages set like_number = like_number + 1 where id = $1")
	if err != nil {
		return 2, err
	}
	changePassageTitle, err = SQLPrepare("update passages set title = $1 where id = $2")
	if err != nil {
		return 3, err
	}
	return 0, nil
}

type PassageBase struct {
	Id int64
	Title string
	Owner int64
	ListImage int64
	Like uint32
	CommentNumber uint32
	Type int16
}

func LoadPassageBase(row *sql.Row) (*PassageBase, error) {
	passage := &PassageBase{}
	err := row.Scan(&(passage.Title), &(passage.CommentNumber), &(passage.Like), &(passage.Owner), &(passage.Id), &(passage.ListImage), &(passage.Type))
	if err != nil {
		return nil, err
	}
	return passage, nil
}

func LoadPassagesBase(rows *sql.Rows, otherParts []interface{}) (*PassageBase, error) {
	if !rows.Next() {
		return nil, nil
	}
	passage := &PassageBase{}
	otherParts = append(otherParts, &(passage.Type), &(passage.Title), &(passage.CommentNumber), &(passage.Like), &(passage.Owner), &(passage.Id), &(passage.ListImage))
	err := rows.Scan(otherParts...)
	if err != nil {
		return nil, err
	}
	return passage, nil
}

func MakePassage(tx *sql.Tx, owner int64, title string, listImage int64, t int16) (int64, error) {
	var makePassageStepUpdate = makePassageStepUpdate
	var makePassageStepInsert = makePassageStepInsert
	if tx != nil {
		makeCommentStepUpdate = tx.Stmt(makeCommentStepUpdate)
		makeCommentStepInsert = tx.Stmt(makeCommentStepInsert)
	}
	row := tx.Stmt(makePassageStepInsert).QueryRow(title, owner, listImage, t)
	var pId int64
	err := row.Scan(&pId)
	if err != nil {
		return 0, err
	}
	r, err := tx.Stmt(makePassageStepUpdate).Exec(owner)
	if err != nil {
		return 0, err
	}
	if line, err := r.RowsAffected(); err != nil {
		return 0, err
	} else if line != 1 {
		return 0, UnknownError
	}
	return pId, err
}

func LikePassage(tx *sql.Tx, id int64) error {
	var likePassage = likePassage
	if tx != nil {
		tx.Stmt(likePassage)
	}
	_, err := likePassage.Exec(id)
	return err
}

func ChangeTitle(tx *sql.Tx, id int64, title string) error {
	var changePassageTitle = changePassageTitle
	if tx != nil {
		changePassageTitle = tx.Stmt(changePassageTitle)
	}
	_, err := changePassageTitle.Exec(title, id)
	return err
}
