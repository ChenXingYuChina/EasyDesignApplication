package user

import (
	"crypto/md5"
	"encoding/hex"
	"strings"
	"time"
)

type Password string

func (p *Password) CheckRight() bool {
	m := int(time.Now().Unix() / (60))
	bs := make([]byte, 16)
	l, err := hex.Decode(bs, []byte(*p))
	if err != nil || l != 16{
		return false
	}
	b := byte(0)
	goal := make([]byte, 16)
	for i := 0; i < 16; i++ {
		goal[i] = bs[i] ^ byte(m + i)
		b ^= goal[i]
	}
	if b != 0 {
		m--
		b = 0
		for i := 1; i < 16; i++ {
			goal[i] = bs[i] ^ byte(m + i)
			b ^= goal[i]
		}
		if b != 0 {
			return false
		}
	}
	mid := md5.Sum(goal)
	*p = Password(hex.EncodeToString(mid[:]))
	return true
}

func (p Password) checkEqual(p2 Password) bool {
	return strings.EqualFold(string(p), string(p2))
}

func GenPasswordInBack(pw string) string {
	mid := md5.Sum([]byte(pw))
	mid[15] = 0
	for i := 0; i < 15; i++ {
		mid[15] ^= mid[i]
	}
	mid = md5.Sum(mid[:])
	return hex.EncodeToString(mid[:])
}


func MakePasswordInFront(password string) string {
	mid := md5.Sum([]byte(password))
	mid[15] = 0
	for i := 0; i < 15; i++ {
		mid[15] ^= mid[i]
	}
	m := time.Now().Unix() / (60)
	for i := int64(0); i < 16; i++ {
		mid[i] = 0xff & (mid[i] ^ byte(m + i))
	}
	return string(hex.EncodeToString(mid[:]))
}