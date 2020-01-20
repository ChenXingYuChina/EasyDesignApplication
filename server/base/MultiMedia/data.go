package MultiMedia

import (
	"log"
	"os"
)

type data struct {
	Id   int64
	Data []byte
}

func loadData(name string, id int64) (*data, error) {
	f, err := os.Open(name)
	if err != nil {
		return nil, err
	}
	stat, err := f.Stat()
	if err != nil {
		return nil, err
	}
	goal := &data{Id:id, Data:make([]byte, stat.Size())}
	_, err = f.Read(goal.Data)
	if err != nil {
		return nil, err
	}
	err = f.Close()
	if err != nil {
		log.Println(err)
	}
	return goal, nil
}

func saveData(name string, d []byte) error {
	f, err := os.Create(name)
	if err != nil {
		return err
	}
	_, err = f.Write(d)
	if err != nil {
		return err
	}
	return f.Close()
}
