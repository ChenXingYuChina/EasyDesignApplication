package user

import (
	"EasyDesignApplication/server/base"
	"EasyDesignApplication/server/base/ComplexString"
	"database/sql"
	"fmt"
)

type Workshop struct {
	*WorkShopBase
	LongDescription *ComplexString.ComplexString
}

const (
	workshopLongDescriptionDir      = "/wl"
	workshopLongDescriptionFilename = "/wl/%x.cs"
)

const (
	Normal = iota
	Manager
	Owner
)

var workshopLongDescriptionRealFilename string

func PrepareWorkshopDir() {
	workshopLongDescriptionRealFilename = base.DataDir + workshopLongDescriptionFilename
	base.CheckAndMakeDir(base.DataDir + workshopLongDescriptionDir)
}

func LoadWorkshop(id int64) (*Workshop, error) {
	goal := &Workshop{}
	var err error
	goal.WorkShopBase, err = loadWorkshopBase(id)
	if err != nil {
		return nil, err
	}
	goal.LongDescription, err = ComplexString.LoadComplexStringFromFile(fmt.Sprintf(workshopLongDescriptionRealFilename, id))
	if err != nil {
		return nil, err
	}
	return goal, nil
}

func OpenWorkshop(name string, headImage int64, longDescription *ComplexString.ComplexString, who int64) (*Workshop, error) {
	var state uint8
	goal := &Workshop{}
	err := base.InTransaction(func(tx *sql.Tx) ([]string, error) {
		goal.WorkShopBase, state = openWorkShop(tx, who, name, headImage)
		if state != 0 {
			switch state {
			case 255:
				return nil, base.UnknownError
			case 1:
				return nil, UnauthorizedError
			}
		}
		goal.LongDescription = longDescription
		fileName := fmt.Sprintf(workshopLongDescriptionRealFilename, goal.WorkShopBase.Id)
		err := longDescription.SaveComplexStringToFile(fileName)
		if err != nil {
			return []string{fileName}, err
		}
		return nil, nil
	})
	return goal, err
}
