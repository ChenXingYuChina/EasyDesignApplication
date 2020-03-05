package cn.edu.hebut.easydesign.Activity.UserInformation.Editor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.UserInformation.CachedIdentity.StudentCached;
import cn.edu.hebut.easydesign.Activity.UserInformation.UserInformation;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.User.School;

public class StudentEditor extends IdentityEditor<StudentCached> {
    private ViewGroup viewGroup;

    public StudentEditor(@NonNull final Context context) {
        super(context);
        inflate(context, R.layout.user_student_editor_layout, this);
        viewGroup = findViewById(R.id.school_list);
        View add = findViewById(R.id.new_school);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addVoidSchool(context);
            }
        });
    }

    @Override
    protected void setData(StudentCached student) {
        if (student == null) {
            return;
        }
        viewGroup.removeAllViews();
        for (int i = 0; i < student.cachedOrder.size(); i++) {
            School s = student.student.schools.get(i);
            SchoolEditor editor = addVoidSchool(ContextHolder.getContext());
            editor.setData(s, student.cachedOrder.get(i), student.cachedCountryPosition.get(i), student.cachedNamePosition.get(i));
        }
    }

    private SchoolEditor addVoidSchool(Context context) {
        final SchoolEditor editor = new SchoolEditor(context);
        editor.getDelete().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewGroup.removeView(editor);
            }
        });
        viewGroup.addView(editor);
        return editor;
    }

    @Override
    protected void setToCache(UserInformation userInformation) {
        int schoolCount = viewGroup.getChildCount();
        StudentCached studentCached = new StudentCached();
        for (int i = 0; i < schoolCount; i++) {
            SchoolEditor editor = (SchoolEditor) viewGroup.getChildAt(i);
            studentCached.addToCached(
                    editor.getSchool(),
                    editor.getOrder(),
                    editor.getSelectedCountryPosition(),
                    editor.getSelectedNamePosition()
            );
        }
        userInformation.setStudentCached(studentCached);
    }
}
