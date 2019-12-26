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

// &(passage.Title), &(passage.CommentNumber), &(passage.Like), &(passage.Owner), &(passage.Id), &(passage.ListImage)
func PreparePassageListSQL() (uint8, error) {
	var err error
	getWorkshopPassageList, err = base.Database.Prepare("select $1, pt.passage_type, p.title, p.comment_number, p.like_number, p.owner, wp.passage_id, p.list_image from passage_type pt, workshop_passage wp, passages p where (wp.workshop_id = $1 and wp.passage_id = pt.passage_id and p.id = wp.passage_id) order by wp.passage_id desc limit $2 offset $3")
	if err != nil {
		return 0, err
	}
	getPassageListByType, err = base.Database.Prepare("select wp.workshop_id, $1, p.title, p.comment_number, p.like_number, p.owner, wp.passage_id, p.list_image from passage_type pt, passages p left join workshop_passage wp on p.id = wp.passage_id where pt.passage_type = $1 and pt.passage_id = p.id order by pt.passage_id desc limit $2 offset $3")
	if err != nil {
		return 1, err
	}
	getPassageListByUserAndType, err = base.Database.Prepare("select wp.workshop_id, $2, p.title, p.comment_number, p.like_number, $1, wp.passage_id, p.list_image from passage_type pt, passages p left join workshop_passage wp on p.id = wp.passage_id where p.owner = $1 and pt.passage_type = $2 and pt.passage_id = p.id order by pt.passage_id desc limit $3 offset $4")
	if err != nil {
		return 2, err
	}
	searchPassageByTitle, err = base.Database.Prepare("select wp.workshop_id, pt.passage_type, p.title, p.comment_number, p.like_number, p.owner, wp.passage_id, p.list_image from passage_type pt, passages p left join workshop_passage wp on p.id = wp.passage_id where p.title like '%'+$1+'%' and pt.passage_id = p.id order by pt.passage_id desc limit $2 offset $3")
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
	goal, err := loadPassageList(r, length)
	if err != nil {
		return nil, err
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

	goal, err := loadPassageList(r, length)
	if err != nil {
		return nil, err
	}
	err = r.Close()
	if err != nil {
		log.Println(err)
	}
	return goal, nil
}

func LoadPassageListByUserAndType(useId int64, t int16, length uint8, offset int64) (PassageList, error) {
	r, err :=  getPassageListByUserAndType.Query(useId, t, length, offset)
	if err != nil {
		return nil, err
	}
	goal, err := loadPassageList(r, length)
	if err != nil {
		return nil, err
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
	goal, err := loadPassageList(r, length)
	if err != nil {
		return nil, err
	}
	err = r.Close()
	if err != nil {
		log.Println(err)
	}
	return goal, nil
}


func loadPassageList(r *sql.Rows, length uint8) (PassageList, error) {
	goal := make([]PassageListItem, 0, length)
	var helpSlice []interface{}
	var i int
	var err error
	for ; r.Next(); i++ {
		helpSlice = append(helpSlice, &(goal[i].WorkshopId), &(goal[i].PassageType))
		goal[i].PassageBase, err = base.LoadPassagesBase(r, helpSlice)
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
		helpSlice = helpSlice[:0]
	}
	goal = goal[:i]
	return goal, nil
}
