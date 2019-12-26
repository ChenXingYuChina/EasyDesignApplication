package asynchronoussIOAndBuffer

import (
	"fmt"
	"os"
	"testing"
	"time"
)

type testDataResource struct {
	data []byte
}

func (d *testDataResource) Load(key Key) (Bean, error) {
	if key.TypeId() == 0 {
		return &testBean{d.data[key.Uid()], byte(key.Uid())}, nil
	} else if key.TypeId() == 1 {
		return &testBean2{d.data[key.Uid()], byte(key.Uid())}, nil
	}
	return nil, nil
}

func (d *testDataResource) Save(bean Bean) error {
	switch b := bean.(type) {
	case *testBean:
		d.data[bean.GetKey().Uid()] = b.d
	case *testBean2:
		d.data[bean.GetKey().Uid()] = b.d
	}
	return nil
}

func (d *testDataResource) Delete(key Key) error {
	d.data[key.Uid()] = 0
	return nil
}

type testBean struct {
	d byte
	l byte
}

func (b *testBean) GetKey() Key {
	k := testKey(b.l)
	return &k
}

type testBean2 struct {
	d byte
	l byte
}

func (b *testBean2) GetKey() Key {
	k := testKey2(b.l)
	return &k
}

type testKey uint8

func (k *testKey) GetKey() Key {
	return k
}

func (k *testKey) Uid() int64 {
	return int64(*k)
}

func (*testKey) TypeId() uint8 {
	return 0
}

type testKey2 uint8

func (k *testKey2) Uid() int64 {
	return int64(*k)
}

func (k *testKey2) TypeId() uint8 {
	return 1
}

func (k *testKey2) GetKey() Key {
	return k
}

func TestMain(m *testing.M) {
	Init(map[uint8]DataSource{0:&testDataResource{[]byte{1, 2, 3, 4, 5, 6, 7, 255:0}}}, false)
	keepingTime = -1
	m.Run()
}

func TestLoad(t *testing.T) {
	k := testKey(0)
	f := DataManager.Load(&k)
	b, err := f()
	if err != nil {
		t.Fatal(err)
	}
	if (b.(*testBean)).d != 1 {
		t.Fatal(b)
	}
}

func TestSave(t *testing.T) {
	DataManager.Save(&testBean{d:10, l:3})
	table := DataManager.(*DataTable)
	table.buckets[3].clean(table.dataSource, false)
	v := table.dataSource[0].(*testDataResource).data[3]
	if v != 10 {
		t.Fatal(v)
	}
}

func TestDelete(t *testing.T) {
	k := testKey(0)
	DataManager.Delete(&k)

	table := DataManager.(*DataTable)
	table.buckets[0].clean(table.dataSource, false)

	v := table.dataSource[0].(*testDataResource).data[0]
	if v != 0 {
		t.Fatal(v)
	}
}

func TestMultiDataSource(t *testing.T) {
	D := DataManager
	Init(map[uint8]DataSource{0:&testDataResource{[]byte{1, 2, 3, 4, 5, 6, 7, 255:0}}, 1:&testDataResource{[]byte{1, 2, 3, 4, 5, 6, 7, 255:0}}}, false)
	DataManager.Save(&testBean2{2, 0})
	DataManager.Save(&testBean{3, 1})
	table := DataManager.(*DataTable)
	table.buckets[1].clean(table.dataSource, false)
	v := table.dataSource[0].(*testDataResource).data[1]
	if v != 3 {
		t.Fatal(v)
	}
	v = table.dataSource[1].(*testDataResource).data[0]
	if v != 2 {
		t.Fatal(v)
	}
	DataManager = D
}

func TestDeleteThenLoad(t *testing.T) {
	k := testKey(0)
	DataManager.Delete(&k)
	f := DataManager.Load(&k)
	b, err := f()
	if !os.IsNotExist(err) || b != nil{
		t.Fatal(err, b)
	}
}

func TestDeleteThenSave(t *testing.T) {
	k := testKey(0)
	go DataManager.Delete(&k)
	go DataManager.Save(&testBean{10, 0})
	<-time.Tick(20 * time.Millisecond)
	table := DataManager.(*DataTable)
	table.buckets[0].clean(table.dataSource, false)
	v := table.dataSource[0].(*testDataResource).data[0]
	if v != 0 {
		t.Fatal(v)
	}
}

func TestDeleteAndDelete(t *testing.T) {
	k := testKey(0)
	go DataManager.Delete(&k)
	go DataManager.Delete(&k)
	<-time.Tick(20 * time.Millisecond)
	table := DataManager.(*DataTable)
	table.buckets[0].clean(table.dataSource, false)
	v := table.dataSource[0].(*testDataResource).data[0]
	if v != 0 {
		t.Fatal(v)
	}
}

func TestLoadWithLoad(t *testing.T) {
	start1 := time.Now()
	k := testKey(0)
	var f1 func() (Bean, error)
	var f2 func() (Bean, error)
	//go func() {f1 = DataManager.Load(&k)}()
	//go func() {f2 = DataManager.Load(&k)}()
	f1 = DataManager.Load(&k)
	f2 = DataManager.Load(&k)

	//<-time.Tick(1000 *time.Millisecond)
	b, err := f2()
	if bean, is := b.(*testBean); is && err == nil {
		if bean.d != 1 {
			t.Fatal(b)
		}
	} else {
		t.Fatal(b, err)
	}
	b, err = f1()
	if bean, is := b.(*testBean); is && err == nil {
		if bean.d != 1 {
			t.Fatal(b)
		}
	} else {
		t.Fatal(b, err)
	}
	<-time.Tick(2*time.Millisecond)
	fmt.Println(time.Now().UnixNano() - start1.UnixNano() - int64(2 * time.Millisecond))
}

func TestLoadThenSave(t *testing.T) {
	k := testKey(0)
	f := DataManager.Load(&k)
	DataManager.Save(&testBean{10, 0})
	<-time.Tick(1*time.Millisecond)
	b, err := f()
	if err != nil {
		t.Fatal(err)
	}
	if bean, is := b.(*testBean); is {
		if bean.d != 10 {
			t.Fatal(b)
		}
	} else {
		t.Fatal(b)
	}
	table := DataManager.(*DataTable)
	table.buckets[0].clean(table.dataSource, false)
	v := table.dataSource[0].(*testDataResource).data[0]
	if v != 10 {
		t.Fatal(v)
	}
}

func TestLoadAndDelete(t *testing.T) {
	k := testKey(0)
	f := DataManager.Load(&k)
	DataManager.Delete(&k)
	b, err := f()
	if !os.IsNotExist(err) {
		t.Fatal(b, err)
	}
	table := DataManager.(*DataTable)
	table.buckets[0].clean(table.dataSource, false)
	v := table.dataSource[0].(*testDataResource).data[0]
	if v != 0 {
		t.Fatal(v)
	}
}

func TestSaveAndLoad(t *testing.T) {
	DataManager.Save(&testBean{10, 0})
	k := testKey(0)
	f := DataManager.Load(&k)
	b, err := f()
	if err != nil {
		t.Fatal(err)
	}
	if bean, is := b.(*testBean); is {
		if bean.d != 10 {
			t.Fatal(b)
		}
	} else {
		t.Fatal(b, err)
	}
	table := DataManager.(*DataTable)
	table.buckets[0].clean(table.dataSource, false)
	v := table.dataSource[0].(*testDataResource).data[0]
	if v != 10 {
		t.Fatal(v)
	}
}

func TestSaveThenSave(t *testing.T) {
	bean := &testBean{10, 0}
	DataManager.Save(bean)
	bean.d = 11
	DataManager.Save(bean)
	table := DataManager.(*DataTable)
	table.buckets[0].clean(table.dataSource, false)
	v := table.dataSource[0].(*testDataResource).data[0]
	if v != 11 {
		t.Fatal(v)
	}
}

func TestSaveThenDelete(t *testing.T) {
	k := testKey(0)
	DataManager.Save(&testBean{10, 0})
	DataManager.Delete(&k)

	table := DataManager.(*DataTable)
	table.buckets[0].clean(table.dataSource, false)
	v := table.dataSource[0].(*testDataResource).data[0]
	if v != 0 {
		t.Fatal(v)
	}
}
