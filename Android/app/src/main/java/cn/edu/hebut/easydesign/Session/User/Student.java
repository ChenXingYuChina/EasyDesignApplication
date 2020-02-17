package cn.edu.hebut.easydesign.Session.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Student extends Identity implements Serializable {
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

    public static final String[] diplomaNames = new String[]{"小学", "初中", "高中", "大学", "研究生", "博士"};

    @Override
    public String toString() {
        int last = -1;
        int lastDiploma = -1;
        for (int i = 0; i < schools.size(); i++) {
            School school = schools.get(i);
            if (school.publicSchool) {
                if (school.diploma > lastDiploma) {
                    last = i;
                    lastDiploma = school.diploma;
                }
            }
        }
        if (lastDiploma == -1) {
            return "未公开";
        }
        return schools.get(last).name + "/" + diplomaNames[lastDiploma];
    }
}
