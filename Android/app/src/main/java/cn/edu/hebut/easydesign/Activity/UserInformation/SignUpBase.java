package cn.edu.hebut.easydesign.Activity.UserInformation;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.Activity.LoginPage;
import cn.edu.hebut.easydesign.HttpClient.Form.EmailField;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.FormFieldView;
import cn.edu.hebut.easydesign.HttpClient.Form.PasswordField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;
import cn.edu.hebut.easydesign.Tools.ObjectHolder;
import okhttp3.FormBody;
import okhttp3.MultipartBody;

public class SignUpBase extends FrameLayout implements FormFieldView {
    private EditText userName, password, passwordCopy, email;
    private Spinner identity;
    private TextView errTip;

    public SignUpBase(@NonNull final Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.sign_up_base_layout, this);
        userName = findViewById(R.id.user_name_input);
        password = findViewById(R.id.password);
        passwordCopy = findViewById(R.id.password_copy);
        email = findViewById(R.id.email_input);
        identity = findViewById(R.id.identity_select);
        errTip = findViewById(R.id.err_tip);
    }

    void handleError(int error) {
        switch (error) {
            case 1:
                handleError("密码不一致");
                break;
            case 2:
                handleError("任何输入项不得为空");
                break;
            case 3:
                handleError("密码格式错误");
                break;
            case 4:
                handleError("邮箱格式错误");
        }
    }

    void handleError(String message) {
        toTop.toTop();
        errTip.setVisibility(View.VISIBLE);
        errTip.setText(message);
    }

    void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        identity.setOnItemSelectedListener(listener);
        identity.setSelection(0);
    }

    private ToTop toTop;

    public void setToTop(ToTop toTop) {
        this.toTop = toTop;
    }

    @Override
    public void collectData(List<FormField> goal) throws Exception {
        String pw = password.getText().toString();
        String emailInput = email.getText().toString();
        String nameInput = userName.getText().toString();
        if (pw.length() == 0 || emailInput.length() == 0 || nameInput.length() == 0) {
            handleError(2);
            throw new Exception();
        }
        if (!pw.equals(passwordCopy.getText().toString())) {
            handleError(1);
            throw new Exception();
        }
        try {
            goal.add(new EmailField("email", emailInput));
        } catch (Exception e) {
            e.printStackTrace();
            handleError(4);
            throw e;
        }
        goal.add(new TextField("name", nameInput));
        try {
            goal.add(new PasswordField("pw", pw));
        } catch (Exception e) {
            e.printStackTrace();
            handleError(e.getMessage());
            throw e;
        }
    }

    public interface ToTop {
        void toTop();
    }

}
