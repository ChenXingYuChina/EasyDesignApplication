package middle

import (
	"EasyDesignApplication/server/asynchronoussIOAndBuffer"
	"EasyDesignApplication/server/base"
	"log"
	"sync"
	"time"
)

type FullPassage struct {
	*Passage
	Comments    []*Comment
	SubComments [][]*base.SubComment
}

func loadFullPassage(id int64) (*FullPassage, error) {
	var err error
	p, err := loadPassageNow(id)
	if err != nil {
		return nil, err
	}
	comments, err := base.LoadCommentBaseHot(id)
	if err != nil {
		return nil, err
	}
	fullComments := make([]*Comment, 0, len(comments))
	subComments := make([][]*base.SubComment, 0, len(comments))
	for _, v := range comments {
		fc, err := LoadComment(v)
		scs, err := base.LoadHotSubComment(id, v.Position)
		if err != nil {
			return nil, err
		}
		if err != nil {
			return nil, err
		}
		fullComments = append(fullComments, fc)
		subComments = append(subComments, scs)
	}
	return &FullPassage{p, fullComments, subComments}, nil
}

// this will be the type of passage
const (
	x = iota
	typeNum
)

type residentContentList struct {
	list         PassageList
	userMini     []*base.UserMini
	fullPassages map[int64]*FullPassage
	lock         *sync.RWMutex
	passageType  int16
	load         func(t int16) (PassageList, error)
	helpFsList   []func() (asynchronoussIOAndBuffer.Bean, error)
}

func (t *residentContentList) refresh() error {
	t.lock.RLock()
	passageList, err := t.load(t.passageType)
	if err != nil {
		t.lock.RUnlock()
		return err
	}
	fullList := map[int64]*FullPassage{}
	userMiniFunctionList := make([]GetFunction, len(passageList))
	for i, v := range passageList {
		if fp, ok := t.fullPassages[v.Id]; ok {
			fullList[v.Id] = fp
		} else {
			fp, err = loadFullPassage(v.Id)
			if err != nil {
				t.lock.RUnlock()

				return err
			}
			fullList[v.Id] = fp
		}
		userMiniFunctionList[i] = LoadUserMini(v.Owner)
	}
	t.lock.RUnlock()
	userMiniList, err := GetUserMinisFromFunctions(userMiniFunctionList)
	if err != nil {
		return err
	}
	t.lock.Lock()
	t.list = passageList
	t.fullPassages = fullList
	t.userMini = userMiniList
	t.lock.Unlock()
	return nil
}

func (t *residentContentList) getPassageListFromCache() (PassageList, []*base.UserMini) {
	t.lock.RLock()
	defer t.lock.RUnlock()
	return t.list[:], t.userMini[:]
}

func (t *residentContentList) getPassageFromCache(passageId int64) *FullPassage {
	t.lock.RLock()
	defer t.lock.RUnlock()
	if fp, ok := t.fullPassages[passageId]; ok {
		return fp
	} else {
		return nil
	}
}

type residentContentTable struct {
	lists []*residentContentList
	gapTime time.Duration
}

func (t *residentContentTable) refresh() {
	ticker := time.NewTicker(t.gapTime / typeNum)
	var c int
	for {
		<-ticker.C
		err := t.lists[c].refresh()
		if err != nil {
			log.Println(err)
		}
		c++
		if c > typeNum {
			c = 0
		}
	}
}

func (t *residentContentTable) getPassageListFromCache(passageType int16) (PassageList, []*base.UserMini) {
	return t.lists[passageType].getPassageListFromCache()
}

func (t *residentContentTable) getPassageFromCache(passageType int16, passageId int64) *FullPassage {
	return t.lists[passageType].getPassageFromCache(passageId)
}

func loadLast30(passageType int16) (PassageList, error) {
	return loadPassageListByTypeLast(passageType, 30, 0)
}

var hotTable *residentContentTable
var lastTable *residentContentTable


func PrepareResidentContentTable() {
	hotTable = &residentContentTable{gapTime:time.Hour}
	lastTable = &residentContentTable{gapTime:time.Second * 10}
	for i := 0; i < typeNum; i++ {
		hotTable.lists[i] = &residentContentList{
			fullPassages: map[int64]*FullPassage{},
			passageType:  int16(i),
			lock:         new(sync.RWMutex),
			load:         loadHottestPassageListByType,
		}
		lastTable.lists[i] = &residentContentList{
			fullPassages: map[int64]*FullPassage{},
			passageType:int16(i),
			lock:new(sync.RWMutex),
			load:loadLast30,
		}
	}
}

