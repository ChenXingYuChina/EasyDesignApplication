package cn.edu.hebut.easydesign.Resources.Passage;

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
    public boolean liked = false;

    public SubComment(JSONObject subComment) throws Exception {
        this.content = subComment.getString("content");
        this.passage = subComment.getLong("passage");
        this.owner = subComment.getLong("owner");
        this.like = subComment.getInt("like");
        this.father = subComment.getInt("father");
        this.position = (short) subComment.getInt("position");
    }
    public String getContent(){return this.content;}
    public int getLikeNumber(){return this.like;}

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

    public class likeSubComment extends NoReplySessionHostPostTask {
        private long passageId;
        private int father;
        private short position;

        public likeSubComment() {
            super("likeSubComment");
            this.passageId = passage;
            this.father = SubComment.this.father;
            this.position = SubComment.this.position;
            liked = true;
        }

        @Override
        protected int makeForm(Form form, Session session) {
            form.addFields(new LongField("pid", passageId)).addFields(new IntField("father", father)).addFields(new ShortField("position", position));
            return 0;
        }
    }

}
