package cn.edu.hebut.easydesign.Session.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Designer extends Identity implements Serializable {
    public List<Work> works;

    public Designer() {
        works = new LinkedList<>();
    }

    public Designer(JSONObject identity) throws Exception {
        JSONArray array = identity.getJSONArray("works");
        works = new ArrayList<>(identity.length());
        for (int i = 0; i < identity.length(); i++) {
            works.add(new Work(array.getJSONObject(i)));
        }
    }

    @Override
    public JSONObject toJson() throws Exception {
        JSONObject goal = new JSONObject();
        JSONArray array = new JSONArray();
        for (int i = 0; i < works.size(); i++) {
            array.put(works.get(i).toJson());
        }
        return goal.put("works", array);
    }

    @Override
    public String toString() {
        Work first = works.get(0);
        return first.industry + "/" + first.position;
    }
}
