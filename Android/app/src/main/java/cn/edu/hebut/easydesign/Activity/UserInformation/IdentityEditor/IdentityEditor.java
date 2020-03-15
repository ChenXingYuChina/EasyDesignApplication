package cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.UserInformation.InformationEditor.InformationEditor;
import cn.edu.hebut.easydesign.Activity.UserInformation.UserInformation;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.FormFieldView;
import cn.edu.hebut.easydesign.Session.User.Identity;

public abstract class IdentityEditor<T extends Identity> extends FrameLayout implements FormFieldView, InformationEditor {
    protected static final String fieldName = "identity";
    public IdentityEditor(@NonNull Context context) {
        super(context);
    }

    public IdentityEditor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void setDate(T data);

    @Override
    public String getApiUrl() {
        return "updateIdentity";
    }
}
