package cn.edu.hebut.easydesign.DataManagement;

import java.io.Serializable;

public interface Data extends Serializable {
    public long GetId();
    public DataType GetType();
}
