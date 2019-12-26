package asynchronoussIOAndBuffer

import (
	"os"
	"sync"
	"time"
)

const (
	load   uint8 = 1
	deleteItem uint8 = 1 << iota
	toSave
	keeping
	loadFail
	remove
	nothing
)

var keepingTime int64 = int64(10 * time.Minute)


type bucket struct {
	bucket map[int64]*item
	channelPool *channelPool
	itemPool *itemPool
	lock *sync.RWMutex
}

func (b *bucket) save(k int64, bean Bean) {
	b.lock.Lock()
	i := b.bucket[k]
	if i == nil {
		newItem := b.itemPool.get()
		newItem.state = toSave
		newItem.bean = bean
		newItem.lastActiveTime = time.Now().Unix()
		newItem.waits = make([]chan Bean, 0, 3)
		b.bucket[k] = newItem
	} else {
		switch i.state {
		case load:
			for _, v := range i.waits {
				v <- bean
			}
			i.waits = i.waits[:0]
			i.bean = bean
			i.state = toSave
		case toSave:
			i.bean = bean
		case keeping, loadFail:
			i.bean = bean
			i.state = toSave
		}
		if i.state != loadFail {
			i.lastActiveTime = time.Now().Unix()
		}
	}
	b.lock.Unlock()
	return
}

func (b *bucket) load(k int64) (c chan Bean, bean Bean) {
	b.lock.RLock()
	i := b.bucket[k]
	if i != nil {
		if i.state != loadFail {
			i.lastActiveTime = time.Now().Unix()
		}
		switch i.state {
		case toSave, keeping:
			goal := i.bean
			i.lastActiveTime = time.Now().Unix()
			b.lock.RUnlock()
			return nil, goal
		case loadFail:
			b.lock.RUnlock()
			return nil, LoadFail{}
		case deleteItem:
			b.lock.RUnlock()
			return nil, Error{os.ErrNotExist}
		case load:
			b.lock.RUnlock()
			b.lock.Lock()
			switch i.state {
			case load:
				c := b.channelPool.get()
				i.waits = append(i.waits, c)
				b.lock.Unlock()
				return c, nil
			case toSave, keeping:
				goal := i.bean
				i.lastActiveTime = time.Now().Unix()
				b.lock.Unlock()
				return nil, goal
			case loadFail:
				b.lock.Unlock()
				return nil, LoadFail{}
			case deleteItem:
				b.lock.Unlock()
				return nil, Error{os.ErrNotExist}
			}
		}
		return nil, nil
	} else {
		b.lock.RUnlock()
		b.lock.Lock()
		i = b.bucket[k]
		if i != nil {
			switch i.state {
			case toSave, keeping:
				goal := i.bean
				i.lastActiveTime = time.Now().Unix()
				b.lock.Unlock()
				return nil, goal
			case loadFail:
				b.lock.Unlock()
				return nil, LoadFail{}
			case deleteItem:
				b.lock.Unlock()
				return nil, Error{os.ErrNotExist}
			case load:
				c := b.channelPool.get()
				i.waits = append(i.waits, c)
				b.lock.Unlock()
				return c, nil
			}
			return nil, nil
		} else {
			newItem := b.itemPool.get()
			newItem.state = load
			newItem.bean = bean
			newItem.lastActiveTime = time.Now().Unix()
			newItem.waits = make([]chan Bean, 1, 3)
			c := b.channelPool.get()
			newItem.waits[0] = c
			b.bucket[k] = newItem
			b.lock.Unlock()
			return c, nil
		}
	}
}

func (b *bucket) delete(k int64, key Key) {
	b.lock.Lock()
	i := b.bucket[k]
	if i != nil {
		if i.state != loadFail {
			i.lastActiveTime = time.Now().Unix()
		}
		switch i.state {
		case load:
			for _, v := range i.waits {
				v <- Error{os.ErrNotExist}
			}
			i.bean = key
			i.waits = i.waits[:0]
			i.state = deleteItem
		case keeping, toSave:
			i.state = deleteItem
		case deleteItem, loadFail:
			i.bean = key
		}
	} else {
		newItem := b.itemPool.get()
		newItem.state = deleteItem
		newItem.bean = key
		newItem.lastActiveTime = time.Now().Unix()
		newItem.waits = make([]chan Bean, 0, 3)
		b.bucket[k] = newItem
	}
	b.lock.Unlock()
}

type cmd struct {
	bean Bean
	do   uint8
}

func (b *bucket) clean(dataSources map[uint8]DataSource, clear bool) {
	b.lock.RLock()
	notActiveTime := time.Now().Unix() - keepingTime
	NeedToHandle := make([]cmd, 0, len(b.bucket))
	if clear {
		for _, i := range b.bucket {
			if i.lastActiveTime < notActiveTime {
				switch i.state {
				case deleteItem:
					if i.bean != nil {
						NeedToHandle = append(NeedToHandle, cmd{i.bean, deleteItem})
					} else {
						NeedToHandle = append(NeedToHandle, cmd{nil, remove})
					}
				case keeping:
					NeedToHandle = append(NeedToHandle, cmd{i.bean, remove})
				case toSave:
					NeedToHandle = append(NeedToHandle, cmd{i.bean, toSave})
				case loadFail:
					NeedToHandle = append(NeedToHandle, cmd{i.bean, loadFail})
				}
			} else {
				switch i.state {
				case keeping:
					NeedToHandle = append(NeedToHandle, cmd{i.bean, remove})
				}
			}
		}
	} else {
		for _, i := range b.bucket {
			if i.lastActiveTime < notActiveTime {
				switch i.state {
				case deleteItem:
					NeedToHandle = append(NeedToHandle, cmd{i.bean, deleteItem})
				case keeping:
					NeedToHandle = append(NeedToHandle, cmd{i.bean, remove})
				case toSave:
					NeedToHandle = append(NeedToHandle, cmd{i.bean, toSave})
				case loadFail:
					NeedToHandle = append(NeedToHandle, cmd{i.bean, loadFail})
				}
			}
		}
	}
	b.lock.RUnlock()
	counter := int64(0)
	c := make(chan int64, len(NeedToHandle))
	for _, cmd := range NeedToHandle {
		switch cmd.do {
		case deleteItem:
			go deleteBean(cmd.bean.GetKey(), dataSources[cmd.bean.GetKey().TypeId()], c, counter)
			counter++
		case toSave:
			go saveBean(cmd.bean, dataSources[cmd.bean.GetKey().TypeId()], c, counter)
			counter++
		}
	}
	for i := int64(0); i < counter; i++ {
		p := <-c
		if p < 0 {
			NeedToHandle[-p].do = nothing
		}
	}
	b.lock.Lock()
	for _, cmd := range NeedToHandle {
		key := cmd.bean.GetKey()
		k := key.Uid() & -16 | int64(key.TypeId())
		i := b.bucket[k]
		if i.lastActiveTime < notActiveTime {
			switch cmd.do {
			case nothing:
			case remove, loadFail, toSave:
				delete(b.bucket, k)
				b.itemPool.put(i)
			}
		} else {
			switch cmd.do {
			case nothing:
			case deleteItem:
				i.bean = nil
				i.state = deleteItem
			case toSave:
				if i.bean == cmd.bean && i.state == toSave {
					i.state = keeping
				}
			}
		}
	}
	b.lock.Unlock()
}

func saveBean(bean Bean, source DataSource, c chan int64, position int64) {
	if source.Save(bean) != nil {
		c <- -position
	}
	c <- position
}

func deleteBean(key Key, source DataSource, c chan int64, position int64) {
	if source.Delete(key) != nil {
		c<- -position
	}
	c<-position
}

func (b *bucket) loadCallback(bean Bean) {
	b.lock.Lock()
	key := bean.GetKey()
	k := key.Uid() & -16 | int64(key.TypeId())
	i := b.bucket[k]
	switch i.state {
	case load:
		i.state = keeping
		i.bean = bean
		for _, v := range i.waits {
			v <- bean
		}
	case loadFail:
		i.lastActiveTime = time.Now().Unix()
		i.state = keeping
		i.bean = bean
	}
	b.lock.Unlock()
}

func (b *bucket) loadFailCallback(key Key, err error) {
	b.lock.Lock()
	k := key.Uid() & -16 | int64(key.TypeId())
	i := b.bucket[k]
	if i.state == load {
		i.state = loadFail
		for _, v := range i.waits {
			v <- Error{err}
		}
	}
	b.lock.Unlock()
}

type item struct {
	bean Bean
	state uint8
	lastActiveTime int64
	waits []chan Bean
}

