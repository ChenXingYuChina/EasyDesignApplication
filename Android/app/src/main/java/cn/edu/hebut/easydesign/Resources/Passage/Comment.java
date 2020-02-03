package cn.edu.hebut.easydesign.Resources.Passage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.ComplexString.ComplexStringLoader;

public class Comment {
    public long passage, owner;
    public int like, position;
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

}
