package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.R;

public class UserTop extends FrameLayout {

    public UserTop(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.home_top_layout, this);

    }

    public void setUser() {

    }
}
