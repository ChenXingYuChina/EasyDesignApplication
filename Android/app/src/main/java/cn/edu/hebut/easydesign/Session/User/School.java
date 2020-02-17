package cn.edu.hebut.easydesign.Session.User;

import org.json.JSONObject;

import java.io.Serializable;

public class School implements Serializable {
    public boolean publicSchool;
    public String country, name;
    public int diploma;
    public School(boolean publicSchool, String country, String name, int diploma) {
        this.publicSchool = publicSchool;
        this.country = country;
        this.name = name;
        this.diploma = diploma;
    }
    public School(JSONObject school) throws Exception {
        publicSchool = school.getBoolean("public");
        diploma = school.getInt("diploma");
        country = school.getString("country");
        name = school.getString("name");
    }
    public JSONObject toJson() throws Exception {
        JSONObject goal = new JSONObject();
        goal.put("public", publicSchool);
        goal.put("diploma", diploma);
        goal.put("country", country);
        goal.put("name", name);
        return goal;
    }

    private static final String[] diplomaNames = new String[]{"小学", "初中", "高中", "大学", "研究生", "博士"};

    public String getDiploma() {
        return diplomaNames[diploma];
    }
}
