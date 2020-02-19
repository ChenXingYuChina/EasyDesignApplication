package Passage

import (
	"EasyDesignApplication/server/base"
	"database/sql"
	"log"
)

var (
	getWorkshopPassageList      *sql.Stmt
	getPassageListByType        *sql.Stmt
	getHottestPassageListByType *sql.Stmt
	getPassageListByUserAndType *sql.Stmt
	searchPassageByTitle        *sql.Stmt
	starPassageByUser           *sql.Stmt
)

// &(passage.Title), &(passage.CommentNumber), &(passage.Like), &(passage.Owner), &(passage.Id), &(passage.ListImage)
// 修改最热的查询条件，与时间关联
func preparePassageListSQL() (uint8, error) {
	var err error
	getWorkshopPassageList, err = base.Database.Prepare("select COALESCE(wp.workshop_id, 0), p.type, p.title, p.comment_number, p.like_number, p.owner, p.id, p.list_image from workshop_passage wp, passages p where (wp.workshop_id = $1 and p.id = wp.passage_id) order by wp.passage_id desc limit $2 offset $3")
	if err != nil {
		return 0, err
	}
	getPassageListByType, err = base.Database.Prepare("select COALESCE(wp.workshop_id, 0), p.type, p.title, p.comment_number, p.like_number, p.owner, p.id, p.list_image from passages p left join workshop_passage wp on p.id = wp.passage_id where p.type = $1 order by p.id desc limit $2 offset $3")
	if err != nil {
		return 1, err
	}
	getHottestPassageListByType, err = base.Database.Prepare("select COALESCE(wp.workshop_id, 0), p.type, p.title, p.comment_number, p.like_number, p.owner, p.id, p.list_image from passages p left join workshop_passage wp on p.id = wp.passage_id where p.type = $1 order by (p.comment_number + p.like_number) / extract(epoch from (now() - p.public_time)) desc limit 30")
	if err != nil {
		return 3, err
	}
	getPassageListByUserAndType, err = base.Database.Prepare("select COALESCE(wp.workshop_id, 0), p.type, p.title, p.comment_number, p.like_number, p.owner, p.id, p.list_image from passages p left join workshop_passage wp on p.id = wp.passage_id where p.owner = $1 and p.type = $2 order by p.id desc limit $3 offset $4")
	if err != nil {
		return 2, err
	}
	searchPassageByTitle, err = base.Database.Prepare("select COALESCE(wp.workshop_id, 0), p.type, p.title, p.comment_number, p.like_number, p.owner, p.id, p.list_image from passages p left join workshop_passage wp on p.id = wp.passage_id where p.title like $1 order by p.id desc limit $2 offset $3")
	if err != nil {
		return 4, err
	}
	starPassageByUser, err = base.Database.Prepare(`with ps as (select COALESCE(wp.workshop_id, 0) as workshop, p.type as type, p.title as title, p.comment_number as comment_number, p.like_number as like_number, p.owner as owner, p.id as id, p.list_image as list_image from passages p left join workshop_passage wp on p.id = wp.passage_id) select workshop, type, title, comment_number, like_number, owner, id, list_image from ps inner join star_passage sp on sp.passage_id = ps.id where sp.user_id = $1 limit $2 offset $3`)
	if err != nil {
		return 5, err
	}
	return 0, nil
}

type PassageListItem struct {
	*PassageBase
	WorkshopId int64 `json:"workshop"`
}

type PassageList []PassageListItem

func LoadPassageListByTypeLast(t int16, length uint8, offset int64) (PassageList, error) {
	r, err := getPassageListByType.Query(t, length, offset)
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

const length = 30

func LoadHottestPassageListByType(t int16) (PassageList, error) {
	r, err := getHottestPassageListByType.Query(t)
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

func LoadPassageListByWorkshopLast(workshopId int64, length uint8, offset int64) (PassageList, error) {
	r, err := getWorkshopPassageList.Query(workshopId, length, offset)
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
	r, err := getPassageListByUserAndType.Query(useId, t, length, offset)
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
	r, err := searchPassageByTitle.Query("%"+keyword+"%", length, offset)
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
	var err error
	for i := 0; r.Next(); i++ {
		goal = append(goal, PassageListItem{})
		helpSlice = append(helpSlice, &(goal[i].WorkshopId))
		goal[i].PassageBase, err = loadPassagesBase(r, helpSlice)
		if err != nil {
			err = r.Close()
			if err != nil {
				log.Println(err)
			}
			return nil, err
		}
		helpSlice = helpSlice[:0]
	}
	return goal, nil
}

func LoadStarPassage(uid int64, length uint8, offset int64) (PassageList, error) {
	r, err := starPassageByUser.Query(uid, length, offset)
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
