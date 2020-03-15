package cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor;

import android.content.Context;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.TwoSpinnerHelper;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.UserSchoolHelper;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.User.School;
import cn.edu.hebut.easydesign.Session.User.UserStringResources;

public class SchoolEditor extends SubEditor<School> {
    private UserSchoolHelper<Spinner, Spinner, Spinner, Chip> helper;
    private TextView order;
    private View delete;
    private TwoSpinnerHelper twoSpinnerHelper;
    private static int[] schoolNameIds = new int[]{R.array.chinese_school, R.array.nz_school};

    public SchoolEditor(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.user_student_school_edit_layout, this);
        helper = new UserSchoolHelper<>(this);
        order = findViewById(R.id.order);
        delete = findViewById(R.id.delete_school);
        twoSpinnerHelper = new TwoSpinnerHelper(helper.getCountry(), UserStringResources.getCountryNames(), helper.getName(), UserStringResources.getSchoolNames());
    }

    public School getSchool() {
        School school = new School();
        school.publicSchool = helper.getPublicSchool().isChecked();
        school.country = twoSpinnerHelper.getSelectedFather();
        school.name = twoSpinnerHelper.getSelectedChild();
        school.diploma = helper.getDiploma().getSelectedItemPosition();
        return school;
    }

    public int getOrder() {
        return Integer.valueOf(order.getText().toString());
    }

    public View getDelete() {
        return delete;
    }

    @Override
    public void setData(School data) {
        helper.putData(data);
        order.setText("0");
    }
}
