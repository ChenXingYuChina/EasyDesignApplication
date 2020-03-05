package action

import (
	"testing"
)

func TestSignUpRemote(t *testing.T) {
	signUpStudentInCmd("a@b.com", "hello world")
}