package middle

import (
	"EasyDesignApplication/server/base"
	"database/sql"
	"log"
)

var (
	getWorkshopPassageList *sql.Stmt
	getPassageListByType *sql.Stmt
	getHottestPassageListByType *sql.Stmt
	getPassageListByUserAndType *sql.Stmt
	searchPassageByTitle *sql.Stmt
)

// &(passage.Title), &(passage.CommentNumber), &(passage.Like), &(passage.Owner), &(passage.Id), &(passage.ListImage)
// 修改最热的查询条件，与时间关联
func PreparePassageListSQL() (uint8, error) {
	var err error
	getWorkshopPassageList, err = base.Database.Prepare("select $1, p.type, p.title, p.comment_number, p.like_number, p.owner, wp.passage_id, p.list_image from workshop_passage wp, passages p where (wp.workshop_id = $1 and p.id = wp.passage_id) order by wp.passage_id desc limit $2 offset $3")
	if err != nil {
		return 0, err
	}
	getPassageListByType, err = base.Database.Prepare("select wp.workshop_id, $1, p.title, p.comment_number, p.like_number, p.owner, wp.passage_id, p.list_image from passages p left join workshop_passage wp on p.id = wp.passage_id where p.type = $1 order by p.id desc limit $2 offset $3")
	if err != nil {
		return 1, err
	}
	getHottestPassageListByType, err = base.Database.Prepare("select wp.workshop_id, $1, p.title, p.comment_number, p.like_number, p.owner, wp.passage_id, p.list_image from passages p left join workshop_passage wp on p.id = wp.passage_id where p.type = $1 order by (p.comment_number + p.like_number) / (now() - p.public_time) desc limit 30")
	if err != nil {
		return 3, err
	}
	getPassageListByUserAndType, err = base.Database.Prepare("select wp.workshop_id, $2, p.title, p.comment_number, p.like_number, $1, wp.passage_id, p.list_image from passages p left join workshop_passage wp on p.id = wp.passage_id where p.owner = $1 and p.type = $2 order by p.id desc limit $3 offset $4")
	if err != nil {
		return 2, err
	}
	searchPassageByTitle, err = base.Database.Prepare("select wp.workshop_id, p.type, p.title, p.comment_number, p.like_number, p.owner, wp.passage_id, p.list_image from passages p left join workshop_passage wp on p.id = wp.passage_id where p.title like '%'+$1+'%' order by p.id desc limit $2 offset $3")
	return 0, nil
}

type PassageListItem struct {
	*base.PassageBase
	WorkshopId int64 `json:"workshop"`
}

type PassageList []PassageListItem


func loadPassageListByTypeLast(t int16, length uint8, offset int64) (PassageList, error) {
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

func loadHottestPassageListByType(t int16) (PassageList, error) {
	r, err :=  getHottestPassageListByType.Query(t)
	if err != nil {
		return nil, err
	}
	goal, err := loadPassageList(r, 30)
	if err != nil {
		return nil, err
	}
	err = r.Close()
	if err != nil {
		log.Println(err)
	}
	return goal, nil
}

func LoadPassageListByWorkshopLast(workshopId int64, length uint8, offset int64) (PassageList, error) {
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

func LoadPassageListByUserAndTypeLast(useId int64, t int16, length uint8, offset int64) (PassageList, error) {
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
		helpSlice = append(helpSlice, &(goal[i].WorkshopId))
		goal[i].PassageBase, err = base.LoadPassagesBase(r, helpSlice)
		if err != nil {
			err = r.Close()
			if err != nil {
				log.Println(err)
			}
			return nil, err
		}
		helpSlice = helpSlice[:0]
	}
	goal = goal[:i]
	return goal, nil
}
