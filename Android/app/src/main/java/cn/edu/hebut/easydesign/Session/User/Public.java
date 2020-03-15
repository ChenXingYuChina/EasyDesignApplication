package cn.edu.hebut.easydesign.Session.User;

import org.json.JSONObject;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class Public extends Identity implements Serializable {
    public int industry, position;
    public Public(int industry, int position) {
        this.industry = industry;
        this.position = position;
    }

    public Public(JSONObject identity) throws Exception {
        industry = identity.getInt("industry");
        position = identity.getInt("position");
    }
    @Override
    public JSONObject toJson() throws Exception {
        JSONObject goal = new JSONObject();
        goal.put("industry", industry);
        goal.put("position", position);
        return goal;
    }

    @NonNull
    @Override
    public String toString() {
        return UserStringResources.getIndustryNames()[industry] + "/" + UserStringResources.getPositionNames().get(industry)[position];
    }

    @Override
    public int getType() {
        return 2;
    }
}
