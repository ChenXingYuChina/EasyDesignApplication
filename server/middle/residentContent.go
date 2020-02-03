package middle

import (
	"EasyDesignApplication/server/asynchronoussIOAndBuffer"
	"EasyDesignApplication/server/base/Passage"
	. "EasyDesignApplication/server/base/comment"
	"EasyDesignApplication/server/base/user"
	"sync"
	"sync/atomic"
	"time"
)

type FullPassage struct {
	*Passage.Passage
	Comments    []*Comment      `json:"com"`
	SubComments [][]*SubComment `json:"sub_com"`
}

func loadFullPassage(id int64) (*FullPassage, error) {
	var err error
	p, err := loadPassageNow(id)
	if err != nil {
		return nil, err
	}
	comments, err := LoadCommentBaseHot(id)
	if err != nil {
		return nil, err
	}
	fullComments := make([]*Comment, 0, len(comments))
	subComments := make([][]*SubComment, 0, len(comments))
	for _, v := range comments {
		fc, err := LoadComment(v)
		scs, err := LoadHotSubComment(id, v.Position)
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
	y
	typeNum
)

type residentContentList struct {
	list           Passage.PassageList
	userMini       []*user.UserMini
	fullPassages   map[int64]*FullPassage
	lock           sync.RWMutex
	passageType    int16
	load           func(t int16) (Passage.PassageList, error)
	helpFsList     []func() (asynchronoussIOAndBuffer.Bean, error)
	lastUpdateTime int64
}

func (t *residentContentList) refresh(gapTime int64) (err error) {
	now := time.Now().Unix()
	if now-atomic.LoadInt64(&(t.lastUpdateTime)) < gapTime {
		return nil
	}
	t.lock.Lock()
	defer t.lock.Unlock()
	now = time.Now().Unix()
	if now-atomic.LoadInt64(&(t.lastUpdateTime)) < gapTime {
		return nil
	}
	defer atomic.StoreInt64(&(t.lastUpdateTime), time.Now().Unix())
	passageList, err := t.load(t.passageType)
	if err != nil {
		return
	}
	fullList := map[int64]*FullPassage{}
	userMiniFunctionList := make([]GetFunction, len(passageList))
	for i, v := range passageList {
		if fp, ok := t.fullPassages[v.Id]; ok {
			fullList[v.Id] = fp
		} else {
			fp, err = loadFullPassage(v.Id)
			if err != nil {
				return
			}
			fullList[v.Id] = fp
		}
		userMiniFunctionList[i] = LoadUserMini(v.Owner)
	}
	userMiniList, err := GetUserMinisFromFunctions(userMiniFunctionList)
	if err != nil {
		return
	}
	t.list = passageList
	t.fullPassages = fullList
	t.userMini = userMiniList
	return nil
}

func (t *residentContentList) getPassageListFromCache(gapTime int64) (pList Passage.PassageList, uList []*user.UserMini) {
	_ = t.refresh(gapTime)
	t.lock.RLock()
	pList = t.list[:]
	uList = t.userMini[:]
	t.lock.RUnlock()
	return
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
	lists   []*residentContentList
	gapTime time.Duration
}

func (t *residentContentTable) getPassageListFromCache(passageType int16) (Passage.PassageList, []*user.UserMini) {
	return t.lists[passageType].getPassageListFromCache(int64(t.gapTime))
}

func (t *residentContentTable) getPassageFromCache(passageType int16, passageId int64) *FullPassage {
	return t.lists[passageType].getPassageFromCache(passageId)
}

func loadLast30(passageType int16) (Passage.PassageList, error) {
	return Passage.LoadPassageListByTypeLast(passageType, 30, 0)
}

var hotTable *residentContentTable
var lastTable *residentContentTable

func prepareResidentContentTable() {
	hotTable = &residentContentTable{gapTime: time.Hour / time.Second, lists: make([]*residentContentList, typeNum)}
	lastTable = &residentContentTable{gapTime: time.Second * 10 / time.Second, lists: make([]*residentContentList, typeNum)}
	for i := 0; i < typeNum; i++ {
		hotTable.lists[i] = &residentContentList{
			fullPassages: map[int64]*FullPassage{},
			passageType:  int16(i),
			//lock:         new(sync.RWMutex),
			load: Passage.LoadHottestPassageListByType,
		}
		_ = hotTable.lists[i].refresh(int64(hotTable.gapTime))
		lastTable.lists[i] = &residentContentList{
			fullPassages: map[int64]*FullPassage{},
			passageType:  int16(i),
			//lock:new(sync.RWMutex),
			load: loadLast30,
		}
		_ = lastTable.lists[i].refresh(int64(hotTable.gapTime))
	}
}

func (t *residentContentTable) checkUpdate(lastTime int64, passageType int16) bool {
	return t.lists[passageType].lastUpdateTime > lastTime
}

func checkLast(lastTime int64, passageType int16) bool {
	return lastTable.checkUpdate(lastTime, passageType)
}

func checkHot(lastTime int64, passageType int16) bool {
	return hotTable.checkUpdate(lastTime, passageType)
}

func CheckUpdate(lastTime int64, passageType int16, hot bool) bool {
	if hot {
		return checkHot(lastTime, passageType)
	} else {
		return checkLast(lastTime, passageType)
	}
}
