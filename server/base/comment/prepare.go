package comment

import (
	"EasyDesignApplication/server/base"
	"log"
)

func init() {
	base.RegisterPrepare(func() {
		if s, err := prepare(); err != nil {
			log.Fatal(err, s)
		}
	})
}

func prepare() (s uint8, err error) {
	prepareCommentDir()
	s, err = prepareCommentSQL()
	if err != nil {
		return
	}
	return prepareSubCommentSQL()
}
