package action

import "EasyDesignApplication/server/middle"

type Passage struct {
	*middle.FullPassage
	Full bool `json:"full"`
}
