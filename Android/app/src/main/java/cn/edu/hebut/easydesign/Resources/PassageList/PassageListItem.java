package cn.edu.hebut.easydesign.Resources.PassageList;

import org.json.JSONException;
import org.json.JSONObject;

public class PassageListItem {
    public long workshopId, owner, id, listImage;
    public int like, commentNumber;
    public String title;
    public short type;

    public PassageListItem(JSONObject item) throws JSONException {
        workshopId = item.getLong("workshop");
        owner = item.getLong("owner");
        id = item.getLong("id");
        listImage = item.getLong("list_image");
        like = item.getInt("like");
        commentNumber = item.getInt("com");
        title = item.getString("title");
        type = (short) item.getInt("type");
    }
}
