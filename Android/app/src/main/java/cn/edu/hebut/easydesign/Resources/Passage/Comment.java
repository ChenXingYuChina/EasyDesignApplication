package cn.edu.hebut.easydesign.Resources.Passage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.LikeAble;
import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.ComplexString.ComplexStringLoader;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.IntField;
import cn.edu.hebut.easydesign.HttpClient.Form.LongField;
import cn.edu.hebut.easydesign.Session.NeedSessionTask.NoReplySessionHostPostTask;
import cn.edu.hebut.easydesign.Session.Session;

public class Comment implements LikeAble {
    public long passage, owner;
    public int like, position, subCommentNumber;
    public ArrayList<SubComment> subComments;
    public ComplexString content;
    private boolean liked = false;

    public Comment(JSONObject comment, JSONArray subComments) throws Exception {
        int length = subComments.length();
        if (subComments.length() == 0) {
            this.subComments = null;
        } else {
            this.subComments = new ArrayList<>(length);
            for (int i = 0; i < subComments.length(); i++) {
                this.subComments.add(new SubComment(subComments.getJSONObject(i)));
            }
        }
        this.content = ComplexStringLoader.getInstance().LoadFromNet(comment.getJSONObject("content"));
        this.like = comment.getInt("like");
        this.position = comment.getInt("position");
        this.subCommentNumber = comment.getInt("sub_com_number");
        this.owner = comment.getLong("owner");
        this.passage = comment.getLong("passage");
    }

    public JSONObject toJson() throws Exception {
        JSONObject goal = new JSONObject();
        goal.put("content", content.toJson());
        goal.put("like", like);
        goal.put("owner", owner);
        goal.put("passage", passage);
        return goal;
    }

    public ArrayList<SubComment> getSubComments() {
        return subComments;
    }

    public ComplexString getContent() {
        return this.content;
    }

    public int getPosition() {
        return this.position;
    }

    public ArrayList<SubComment> getReplayList() {
        return this.subComments;
    }

    public void setReplyList(ArrayList<SubComment> replyList) {
        this.subComments = replyList;
    }

    public int getSubComNumber() {
        return this.subCommentNumber;
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

    public class likeComment extends NoReplySessionHostPostTask {
        private long passage;
        private int position;

        public likeComment() {
            super("likeComment");
            this.passage = Comment.this.passage;
            this.position = Comment.this.position;
        }

        @Override
        protected int makeForm(Form form, Session session) {
            form.addFields(new LongField("pid", passage)).addFields(new IntField("position", position));
            return 0;
        }
    }
}
