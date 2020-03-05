package cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper;

import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.User.Work;

public class UserWorkHelper {
    private TextView company, industry, position, time;
    private static final DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    private static Date untilNow;
    private ViewGroup cachedView;

    static {
        try {
            untilNow = format.parse("0001/01/01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public UserWorkHelper(ViewGroup parent) {
        initView(parent);
    }

    public UserWorkHelper() {
        company = industry = position = time = null;
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
            industry.setText(work.industry);
        }
        if (position != null) {
            position.setText(work.position);
        }
        if (time != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(format.format(work.start));
            builder.append(" - ");
            if (work.end.equals(untilNow)) {
                builder.append("至今");
            } else {
                builder.append(format.format(work.end));
            }
            time.setText(builder.toString());
        }
    }

    public void setData(Work work) {
        setData(cachedView, work);
    }
}
