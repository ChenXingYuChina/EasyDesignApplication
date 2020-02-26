package cn.edu.hebut.easydesign.Resources.PassageList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.LikeAble;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.LongField;
import cn.edu.hebut.easydesign.Session.NeedSessionTask.NoReplySessionHostPostTask;
import cn.edu.hebut.easydesign.Session.Session;

public class PassageListItem implements Serializable, LikeAble {
    // 哪个工作室，作者，文章编号，列表图片
    public long workshopId, owner, id, listImage;
    // 点赞数，评论数
    public int like, commentNumber;
    // 标题
    public String title;
    // 文章类型 暂时无意义
    public short type;

    private boolean liked = false;

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

    @Override
    public boolean isLiked() {
        return liked;
    }

    @Override
    public void setLiked() {
        liked = true;
    }

    @Override
    public int likeNumber() {
        return like;
    }


    public class likePassage extends NoReplySessionHostPostTask {

        public likePassage() {
            super("likePassage");
        }

        @Override
        protected int makeForm(Form form, Session session) {
            form.addFields(new LongField("pid", id));
            return 0;
        }
    }
}



