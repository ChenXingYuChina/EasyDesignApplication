package asynchronoussIOAndBuffer

import (
	"errors"
	"sync"
)

type itemPool sync.Pool

func (p *itemPool) get() *item {
	return (*sync.Pool)(p).Get().(*item)
}

func (p *itemPool) put(i *item) {
	(*sync.Pool)(p).Put(i)
}

type channelPool sync.Pool

func (p *channelPool) get() chan Bean {
	return (*sync.Pool)(p).Get().(chan Bean)
}

func (p *channelPool) put(c chan Bean) {
	(*sync.Pool)(p).Put(c)
}

type resultPool sync.Pool

func (p *resultPool) get() *result {
	return (*sync.Pool)(p).Get().(*result)
}

func (p *resultPool) put(r *result) {
	(*sync.Pool)(p).Put(r)
}

var LoadFailError = errors.New("LOAD FAIL")
