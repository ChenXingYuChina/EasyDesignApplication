package middle

import (
	. "EasyDesignApplication/server/base/Passage"
	"EasyDesignApplication/server/base/user"
)

func LoadLastPassageListByType(passageType int16, begin int64, length uint8) (PassageList, []*user.UserMini, error) {
	pList, uList := lastTable.getPassageListFromCache(passageType)
	l := int64(len(pList))
	if begin > l {
		return loadLastPassageListByType(passageType, begin, length)
	} else {
		end := begin + int64(length)
		if end > l {
			pl, ul, err := loadLastPassageListByType(passageType, l, uint8(l-end))
			if err != nil {
				return pList[begin:], uList[begin:], nil
			}
			return append(pList[begin:], pl...), append(uList[begin:], ul...), nil
		} else {
			return pList[begin:end], uList[begin:end], nil
		}
	}
}

func loadLastPassageListByType(passageType int16, begin int64, length uint8) (PassageList, []*user.UserMini, error) {
	pList, err := LoadPassageListByTypeLast(passageType, length, begin)
	if err != nil {
		return nil, nil, err
	}
	return pList, []*user.UserMini{}, nil
}

func LoadHotPassageList(passageType int16, begin int64, length uint8) (PassageList, []*user.UserMini, error) {
	pList, uList := hotTable.getPassageListFromCache(passageType)
	l := int64(len(pList))
	if begin > l {
		return PassageList{}, []*user.UserMini{}, nil
	} else {
		end := begin + int64(length)
		if end >= l {
			return pList[begin:], uList, nil
		} else {
			return pList[begin:end], uList[begin:end], nil
		}
	}
}

func LoadFullPassageFromCache(passageType int16, passageId int64) (*FullPassage, GetFunction) {
	p := hotTable.getPassageFromCache(passageType, passageId)
	if p != nil {
		return p, nil
	}
	p = lastTable.getPassageFromCache(passageType, passageId)
	if p != nil {
		return p, nil
	}
	return nil, loadPassage(passageId)
}

