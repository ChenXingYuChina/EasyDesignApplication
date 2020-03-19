package ComplexString

import (
	"encoding/binary"
	"encoding/json"
	"io"
	"io/ioutil"
	"os"
)

type resources struct {
	Res  []int64 `json:"res"`
	Urls []string `json:"urls"`
}
//
//func (r *resources) UnmarshalJSON(d []byte) error {
//	var rr resources
//	err := json.Unmarshal(d, &rr)
//	*r = rr
//	return err
//}

func (r *resources) MarshalJSON() ([]byte, error) {
	buffer := make([]Resource, 0, len(r.Res))
	c := 0
	for _, v := range r.Res {
		if v == TypeUrl {
			buffer = append(buffer, HyperLink(r.Urls[c]))
			c++
		} else {
			buffer = append(buffer, LoadResourcesExceptUrl(v))
		}
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

	defer func() {
		err = f.Close()
	}()
	if err != nil {
		return err
	}
	err = s.SaveComplexString(f)
	if err != nil {
		return err
	}
	return nil
}

func (s *ComplexString) SaveComplexString(w io.Writer) (err error) {
	help := int64(len(s.Content)) | (int64(len(s.Positions)) << 32)
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
	err = binary.Write(w, binary.LittleEndian, s.ResourcesId.Res)
	if err != nil {
		return err
	}
	err = binary.Write(w, binary.LittleEndian, uint32(len(s.ResourcesId.Urls)))
	if err != nil {
		return err
	}
	return SaveStringsToFile(w, s.ResourcesId.Urls)
}

func LoadComplexStringFromFile(fileName string) (goal *ComplexString, err error) {
	f, err := os.Open(fileName)
	if err != nil {
		return
	}
	defer func() {
		err = f.Close()
	}()
	goal, err = LoadComplexString(f)
	if err != nil {
		return nil, err
	}
	return goal, nil
}

func LoadComplexString(r io.Reader) (goal *ComplexString, err error) {
	var help int64
	err = binary.Read(r, binary.LittleEndian, &help)
	if err != nil {
		return nil, err
	}
	lLow := int(int32(help))
	lHigh := int(int32(help >> 32))
	goal = &ComplexString{}
	buffer := make([]byte, lLow)
	goal.Positions = make([]int32, lHigh)
	goal.Widths = make([]int32, lHigh)
	goal.ResourcesId = resources{Res: make([]int64, lHigh)}
	_, err = r.Read(buffer)
	if err != nil {
		return nil, err
	}
	goal.Content = string(buffer)
	err = binary.Read(r, binary.LittleEndian, goal.Positions)
	if err != nil {
		return nil, err
	}
	err = binary.Read(r, binary.LittleEndian, goal.Widths)
	if err != nil {
		return nil, err
	}
	err = binary.Read(r, binary.LittleEndian, goal.ResourcesId.Res)
	if err != nil {
		return nil, err
	}
	var l uint32
	err = binary.Read(r, binary.LittleEndian, &l)
	if err != nil {
		return nil, err
	}
	goal.ResourcesId.Urls, err = LoadStringsFromFile(r, l)
	return goal, err
}

func LoadStringsFromFile(r io.Reader, length uint32) ([]string, error) {
	buffer, err := ioutil.ReadAll(r)
	if err != nil {
		return nil, err
	}
	var left = 0
	goal := make([]string, 0, length)
	for i, v := range buffer {
		if v == 0 {
			goal = append(goal, string(buffer[left:i]))
			left = i+1
		}
	}
	goal = append(goal, string(buffer[left:]))
	return goal, nil
}

func SaveStringsToFile(w io.Writer, data []string) error {
	if data == nil || len(data) == 0 {
		return nil
	}
	_, err := w.Write([]byte(data[0]))
	if err != nil {
		return err
	}
	help := []byte{0}
	for _, v := range data[1:] {
		_, err = w.Write(help)
		if err != nil {
			return err
		}
		_, err = w.Write([]byte(v))
		if err != nil {
			return err
		}
	}
	return nil
}
