package asynchronoussIOAndBuffer

import (
	"sync"
	"time"
)

type DataSource interface {
	Load(key Key) (Bean, error)
	Save(bean Bean) error
	Delete(key Key) error
}

type Bean interface {
	GetKey() Key
}

type Key interface {
	Uid() int64
	TypeId() uint8
	Bean
}

type Bucket interface {
	save(k int64, bean Bean)
	load(k int64) (c chan Bean, bean Bean)
	delete(k int64, key Key)
	clean(sources map[uint8]DataSource, clear bool)
	loadCallback(bean Bean)
	loadFailCallback(key Key, err error)
}

type DataMachine interface {
	Load(key Key) func() (Bean, error)
	Save(bean Bean)
	Delete(key Key)
	start()
}

type Error struct {
	E error
}

func (Error) GetKey() Key {
	panic("implement me")
}

type LoadFail struct {}

func (LoadFail) Error() string {
	return "LOAD FAIL"
}

func (LoadFail) GetKey() Key {
	panic("implement me")
}

var DataManager DataMachine

/*
called
 */
func Init(source map[uint8]DataSource, start bool) {
	rPool := new(resultPool)
	rPool.New = func() interface{} {
		return &result{}
	}
	rp = rPool
	table := &DataTable{resultPool:rPool, dataSource:source, cPool:new(channelPool)}
	iPool := new(itemPool)
	iPool.New = func() interface{} {
		return &item{}
	}
	table.cPool.New = func() interface{} {
		return make(chan Bean, 1)
	}
	for i := 0; i < 256; i++ {
		table.buckets[i] = &bucket{bucket:make(map[int64]*item), channelPool:table.cPool, itemPool:iPool, lock:new(sync.RWMutex)}
	}

	DataManager = table
	if start {
		go func() {
			<-time.Tick(5 * time.Minute)
			table.start()
		}()
	}
}
