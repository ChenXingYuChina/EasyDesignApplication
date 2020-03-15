package cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

public abstract class SubEditor<T> extends FrameLayout {
    public SubEditor(@NonNull Context context) {
        super(context);
    }
    public abstract View getDelete();
    public abstract void setData(T data);
}
