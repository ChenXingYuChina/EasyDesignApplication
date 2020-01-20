package comment

import (
	"EasyDesignApplication/server/base"
	"testing"
)

func TestMain(m *testing.M) {
	base.DataDir = "../../"
	PrepareCommentDir()
	PrepareCommentSQL()
}

func TestMakeCommentTo(t *testing.T) {
	//MakeCommentTo(2,)
}

func TestLoadCommentBase(t *testing.T) {
	//LoadCommentBase()
}
