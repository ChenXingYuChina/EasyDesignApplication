package cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.User.UserStringResources;
import cn.edu.hebut.easydesign.Session.User.Work;

public class UserWorkHelper<company extends TextView, industry extends View, position extends View> {
    private company company;
    private TextView time;
    private industry industry;
    private position position;
    private static final DateFormat format = Work.format;
    public static Calendar untilNow;
    private ViewGroup cachedView;

    static {
        untilNow = Calendar.getInstance();
        untilNow.set(1, 0, 1, 0, 0, 0);
    }

    public UserWorkHelper(ViewGroup parent) {
        initView(parent);
    }

    public UserWorkHelper() {
        company = null;
        industry = null;
        position = null;
        time = null;
    }

    private void initView(ViewGroup parent) {
        cachedView = parent;
        company = parent.findViewById(R.id.company);
        industry = parent.findViewById(R.id.industry);
        position = parent.findViewById(R.id.position);
        time = parent.findViewById(R.id.work_time);
    }

    public void setData(ViewGroup parent, Work work) {
        if (parent != cachedView) {
            initView(parent);
        }
        if (company != null) {
            company.setText(work.company);
        }
        if (industry != null) {
            if (industry instanceof TextView) {
                ((TextView) industry).setText(UserStringResources.getIndustryNames()[work.industry]);
            } else if (industry instanceof Spinner) {
                ((Spinner) industry).setSelection(work.industry);
            }
        }
        if (position != null) {
            if (position instanceof TextView) {
                ((TextView) position).setText(UserStringResources.getPositionNames().get(work.industry)[work.position]);
            } else if (position instanceof Spinner) {
                ((Spinner) position).setSelection(work.position);
            }
        }
        if (time != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(format.format(work.start.getTime()));
            builder.append(" 至 ");
            if (work.end.equals(untilNow)) {
                builder.append("至今");
            } else {
                builder.append(format.format(work.end.getTime()));
            }
            time.setText(builder.toString());
        }
    }

    public void setData(Work work) {
        setData(cachedView, work);
    }

    public static String makeDateString(Date date) {
        if (date.equals(untilNow)) {
            return "至今";
        } else {
            return format.format(date);
        }
    }

    public company getCompany() {
        return company;
    }

    public industry getIndustry() {
        return industry;
    }

    public position getPosition() {
        return position;
    }

    public Date parseDate(String date) throws ParseException {
        return format.parse(date);
    }
}
