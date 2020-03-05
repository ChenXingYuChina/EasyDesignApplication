package Passage

import (
	"EasyDesignApplication/server/base"
	"log"
)

func init() {
	base.RegisterPrepare(func() {
		s, err := prepare()
		if err != nil {
			log.Fatal(err, s)
		}
	})
}

func prepare() (s uint8, err error) {
	preparePassageDir()
	s, err = preparePassageSQL()
	if err != nil {
		return
	}
	s, err = preparePassageListSQL()
	if err != nil {
		return
	}
	s, err = preparePassageBodySQL()
	if err != nil {
		return
	}
	s, err = preparePassageAction()
	if err != nil {
		return
	}
	return
}
