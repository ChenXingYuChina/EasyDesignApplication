package middle

import "log"

func Prepare() {
	PrepareCommentDir()
	PreparePassageDir()
	PrepareUserDir()
	PrepareWorkshopDir()
	s, err := PreparePassageBodySQL()
	if err != nil {
		log.Fatal("prepare middle Passage Sql: ", s, err)
	}
	s, err = PreparePassageListSQL()
	if err != nil {
		log.Fatal("prepare middle Passage List Sql: ", s, err)
	}
	s, err = PrepareUserSQL()
	if err != nil {
		log.Fatal("prepare middle User Sql: ", s, err)
	}
	PrepareResidentContentTable()
}
