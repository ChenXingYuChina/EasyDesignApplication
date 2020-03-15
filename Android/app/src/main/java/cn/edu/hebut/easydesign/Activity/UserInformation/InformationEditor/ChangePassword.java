package cn.edu.hebut.easydesign.Activity.UserInformation.InformationEditor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.PasswordField;
import cn.edu.hebut.easydesign.R;

public class ChangePassword extends FrameLayout implements InformationEditor, SpecialErrorHandler {
    private EditText oldPassword, newPassword, newPasswordCopy;
    private TextView tip;
    public ChangePassword(@NonNull Context context) {
        super(context);
        initView(context);
    }
    public ChangePassword(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.user_change_password, this);
        oldPassword = findViewById(R.id.old_password);
        newPassword = findViewById(R.id.new_password);
        newPasswordCopy = findViewById(R.id.new_password_copy);
        tip = findViewById(R.id.tip);

    }

    @Override
    public String getApiUrl() {
        return "changePassword";
    }

    @Override
    public void collectData(List<FormField> goal) throws Exception {
        String old = oldPassword.getText().toString(),
                newOne = newPassword.getText().toString(),
                newCopy = newPasswordCopy.getText().toString();
        if (newOne.length() == 0 || old.length() == 0 || newCopy.length() == 0) {
            tip.setText("输入不得为空");
            throw new Exception();
        }
        if (!newOne.equals(newCopy)) {
            tip.setText("确认密码有误");
            throw new Exception();
        }
        goal.add(new PasswordField("old", old));
        goal.add(new PasswordField("new", newOne));
    }

    @Override
    public String HandleError(int code) {
        if (code == 401) {
            return "密码错误请重试";
        }
        return null;
    }

    @Override
    public String HandleError(Exception e) {
        return null;
    }
}
