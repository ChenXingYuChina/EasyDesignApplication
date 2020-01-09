package cn.edu.hebut.easydesign.Resources.UserMini;

import cn.edu.hebut.easydesign.DataManagement.Data;
import cn.edu.hebut.easydesign.DataManagement.DataType;

public class UserMini implements Data {
    public long headImage;
    public long id;
    public String name;
    public byte identity;

    @Override
    public long GetId() {
        return id;
    }

    @Override
    public DataType GetType() {
        return DataType.UserMini;
    }

    @Override
    public boolean onCache() {
        return true;
    }
}
