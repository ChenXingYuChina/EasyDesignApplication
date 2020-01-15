package dataNet

import (
	"context"
	"encoding/binary"
	"fmt"
	"net"
	"sync"
)

var connectTable *table

type Conn struct {
	c net.Conn
}

func (c *Conn) LoadData() ([]byte, error) {
	length := int32(0)
	err := binary.Read(c.c, binary.LittleEndian, &length)
	fmt.Println(length)
	if err != nil {
		return nil, err
	}
	goal := make([]byte, length)
	_, err = c.c.Read(goal)
	if err != nil {
		fmt.Println(err)
		_, _ = c.c.Write([]byte{'e'})
		return nil, err
	}
	return goal, nil
}

func (c *Conn) CanFinish() error {
	err := binary.Write(c.c, binary.LittleEndian, 'f')
	if err != nil {
		return err
	}
	return nil
}


func (c *Conn) Finish() error {
	return c.c.Close()
}

func Accept(context context.Context, id int64) *Conn {
	cw := connectTable.addListen(id)
	select {
	case <-cw.c:
		return cw.conn
	case <-context.Done():
		return connectTable.cancelListen(id)
	}
}

func Start(listenAddress string) {
	connectTable = &table{}
	for i := 0; i < 8; i++ {
		connectTable.t[i] = make(map[int64]*connOrWait)
		connectTable.locks[i] = new(sync.Mutex)
	}
	l, err := net.Listen("tcp4", listenAddress)
	if err != nil {
		panic(err)
	}
	go func(l net.Listener) {
		for {
			connectTable.listen(l)
		}
	}(l)
}


