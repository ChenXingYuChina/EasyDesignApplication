package cn.edu.hebut.easydesign.Session.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;

public class Student extends Identity implements Serializable {
    public List<School> schools;

    public Student(JSONObject identity) throws Exception {
        JSONArray array = identity.getJSONArray("schools");
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
        return goal.put("schools", array);
    }

    public static final String[] diplomaNames = new String[]{"小学", "初中", "高中", "本科", "研究生", "博士"};

    @NonNull
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
        School c = schools.get(last);
        return UserStringResources.getSchoolNames().get(c.country)[c.name] + "/" + diplomaNames[lastDiploma];
    }

    @Override
    public int getType() {
        return 0;
    }
}
