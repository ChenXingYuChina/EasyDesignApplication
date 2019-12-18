package base

import (
	"database/sql"
	"sync"
)

var (
	makePassageStepInsert *sql.Stmt
	makePassageStepUpdate *sql.Stmt
	likePassage           *sql.Stmt
	changePassageTitle    *sql.Stmt
)

func PreparePassageSQL() (uint8, error) {
	var err error
	makePassageStepInsert, err = SQLPrepare("insert into passages (title, owner) values ($1, $2) returning id")
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
	Like uint32
	CommentNumber uint32
}

var passagePool = new(sync.Pool)

func init() {
	passagePool.New = func() interface{} {
		return &PassageBase{}
	}
}

func GetPassage() *PassageBase {
	return passagePool.Get().(*PassageBase)
}

func RecyclePassage(p *PassageBase) {
	passagePool.Put(p)
}

func LoadPassageBase(row *sql.Row) (*PassageBase, error) {
	passage := GetPassage()
	err := row.Scan(&(passage.Title), &(passage.CommentNumber), &(passage.Like), &(passage.Owner), &(passage.Id))
	if err != nil {
		return nil, err
	}
	return passage, nil
}

func LoadPassagesBase(rows *sql.Rows) (*PassageBase, error) {
	if !rows.Next() {
		return nil, nil
	}
	passage := GetPassage()
	err := rows.Scan(&(passage.Title), &(passage.CommentNumber), &(passage.Like), &(passage.Owner), &(passage.Id))
	if err != nil {
		return nil, err
	}
	return passage, nil
}

func MakePassage(owner int64, title string) (*sql.Tx, int64, error) {
	tx, err := Database.Begin()
	if err != nil {
		_ = tx.Rollback()
		return nil, 0, err
	}
	row := tx.Stmt(makePassageStepInsert).QueryRow(title, owner)
	var pId int64
	err = row.Scan(&pId)
	if err != nil {
		return nil, 0, err
	}
	r, err := tx.Stmt(makePassageStepUpdate).Exec(owner)
	if err != nil {
		_ = tx.Rollback()
		return nil, 0, err
	}
	if line, err := r.RowsAffected(); err != nil {
		_ = tx.Rollback()
		return nil, 0, err
	} else if line != 1 {
		_ = tx.Rollback()
		return nil, 0, UnknownError
	}
	return tx, pId, err
}

func LikePassage(id int64) error {
	_, err := likePassage.Exec(id)
	return err
}

func ChangeTitle(id int64, title string) error {
	_, err := changePassageTitle.Exec(title, id)
	return err
}
