package cn.edu.hebut.easydesign.Activity.UserInformation.Editor;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.UserSchoolHelper;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.User.School;

public class SchoolEditor extends FrameLayout {
    private School school = new School();
    private UserSchoolHelper<Spinner, Spinner, Spinner, Chip> helper;
    private TextView order;
    private View delete;
    private TwoSpinnerHelper twoSpinnerHelper;

    public SchoolEditor(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.user_student_school_edit_layout, this);
        helper = new UserSchoolHelper<>(this);
        order = findViewById(R.id.order);
        delete = findViewById(R.id.delete_school);
        twoSpinnerHelper = new TwoSpinnerHelper(helper.getCountry(), UserSchoolHelper.countryNames, helper.getName(), UserSchoolHelper.schoolNames);
    }

    public void setData(School school, int order, int countryPosition, int namePosition) {
        helper.putData(school);
        twoSpinnerHelper.setSelection(countryPosition, namePosition);
        this.order.setText(order + "");
    }

    public School getSchool() {
        school.publicSchool = helper.getPublicSchool().isChecked();
        school.country = twoSpinnerHelper.getSelectedFatherString();
        school.name = twoSpinnerHelper.getSelectedChildString();
        school.diploma = helper.getDiploma().getSelectedItemPosition();
        return school;
    }

    public int getSelectedCountryPosition() {
        return twoSpinnerHelper.getSelectedFather();
    }

    public int getSelectedNamePosition() {
        return twoSpinnerHelper.getSelectedChild();
    }

    public int getOrder() {
        return Integer.valueOf(order.getText().toString());
    }

    public View getDelete() {
        return delete;
    }
}
