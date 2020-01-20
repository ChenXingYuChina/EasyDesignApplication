package user

import (
	"crypto/md5"
	"encoding/hex"
	"fmt"
	"testing"
	"time"
)

func TestPassword_CheckRight(t *testing.T) {
	pw := Password(makePasswordInFront("hello world"))
	fmt.Println(string(pw))
	if !pw.CheckRight() {
		t.Fatal()
	}
}
var h = []byte{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	'a', 'b', 'c', 'd', 'e', 'f'}
func makePasswordInFront(password string) string {
	mid := md5.Sum([]byte(password))
	mid[15] = 0
	for i := 0; i < 15; i++ {
		mid[15] ^= mid[i]
	}
	fmt.Println(hex.EncodeToString(mid[:]))
	m := time.Now().Unix() / (60)
	goal := make([]byte, 32)
	for i := int64(0); i < 16; i++ {
		x := 0xff & (mid[i] ^ byte(m + i))
		goal[2*i + 1] = h[x & 0xf]
		goal[2*i] = h[(x >> 4) & 0xf]
	}
	return string(goal)
}
