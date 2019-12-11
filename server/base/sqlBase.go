package base

import (
	"database/sql"
	"fmt"
	_ "github.com/lib/pq"
)

var Database *sql.DB

func SqlInit(sqlName, databaseName, password, userName, host string) error {
	var err error
	Database, err = sql.Open("postgres", fmt.Sprintf("dbname=%s user=%s host=%s password=%s sslmode=disable", databaseName, userName, host, password))
	if err != nil {
		return err
	}
	return nil
}

func SQLPrepare(query string) (*sql.Stmt, error) {
	return Database.Prepare(query)
}
