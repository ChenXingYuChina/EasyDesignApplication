package middle

import (
	"EasyDesignApplication/server/base"
	"database/sql"
	"log"
)

var (
	getWorkshopPassageList *sql.Stmt
	getPassageListByType *sql.Stmt
	getPassageListByUserAndType *sql.Stmt
	searchPassageByTitle *sql.Stmt
)

func PreparePassageListSQL() (uint8, error) {
	var err error
	getWorkshopPassageList, err = base.Database.Prepare("select wp.passage_id, p.owner, pt.passage_type, p.title, p.comment_number, p.like_number from passage_type pt, workshop_passage wp, passages p where (wp.workshop_id = $1 and wp.passage_id = pt.passage_id and p.id = wp.passage_id) order by wp.passage_id desc limit $2 offset $3")
	if err != nil {
		return 0, err
	}
	getPassageListByType, err = base.Database.Prepare("select pt.passage_id, wp.workshop_id, p.owner, p.like_number, p.comment_number, p.title from passage_type pt, passages p left join workshop_passage wp on p.id = wp.passage_id where pt.passage_type = $1 and pt.passage_id = p.id order by pt.passage_id desc limit $2 offset $3")
	if err != nil {
		return 1, err
	}
	getPassageListByUserAndType, err = base.Database.Prepare("select pt.passage_id, wp.workshop_id, p.like_number, p.comment_number, p.title from passage_type pt, passages p left join workshop_passage wp on p.id = wp.passage_id where p.owner = $1 and pt.passage_type = $2 and pt.passage_id = p.id order by pt.passage_id desc limit $3 offset $4")
	if err != nil {
		return 2, err
	}
	searchPassageByTitle, err = base.Database.Prepare("select p.id, wp.workshop_id, pt.passage_type, p.owner, p.like_number, p.comment_number, p.title from passage_type pt, passages p left join workshop_passage wp on p.id = wp.passage_id where p.title like '%'+$1+'%' and pt.passage_id = p.id order by pt.passage_id desc limit $2 offset $3")
	return 0, nil
}

type PassageListItem struct {
	*base.PassageBase
	WorkshopId int64
	PassageType int16
}

type PassageList []PassageListItem

const (

)

func LoadPassageListByType(t int16, length uint8, offset int64) (PassageList, error) {
	r, err :=  getPassageListByType.Query(t, length, offset)
	if err != nil {
		return nil, err
	}
	goal := make([]PassageListItem, length)
	for i := 0; r.Next(); i++ {
		goal[i].PassageBase = base.GetPassage()
		err = r.Scan(&(goal[i].Id), &(goal[i].WorkshopId), &(goal[i].Owner), &(goal[i].Like), &(goal[i].CommentNumber), &(goal[i].Title))
		if err != nil {
			err = r.Close()
			if err != nil {
				log.Println(err)
			}
			for ; i >= 0; i-- {
				base.RecyclePassage(goal[i].PassageBase)
			}
			return nil, err
		}
		goal[i].PassageType = t
	}
	err = r.Close()
	if err != nil {
		log.Println(err)
	}
	return goal, nil
}

func LoadPassageListByWorkshop(workshopId int64, length uint8, offset int64) (PassageList, error) {
	r, err :=  getWorkshopPassageList.Query(workshopId, length, offset)
	if err != nil {
		return nil, err
	}
	goal := make([]PassageListItem, length)
	for i := 0; r.Next(); i++ {
		goal[i].PassageBase = base.GetPassage()
		err = r.Scan(&(goal[i].Id), &(goal[i].Owner), &(goal[i].PassageType), &(goal[i].Title), &(goal[i].CommentNumber), &(goal[i].Like))
		if err != nil {
			err = r.Close()
			if err != nil {
				log.Println(err)
			}
			for ; i >= 0; i-- {
				base.RecyclePassage(goal[i].PassageBase)
			}
			return nil, err
		}
		goal[i].WorkshopId = workshopId
	}
	err = r.Close()
	if err != nil {
		log.Println(err)
	}
	return goal, nil
}

func LoadPassageListByUserAndType(useId int64, length uint8, offset int64, t int16) (PassageList, error) {
	r, err :=  getPassageListByUserAndType.Query(useId, t, length, offset)
	if err != nil {
		return nil, err
	}
	goal := make([]PassageListItem, length)
	for i := 0; r.Next(); i++ {
		goal[i].PassageBase = base.GetPassage()
		err = r.Scan(&(goal[i].Id), &(goal[i].WorkshopId), &(goal[i].Like), &(goal[i].CommentNumber), &(goal[i].Title))
		if err != nil {
			err = r.Close()
			if err != nil {
				log.Println(err)
			}
			for ; i >= 0; i-- {
				base.RecyclePassage(goal[i].PassageBase)
			}
			return nil, err
		}
		goal[i].PassageType = t
		goal[i].Owner = useId
	}
	err = r.Close()
	if err != nil {
		log.Println(err)
	}
	return goal, nil
}

func SearchPassageByTitle(keyword string, length uint8, offset int64) (PassageList, error) {
	r, err :=  searchPassageByTitle.Query(keyword, length, offset)
	if err != nil {
		return nil, err
	}
	goal := make([]PassageListItem, length)
	for i := 0; r.Next(); i++ {
		goal[i].PassageBase = base.GetPassage()
		err = r.Scan(&(goal[i].Id), &(goal[i].WorkshopId), &(goal[i].PassageType), &(goal[i].Owner), &(goal[i].Like), &(goal[i].CommentNumber), &(goal[i].Title))
		if err != nil {
			err = r.Close()
			if err != nil {
				log.Println(err)
			}
			for ; i >= 0; i-- {
				base.RecyclePassage(goal[i].PassageBase)
			}
			return nil, err
		}
	}
	err = r.Close()
	if err != nil {
		log.Println(err)
	}
	return goal, nil
}
