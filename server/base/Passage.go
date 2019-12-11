package base

import (
	"database/sql"
	"sync"
)

var (
	makePassageStepInsert *sql.Stmt
	makePassageStepUndate *sql.Stmt
	likePassage *sql.Stmt
	changePassageTitle *sql.Stmt
)

func PreparePassageSQL() (uint8, error) {
	var err error
	makePassageStepInsert, err = SQLPrepare("insert into passages (title, owner) values ($1, $2)")
	if err != nil {
		return 0, nil
	}
	makePassageStepUndate, err = SQLPrepare("update users set passage_number = passage_number + 1 where id = $1")
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

var passagePool *sync.Pool

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

func MakePassage(owner int64, title string) error {
	tx, err := Database.Begin()
	_, err = tx.Stmt(makePassageStepInsert).Exec(title, owner)
	if err != nil {
		_ = tx.Rollback()
		return err
	}
	r, err := tx.Stmt(makePassageStepUndate).Exec(owner)
	if err != nil {
		_ = tx.Rollback()
		return err
	}
	if line, err := r.RowsAffected(); err != nil {
		_ = tx.Rollback()
		return err
	} else if line != 1 {
		_ = tx.Rollback()
		return UnknownError
	}
	return err
}

func LikePassage(id int64) error {
	_, err := likePassage.Exec(id)
	return err
}

func ChangeTitle(id int64, title string) error {
	_, err := changePassageTitle.Exec(title, id)
	return err
}
