package middle

import (
	"EasyDesignApplication/server/base"
	"fmt"
)

type Workshop struct {
	*base.WorkShopBase
	LongDescription *base.ComplexString
}

const (
	workshopLongDescriptionDir      = "/wl"
	workshopLongDescriptionFilename = "/wl/%x.cs"
)

var workshopLongDescriptionRealFilename string

func PrepareWorkshopDir() {
	workshopLongDescriptionRealFilename = base.DataDir + workshopLongDescriptionFilename
	base.CheckAndMakeDir(base.DataDir + workshopLongDescriptionDir)
}

func LoadWorkshop(id int64) (*Workshop, error) {
	goal := &Workshop{}
	var err error
	goal.WorkShopBase, err = base.LoadWorkshopBase(id)
	if err != nil {
		return nil, err
	}
	goal.LongDescription, err = base.LoadComplexStringFromFile(fmt.Sprintf(workshopLongDescriptionRealFilename, id))
	if err != nil {
		return nil, err
	}
	return goal, nil
}

func OpenWorkshop(name string, headImage int64, longDescription *base.ComplexString, who int64) (*Workshop, error) {
	var state uint8
	goal := &Workshop{}
	goal.WorkShopBase, state = base.OpenWorkShop(who, name, headImage)
	if state != 0 {
		switch state {
		case 255:
			return nil, base.UnknownError
		case 1:
			return nil, UnauthorizedError
		}
	}
	goal.LongDescription = longDescription
	err := longDescription.SaveComplexStringToFile(fmt.Sprintf(workshopLongDescriptionRealFilename, goal.WorkShopBase.Id))
	if err != nil {
		return nil, err
	}
	return goal, nil
}
