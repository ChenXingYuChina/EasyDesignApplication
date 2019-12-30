package middle

import (
	"EasyDesignApplication/server/asynchronoussIOAndBuffer"
	"EasyDesignApplication/server/base"
)

const (
	dataTypeMediaPiece = iota
	dataTypeImageData
	dataTypePassage
	dataTypeUserMini
)

type GetFunction func() (asynchronoussIOAndBuffer.Bean, error)

type mediaPiece base.MediaPiece
func (m *mediaPiece) GetKey() asynchronoussIOAndBuffer.Key {
	return mediaPieceKey(m.Id)
}

type mediaPieceKey int64
func (k mediaPieceKey) Uid() int64 {
	return int64(k)
}
func (mediaPieceKey) TypeId() uint8 {
	return dataTypeMediaPiece
}
func (k mediaPieceKey) GetKey() asynchronoussIOAndBuffer.Key {
	return k
}

type mediaPieceDataSource struct {}
func (mediaPieceDataSource) Load(key asynchronoussIOAndBuffer.Key) (asynchronoussIOAndBuffer.Bean, error) {
	m, err := base.LoadMediaPiece(key.Uid())
	return (*mediaPiece)(m), err
}
func (mediaPieceDataSource) Save(bean asynchronoussIOAndBuffer.Bean) error {
	return base.SaveMediaPiece((*base.MediaPiece)(bean.(*mediaPiece)))
}
func (mediaPieceDataSource) Delete(key asynchronoussIOAndBuffer.Key) error {
	return base.DeleteMediaPiece(int64(key.(mediaPieceKey)))
}

func LoadMediaPieceNow(id int64) (*base.MediaPiece, error) {
	b, err := asynchronoussIOAndBuffer.DataManager.Load(mediaPieceKey(id))()
	if err != nil {
		return nil, err
	}
	return (*base.MediaPiece)(b.(*mediaPiece)), nil
}
func LoadMediaPiece(id int64) GetFunction {
	return asynchronoussIOAndBuffer.DataManager.Load(mediaPieceKey(id))
}
func GetMediaPieceFromFunction(f GetFunction) (*base.MediaPiece, error) {
	b, err := f()
	if err != nil {
		return nil, err
	}
	return (*base.MediaPiece)(b.(*mediaPiece)), nil
}


type imageData base.ImageData
func (d *imageData) GetKey() asynchronoussIOAndBuffer.Key {
	return imageDataKey(d.Id)
}

type imageDataKey int64
func (k imageDataKey) Uid() int64 {
	return int64(k)
}
func (imageDataKey) TypeId() uint8 {
	return dataTypeImageData
}
func (k imageDataKey) GetKey() asynchronoussIOAndBuffer.Key {
	return k
}

type imageDataSource struct {}
func (imageDataSource) Load(key asynchronoussIOAndBuffer.Key) (asynchronoussIOAndBuffer.Bean, error) {
	i, err := base.LoadImageData(key.Uid())
	return (*imageData)(i), err
}
func (imageDataSource) Save(bean asynchronoussIOAndBuffer.Bean) error {
	return base.SaveImageData((*base.ImageData)(bean.(*imageData)))
}
func (imageDataSource) Delete(key asynchronoussIOAndBuffer.Key) error {
	return base.DeleteImageData(int64(key.(imageDataKey)))
}

func LoadImageDataNow(id int64) (*base.ImageData, error) {
	b, err := asynchronoussIOAndBuffer.DataManager.Load(imageDataKey(id))()
	if err != nil {
		return nil, err
	}
	return (*base.ImageData)(b.(*imageData)), nil
}
func LoadImageData(id int64) GetFunction {
	return asynchronoussIOAndBuffer.DataManager.Load(imageDataKey(id))
}
func GetImageDataFromFunction(f GetFunction) (*base.ImageData, error) {
	b, err := f()
	if err != nil {
		return nil, err
	}
	return (*base.ImageData)(b.(*imageData)), nil
}

type passage Passage
func (c *passage) GetKey() asynchronoussIOAndBuffer.Key {
	return passageKey(c.Id)
}

type passageKey int64
func (k passageKey) Uid() int64 {
	return int64(k)
}
func (passageKey) TypeId() uint8 {
	return dataTypePassage
}
func (k passageKey) GetKey() asynchronoussIOAndBuffer.Key {
	return k
}

type passageDataSource struct {}
func (passageDataSource) Load(key asynchronoussIOAndBuffer.Key) (asynchronoussIOAndBuffer.Bean, error) {
	p, err := loadPassageFromDisk(key.Uid())
	return (*passage)(p), err
}
func (passageDataSource) Save(bean asynchronoussIOAndBuffer.Bean) error {
	return nil
}
func (passageDataSource) Delete(key asynchronoussIOAndBuffer.Key) error {
	return nil
}

func loadPassageNow(id int64) (*Passage, error) {
	b, err := asynchronoussIOAndBuffer.DataManager.Load(passageKey(id))()
	if err != nil {
		return nil, err
	}
	return (*Passage)(b.(*passage)), err
}
func loadPassage(id int64) GetFunction {
	return asynchronoussIOAndBuffer.DataManager.Load(passageKey(id))
}
func GetPassageFromFunction(f GetFunction) (*Passage, error) {
	b, err := f()
	if err != nil {
		return nil, err
	}
	return (*Passage)(b.(*passage)), nil
}


type userMini base.UserMini
func (u *userMini) GetKey() asynchronoussIOAndBuffer.Key {
	return userMiniKey(u.UserId)
}

type userMiniKey int64
func (k userMiniKey) Uid() int64 {
	return int64(k)
}
func (userMiniKey) TypeId() uint8 {
	return dataTypeUserMini
}
func (k userMiniKey) GetKey() asynchronoussIOAndBuffer.Key {
	return k
}

type userMiniDataSource struct {}
func (userMiniDataSource) Load(key asynchronoussIOAndBuffer.Key) (asynchronoussIOAndBuffer.Bean, error) {
	u, err := base.GetOneUserMini(key.Uid())
	return (*userMini)(u), err
}
func (userMiniDataSource) Save(bean asynchronoussIOAndBuffer.Bean) error {

	return nil
}
func (userMiniDataSource) Delete(key asynchronoussIOAndBuffer.Key) error {
	return nil
}

func LoadUserMini(id int64) GetFunction {
	return GetFunction(asynchronoussIOAndBuffer.DataManager.Load(userMiniKey(id)))
}
func LoadUserMinis(ids []int64, fs []GetFunction) []GetFunction {
	for _, id := range ids {
		fs = append(fs, asynchronoussIOAndBuffer.DataManager.Load(userMiniKey(id)))
	}
	return fs
}
func GetUserMiniFromFunction(f GetFunction) (*base.UserMini, error) {
	b, err := f()
	if err != nil {
		return nil, err
	}
	return (*base.UserMini)(b.(*userMini)), nil
}
func GetUserMinisFromFunctions(fs []GetFunction) ([]*base.UserMini, error) {
	goal := make([]*base.UserMini, len(fs))
	for i, f := range fs {
		b, err := f()
		if err != nil {
			return nil, err
		} else {
			goal[i] = (*base.UserMini)(b.(*userMini))
		}
	}
	return goal, nil
}
func LoadUserMiniNow(id int64) (*base.UserMini, error) {
	b, err := asynchronoussIOAndBuffer.DataManager.Load(userMiniKey(id))()
	if err != nil {
		return nil, err
	}
	return (*base.UserMini)(b.(*userMini)), err
}


func DataManagerInit() {
	asynchronoussIOAndBuffer.Init(map[uint8]asynchronoussIOAndBuffer.DataSource{
		dataTypeUserMini: userMiniDataSource{},
		dataTypePassage: passageDataSource{},
		dataTypeMediaPiece: mediaPieceDataSource{},
		dataTypeImageData: imageDataSource{},
	}, true)
}

