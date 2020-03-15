package cn.edu.hebut.easydesign.Session.User;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.UserWorkHelper;

public class Work implements Serializable, Comparable<Work> {
    public static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public Calendar start, end;
    public String company;
    public int industry, position;

    public Work(Calendar start, Calendar end, String company, int industry, int position) {
        this.start = start;
        this.end = end;
        this.company = company;
        this.industry = industry;
        this.position = position;
    }

    public Work(JSONObject work) throws Exception {
        start = Calendar.getInstance();
        start.setTime(format.parse(work.getString("start")));
        String endString = work.getString("end");
        if (endString.equals("0001-01-01")) {
            end = (Calendar) UserWorkHelper.untilNow.clone();
        } else {
            end = Calendar.getInstance();
            end.setTime(format.parse(endString));
        }
        Log.i("time", "Work: " + end);
        company = work.getString("company");
        industry = work.getInt("industry");
        position = work.getInt("position");
    }
    public JSONObject toJson() throws Exception {
        JSONObject goal = new JSONObject();
        goal.put("start", format.format(start.getTime()));
        goal.put("end", format.format(end.getTime()));
        goal.put("company", company);
        goal.put("industry", industry);
        goal.put("position", position);
        return goal;
    }

    @Override
    public int compareTo(Work o) {
        return start.after(o.start) ? 1: -1;
    }

    @Override
    public int hashCode() {
        return (int) (start.getTimeInMillis() * 11 + end.getTimeInMillis() * 13 + company.hashCode() * 17 + industry * 19 + position * 23);
    }
}
