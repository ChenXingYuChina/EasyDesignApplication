package cn.edu.hebut.easydesign.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Student extends Identity {
    public List<School> schools;

    public Student(JSONObject identity) throws Exception {
        JSONArray array = identity.getJSONArray("school");
        schools = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            schools.add(new School(array.getJSONObject(i)));
        }
    }

    public Student() {
        schools = new LinkedList<>();
    }

    @Override
    public JSONObject toJson() throws Exception {
        JSONObject goal = new JSONObject();
        JSONArray array = new JSONArray();
        for (int i = 0; i < schools.size(); i++) {
            array.put(schools.get(i).toJson());
        }
        return goal;
    }
}
