package base

import (
	"encoding/binary"
	"encoding/json"
	"io"
	"os"
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

func (s *ComplexString) SaveComplexStringToFile(fileName string) (err error) {
	f, err := os.Create(fileName)
	if err != nil {
		return err
	}
	err = s.SaveComplexString(f)
	if err != nil {
		return err
	}
	return f.Close()
}

func (s *ComplexString) SaveComplexString(w io.Writer) (err error) {
	help := int64(len(s.Content)) + (int64(len(s.Positions)) << 32)
	err = binary.Write(w, binary.LittleEndian, help)
	if err != nil {
		return err
	}
	_, err = w.Write([]byte(s.Content))
	if err != nil {
		return err
	}
	err = binary.Write(w, binary.LittleEndian, s.Positions)
	if err != nil {
		return err
	}
	err = binary.Write(w, binary.LittleEndian, s.Widths)
	if err != nil {
		return err
	}
	return binary.Write(w, binary.LittleEndian, s.ResourcesId.r)
}

func LoadComplexStringFromFile(fileName string) (goal *ComplexString, err error) {
	f, err := os.Open(fileName)
	if err != nil {
		return
	}
	goal, err = LoadComplexString(f)
	if err != nil {
		return nil, err
	}
	return goal, f.Close()
}

func LoadComplexString(r io.Reader) (goal *ComplexString, err error) {
	var help int64
	err = binary.Read(r, binary.LittleEndian, &help)
	if err != nil {
		return nil, err
	}
	lLow := int(int32(help))
	lHigh := int(int32(help >> 32))
	goal = &ComplexString{
		Content:string(make([]byte, lLow)),
		Positions:make([]int32, lHigh),
		Widths:make([]int32, lHigh),
		ResourcesId:resources{r:make([]int64, lHigh)},
	}
	_, err = r.Read([]byte(goal.Content))
	if err != nil {
		return nil, err
	}
	err = binary.Read(r, binary.LittleEndian, goal.Positions)
	if err != nil {
		return nil, err
	}
	err = binary.Read(r, binary.LittleEndian, goal.Widths)
	if err != nil {
		return nil, err
	}
	err = binary.Read(r, binary.LittleEndian, goal.ResourcesId.r)
	if err != nil {
		return nil, err
	}
	return goal, nil
}


