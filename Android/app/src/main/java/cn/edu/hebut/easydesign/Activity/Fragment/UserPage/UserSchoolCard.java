package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.User.School;

public class UserSchoolCard extends FrameLayout {
    TextView name, diploma, country;

    public UserSchoolCard(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.user_descritpion_student_school_layout, this);
        name = findViewById(R.id.school_name);
        diploma = findViewById(R.id.diploma);
        country = findViewById(R.id.country);
    }

    public void setSchool(School school) {
        name.setText(school.name);
        diploma.setText(school.getDiploma());
        country.setText(school.country);
    }
}
