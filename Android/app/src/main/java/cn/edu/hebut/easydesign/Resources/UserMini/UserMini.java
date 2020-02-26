package cn.edu.hebut.easydesign.Resources.UserMini;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import cn.edu.hebut.easydesign.DataManager.Data;
import cn.edu.hebut.easydesign.DataManager.DataType;

public class UserMini implements Data {
    public long headImage;
    public long id;
    public String name;
    public byte identity;  // 何种身份，学生0，设计师1，公众2

    public static UserMini parseJson(JSONObject userMini) throws JSONException {
        UserMini goal = new UserMini();
        goal.id = userMini.getLong("id");
        goal.name = userMini.getString("name");
        goal.headImage = userMini.getLong("head_image");
        goal.identity = (byte) userMini.getInt("identity");
        return goal;
    }

    @Override
    public long GetId() {
        return id;
    }

    @Override
    public DataType GetType() {
        return DataType.UserMini;
    }

    @Override
    public void cache(FileOutputStream stream) throws Exception {
        new ObjectOutputStream(stream).writeObject(this);
    }
}
