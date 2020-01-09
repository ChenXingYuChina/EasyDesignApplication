package middle

import "EasyDesignApplication/server/base"

func LastPassageListByTypeLast(passageType int16, begin int64, length uint8) (PassageList, []*base.UserMini, error) {
	pList, uList := lastTable.getPassageListFromCache(passageType)
	l := int64(len(pList))
	var err error
	if begin > l {
		pList, err = loadPassageListByTypeLast(passageType, length, begin)
		if err != nil {
			return nil, nil, err
		}
		ids := make([]int64, len(pList))
		for i, v := range pList {
			ids[i] = v.Owner
		}
		fs := make([]GetFunction, len(pList))
		uList, err = GetUserMinisFromFunctions(LoadUserMinis(ids, fs))
		if err != nil {
			return nil, nil, err
		}
		return pList, uList, nil
	} else {
		end := begin + int64(begin)
		if end >= l {
			return pList[begin:], uList, nil
		} else {
			return pList[begin:end], uList[begin:end], nil
		}
	}
}

func LoadHotPassageList(passageType int16, begin int64, length uint8) (PassageList, []*base.UserMini, error) {
	pList, uList := hotTable.getPassageListFromCache(passageType)
	l := int64(len(pList))
	if begin > l {
		return PassageList{}, []*base.UserMini{}, nil
	} else {
		end := begin + int64(length)
		if end >= l {
			return pList[begin:], uList, nil
		} else {
			return pList[begin:end], uList[begin:end], nil
		}
	}
}

func LoadFullPassageFromHot(passageType int16, passageId int64) (*FullPassage, GetFunction) {
	p := hotTable.getPassageFromCache(passageType, passageId)
	if p != nil {
		return p, nil
	}
	return nil, loadPassage(passageId)
}

func LoadFullPassageFromLast(passageType int16, passageId int64) (*FullPassage, GetFunction) {
	p := lastTable.getPassageFromCache(passageType, passageId)
	if p != nil {
		return p, nil
	}
	return nil, loadPassage(passageId)
}
