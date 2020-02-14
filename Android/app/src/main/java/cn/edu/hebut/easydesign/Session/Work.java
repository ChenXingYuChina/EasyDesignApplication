package cn.edu.hebut.easydesign.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.hebut.easydesign.DataManagement.Data;

public class Work {
    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-ddT");
    private static final String dateTail = "T15:04:05Z07:00";
    public Date start, end;
    public String company, industry, position;

    public Work(Date start, Date end, String company, String industry, String position) {
        this.start = start;
        this.end = end;
        this.company = company;
        this.industry = industry;
        this.position = position;
    }

    public Work(JSONObject work) throws Exception {
        start = format.parse(work.getString("start").substring(0, 10));
        end = format.parse(work.getString("end").substring(0, 10));
        company = work.getString("company");
        industry = work.getString("industry");
        position = work.getString("position");
    }
    public JSONObject toJson() throws Exception {
        JSONObject goal = new JSONObject();
        goal.put("start", format.format(start) + dateTail);
        goal.put("end", format.format(end) + dateTail);
        goal.put("company", company);
        goal.put("industry", industry);
        goal.put("position", position);
        return goal;
    }
}
