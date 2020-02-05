package cn.edu.hebut.easydesign.Resources.PassageList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PassageListItem implements Serializable {
    // 哪个工作室，作者，文章编号，列表图片
    public long workshopId, owner, id, listImage;
    // 点赞数，评论数
    public int like, commentNumber;
    // 标题
    public String title;
    // 文章类型 暂时无意义
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
