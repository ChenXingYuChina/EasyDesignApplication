package middle

import "errors"

var NoAccountError = errors.New("NO THIS ACCOUNT")
var UnauthorizedError = errors.New("THE ACCOUNT IS NOT AUTHORIZED")
