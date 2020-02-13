package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.PassageList.OnHeadBind;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageMultiListView;

public class UserTop extends FrameLayout implements OnHeadBind<PassageMultiListView> {
    public UserTop(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onHeadBind(PassageMultiListView container) {

    }
}
