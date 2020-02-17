package cn.edu.hebut.easydesign.Session.User;

import org.json.JSONObject;

import java.io.Serializable;

public class Public extends Identity implements Serializable {
    public String industry, position;
    public Public(String industry, String position) {
        this.industry = industry;
        this.position = position;
    }

    public Public(JSONObject identity) throws Exception {
        industry = identity.getString("industry");
        position = identity.getString("position");
    }
    @Override
    public JSONObject toJson() throws Exception {
        JSONObject goal = new JSONObject();
        goal.put("industry", industry);
        goal.put("position", position);
        return goal;
    }

    @Override
    public String toString() {
        return industry + "/" + position;
    }
}
