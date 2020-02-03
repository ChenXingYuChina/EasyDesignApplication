package middle

import "testing"

func TestLoadUserMini(t *testing.T) {
	g := LoadUserMini(47)
	u, err := GetUserMiniFromFunction(g)
	if err != nil {
		t.Error(err)
		return
	}
	t.Log(u)
}

func TestLoadPassageNow(t *testing.T) {
	p, err := loadPassageNow(6)
	if err != nil {
		t.Error(err)
		return
	}
	t.Log(p.Body)
}
