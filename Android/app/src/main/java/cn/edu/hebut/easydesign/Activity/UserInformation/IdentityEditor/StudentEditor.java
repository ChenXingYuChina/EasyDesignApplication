package cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.UserInformation.UserInformation;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.Session.User.School;
import cn.edu.hebut.easydesign.Session.User.Student;
import okhttp3.FormBody;
import okhttp3.MultipartBody;

public class StudentEditor extends MultiLevelEditor<Student, SchoolEditor> {
    public StudentEditor(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public StudentEditor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        if (add instanceof Button) {
            ((Button) add).setText("新建学习经历");
        } else if (add instanceof TextView) {
            ((TextView) add).setText("新建学习经历");
        }
    }

    public void collectData(List<FormField> goal) throws Exception {
        Set<studentHelper> helpers = new HashSet<>();
        int schoolCount = viewGroup.getChildCount();
        for (int i = 0; i < schoolCount; i++) {
            SchoolEditor editor = (SchoolEditor) viewGroup.getChildAt(i);
            helpers.add(new studentHelper(editor.getOrder(), editor.getSchool()));
        }
        List<studentHelper> helperList = new ArrayList<>(helpers);
        Collections.sort(helperList);
        Student student = new Student();
        for (studentHelper helper : helperList) {
            student.schools.add(helper.school);
        }
        goal.add(new IdentityField(fieldName, student));
    }

    @Override
    public void setDate(Student data) {
        for (School school:data.schools) {
            SchoolEditor editor = addVoidSubEditor(getContext());
            editor.setData(school);
        }
    }

    @Override
    public SchoolEditor createEditor(Context context) {
        return new SchoolEditor(context);
    }

    private static class studentHelper implements Comparable<studentHelper> {
        int order;
        School school;

        studentHelper(int order, School school) {
            this.order = order;
            this.school = school;
        }

        @Override
        public int compareTo(studentHelper o) {
            return order - o.order;
        }

        @Override
        public int hashCode() {
            return school.hashCode();
        }
    }
}
