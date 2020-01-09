package cn.edu.hebut.easydesign.Resources.Media.MultiMedia;

import android.util.JsonReader;

import org.json.JSONObject;

public class MultiMedia {
    public long id;
    public long length;
    public byte type;

    public MultiMedia(JSONObject object) throws Exception {
        id = object.getLong("id");
        length = object.getLong("length");
        type = (byte) object.getLong("type");
    }

    public JSONObject toJson() throws Exception {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("length", length);
        object.put("type", type);
        return object;
    }
}
