package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.User.Work;

public class UserWorkCard extends FrameLayout {
    private TextView company, industry, position, time;
    private static final DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    private static Date untilNow;

    static {
        try {
            untilNow = format.parse("0001/01/01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public UserWorkCard(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.user_description_designer_work_layout, this);
        company = findViewById(R.id.company);
        industry = findViewById(R.id.industry);
        position = findViewById(R.id.position);
        time = findViewById(R.id.work_time);
    }

    public void setWork(Work work) {
        company.setText(work.company);
        industry.setText(work.industry);
        position.setText(work.position);
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
