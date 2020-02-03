package cn.edu.hebut.easydesign.DataManagement;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public interface Data extends Serializable {
    public long GetId();
    public DataType GetType();
    public void cache(FileOutputStream stream) throws Exception;
}
