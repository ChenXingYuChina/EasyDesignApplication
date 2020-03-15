package cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import java.util.Calendar;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.TwoSpinnerHelper;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.UserWorkHelper;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.User.UserStringResources;
import cn.edu.hebut.easydesign.Session.User.Work;

public class WorkEditor extends SubEditor<Work> {
    private Calendar start = Calendar.getInstance(), end = Calendar.getInstance(), now = Calendar.getInstance();
    private Chip toNow;
    private View delete;
    private TextView startTime, endTime;
    private UserWorkHelper<EditText, Spinner, Spinner> helper;
    private TwoSpinnerHelper twoSpinnerHelper;
    private String nowString;
    private boolean setByAuto = false;

    public WorkEditor(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.user_designer_work_editor_layout, this);
        delete = findViewById(R.id.delete_work);
        toNow = findViewById(R.id.to_now);
        helper = new UserWorkHelper<>(this);
        twoSpinnerHelper = new TwoSpinnerHelper(helper.getIndustry(), UserStringResources.getIndustryNames(), helper.getPosition(), UserStringResources.getPositionNames());
        startTime = findViewById(R.id.start_time);
        endTime = findViewById(R.id.end_time);
        startTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ContextHolder.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        start.set(year, month, dayOfMonth);
                        startTime.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DATE)).show();
            }
        });
        endTime = findViewById(R.id.end_time);
        endTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ContextHolder.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        end.set(year, month, dayOfMonth);
                        endTime.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
//                        Log.i("dateSet", "onDateSet: " + endTime + endMonth + endDay);
                        toNow.setChecked(false);
                        setByAuto = true;
                    }
                }, end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DATE)).show();
            }
        });
        nowString = UserWorkHelper.makeDateString(now.getTime());
        startTime.setText(nowString);
        endTime.setText(nowString);
        toNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    endTime.setText("至今");
                    end = (Calendar) UserWorkHelper.untilNow.clone();
                } else if (!setByAuto) {
                    endTime.setText(nowString);
                    end = (Calendar) now.clone();
                }
                setByAuto = false;
            }
        });
    }

    public View getDelete() {
        return delete;
    }

    @Override
    public void setData(Work data) {
        helper.setData(data);
        end = data.end;
        start = data.start;
        if (end.equals(UserWorkHelper.untilNow)) {
            endTime.setText("至今");
            toNow.setChecked(true);
        } else {
            endTime.setText(UserWorkHelper.makeDateString(data.end.getTime()));
        }
        startTime.setText(UserWorkHelper.makeDateString(data.start.getTime()));
    }

    public Work getWork() {
        return new Work(
                start,
                end,
                helper.getCompany().getText().toString(),
                helper.getIndustry().getSelectedItemPosition(),
                helper.getPosition().getSelectedItemPosition()
        );
    }
}
