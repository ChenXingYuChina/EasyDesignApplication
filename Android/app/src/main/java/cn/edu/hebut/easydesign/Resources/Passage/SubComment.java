package cn.edu.hebut.easydesign.Resources.Passage;

import android.util.Log;

import org.json.JSONObject;

import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.LikeAble;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.IntField;
import cn.edu.hebut.easydesign.HttpClient.Form.LongField;
import cn.edu.hebut.easydesign.HttpClient.Form.ShortField;
import cn.edu.hebut.easydesign.Session.NeedSessionTask.NoReplySessionHostPostTask;
import cn.edu.hebut.easydesign.Session.Session;

public class SubComment implements LikeAble {
    public String content;
    public long passage, owner;
    public int like, father;
    public short position;
    private boolean liked = false;

    public SubComment(JSONObject subComment) throws Exception {
        this.content = subComment.getString("content");
        this.passage = subComment.getLong("passage");
        this.owner = subComment.getLong("owner");
        this.like = subComment.getInt("like");
        this.father = subComment.getInt("father");
        this.position = (short) subComment.getInt("position");
    }

    @Override
    public boolean isLiked() {
        Log.i("onLike", "isLiked: " + this + liked);
        return liked;
    }

    @Override
    public void setLiked() {
        Log.i("onLike", "setLiked: " + this + liked);
        liked = true;
    }

    @Override
    public int likeNumber() {
        return like;
    }

    public class likeSubComment extends NoReplySessionHostPostTask {
        private long passageId;
        private int father;
        private short position;

        public likeSubComment() {
            super("likeSubComment");
            this.passageId = passage;
            this.father = SubComment.this.father;
            this.position = SubComment.this.position;
        }

        @Override
        protected int makeForm(Form form, Session session) {
            form.addFields(new LongField("pid", passageId)).addFields(new IntField("father", father)).addFields(new ShortField("position", position));
            return 0;
        }
    }

}
