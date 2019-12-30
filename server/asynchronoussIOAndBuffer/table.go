package asynchronoussIOAndBuffer

import (
	"github.com/shirou/gopsutil/mem"
	"log"
	"time"
)

var rp *resultPool

type DataTable struct {
	dataSource map[uint8]DataSource
	buckets [256]Bucket
	resultPool *resultPool
	cPool *channelPool
}

func (t *DataTable) Load(key Key) func() (Bean, error) {
	id := key.Uid()
	tId := key.TypeId()
	location := tId + uint8(id)
	c, bean := t.buckets[location].load(id & -16 | int64(tId))
	if c == nil {
		if _, is := bean.(LoadFail); is {
			return func() (bean Bean, e error) {
				return nil, LoadFail{}
			}
		}
		if err, is := bean.(Error); is {
			return func() (bean Bean, e error) {
				return nil, err.E
			}
		}
		return func() (b Bean, e error) {
			return bean, nil
		}
	} else {
		go loadWorker(key, t.buckets[location], t.dataSource[tId])
		r := t.resultPool.get()
		r.cPool = t.cPool
		r.c = c
		return r.get
	}
}

func (t *DataTable) Save(bean Bean) {
	key := bean.GetKey()
	id := key.Uid()
	tId := key.TypeId()
	t.buckets[tId + uint8(id)].save(id & -16 | int64(tId), bean)
}

func (t *DataTable) Delete(key Key) {
	id := key.Uid()
	tId := key.TypeId()
	t.buckets[tId + uint8(id)].delete(id & -16 | int64(tId), key)
}

func (t *DataTable) start() {
	go t.cleaner()
}

func (t *DataTable) cleaner() {
	var i uint8
	tick := time.NewTicker(4 * time.Second)
	tickFast := time.NewTicker(1 * time.Second)
	for {
		v, err := mem.VirtualMemory()
		if err != nil {
			log.Println(err)
		}
		if v.UsedPercent > 80 {
			re: t.buckets[i].clean(t.dataSource, true)
			v, err = mem.VirtualMemory()
			if err != nil {
				log.Println(err)
			}
			<-tickFast.C
			if v.UsedPercent > 75 {
				i++
				goto re
			}
		} else {
			t.buckets[i].clean(t.dataSource, false)
		}
		i++
		<-tick.C
	}
}

type result struct {
	c chan Bean
	cPool *channelPool
}

func (r *result) get() (Bean, error) {
	b := <-r.c
	r.cPool.put(r.c)
	rp.put(r)
	if e, is := b.(Error); is {
		return nil, e.E
	}
	return b, nil
}

func (r *result) error() error {
	b := <-r.c
	r.cPool.put(r.c)
	rp.put(r)
	if e, is := b.(Error); is {
		return e.E
	}
	return nil
}

func loadWorker(key Key, b Bucket, source DataSource) {
	bean, err := source.Load(key)
	if err != nil {
		b.loadFailCallback(key, err)
	} else {
		b.loadCallback(bean)
	}
}
