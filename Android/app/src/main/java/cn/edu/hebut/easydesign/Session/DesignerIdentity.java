package cn.edu.hebut.easydesign.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DesignerIdentity extends Identity {
    public List<Work> works;
    public DesignerIdentity() {
        works = new LinkedList<>();
    }

    public DesignerIdentity(JSONObject identity) throws Exception {
        JSONArray array =identity.getJSONArray("works");
        works = new ArrayList<>(identity.length());
        for (int i = 0; i < identity.length(); i++) {
            works.add(new Work(array.getJSONObject(i)));
        }
    }
    @Override
    public JSONObject toJson() throws Exception {
        JSONObject goal = new JSONObject();
        JSONArray array = new JSONArray();
        for (int i =0; i < works.size();i++) {
            array.put(works.get(i).toJson());
        }
        return goal.put("works", array);
    }
}
