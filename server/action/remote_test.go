package action

import (
	"testing"
)

func TestSignUp(t *testing.T) {
	signUpStudentInCmd("a@b.com", "hello world")
}