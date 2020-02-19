package cn.edu.hebut.easydesign.Resources.Passage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.ComplexString.ComplexStringLoader;

public class Comment {
    public long passage, owner;
    public int like, position, subCommentNumber;
    public ArrayList<SubComment> subComments;
    public ComplexString content;

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

    public int getLikeNumber() {
        return this.like;
    }

    public int getSubComNumber() {
        return this.subCommentNumber;
    }
}
