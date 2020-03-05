package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.UserSchoolHelper;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.User.School;

public class UserSchoolCard extends FrameLayout {
    UserSchoolHelper helper;

    public UserSchoolCard(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.user_descritpion_student_school_layout, this);
        helper = new UserSchoolHelper(this);
    }

    public void setSchool(School school) {
        helper.putData(school);
    }
}
