package cn.edu.hebut.easydesign.Resources.Passage;

import org.json.JSONObject;

import cn.edu.hebut.easydesign.ComplexString.ComplexString;

public class SubComment {
    public String content;
    public long passage, owner;
    public int like, father;
    public short position;

    public boolean Like() {
        // todo finish it
        return true;
    }

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


}
