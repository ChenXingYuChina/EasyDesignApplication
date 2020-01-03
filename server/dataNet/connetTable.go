package dataNet

import (
	"encoding/binary"
	"log"
	"net"
	"sync"
)

type table struct {
	t [8]map[int64]*connOrWait
	locks [8]*sync.Mutex
}

type connOrWait struct {
	conn *Conn
	c chan struct{}
}

func (t *table) addListen(id int64) *connOrWait {
	p := id & 7
	t.locks[p].Lock()
	goal := &connOrWait{nil, make(chan struct{})}
	t.t[p][id] = goal
	t.locks[p].Unlock()
	return goal
}

func (t *table) cancelListen(id int64) *Conn {
	p := id & 7
	t.locks[p].Lock()
	if t.t[p][id].conn != nil {
		c := t.t[p][id]
		delete(t.t[p], id)
		t.locks[p].Unlock()
		close(c.c)
		return c.conn
	}
	t.locks[p].Unlock()
	return nil
}

func (t *table) giveConn(c net.Conn) {
	var id int64
	err := binary.Read(c, binary.LittleEndian, &id)
	if err != nil {
		err = c.Close()
		if err != nil {
			log.Println(err)
		}
		return
	}
	//fmt.Println(id)
	p := id & 7
	t.locks[p].Lock()
	m := t.t[p]
	if cw, ok := m[id]; ok {
		cw.conn = &Conn{c}
		cw.c <- struct{}{}
		close(cw.c)
		delete(t.t[p], id)
		//fmt.Println("connect")
		err = binary.Write(c, binary.LittleEndian, byte('s'))
		//fmt.Println("connect")
	} else {
		err = binary.Write(c, binary.LittleEndian, byte('e'))
		if err != nil {
			return
		}
		err = c.Close()
		if err != nil {
			log.Println(err)
		}
	}
	t.locks[p].Unlock()
}

func (t *table) listen(l net.Listener) {
	con, err := l.Accept()
	if err != nil {
		log.Println(err)
	}
	go t.giveConn(con)
}