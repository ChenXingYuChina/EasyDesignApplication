package base

import (
	"encoding/json"
)

type resources struct {
	r []int64
}

func (r *resources) MarshalJSON() ([]byte, error) {
	buffer := make([]Resource, 0, len(r.r))
	for _, v := range r.r {
		buffer = append(buffer, LoadTextResources(v))
	}
	return json.Marshal(buffer)
}

type ComplexString struct {
	Content     string    `json:"content"`
	Positions   []int32   `json:"position"`
	Widths      []int32   `json:"width"`
	ResourcesId resources `json:"resources"`
}

