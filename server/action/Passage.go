package action

import "EasyDesignApplication/server/middle"

type FullPassage struct {
	*middle.FullPassage
	Full bool `json:"full"`
}
