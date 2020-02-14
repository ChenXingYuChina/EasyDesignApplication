package cn.edu.hebut.easydesign.Session;

import org.json.JSONObject;

public class Public extends Identity {
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
}
