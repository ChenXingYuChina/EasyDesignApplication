package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.UserWorkHelper;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.User.Work;

public class UserWorkCard extends FrameLayout {
    private UserWorkHelper helper;

    public UserWorkCard(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.user_description_designer_work_layout, this);
        helper = new UserWorkHelper(this);
    }

    public void setWork(Work work) {
        helper.setData(work);
    }
}
