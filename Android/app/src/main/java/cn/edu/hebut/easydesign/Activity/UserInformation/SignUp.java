package cn.edu.hebut.easydesign.Activity.UserInformation;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.Activity.LoginPage;
import cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor.DesignerEditor;
import cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor.IdentityEditor;
import cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor.PublicEditor;
import cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor.StudentEditor;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.IntField;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class SignUp extends HoldContextAppCompatActivity implements Spinner.OnItemSelectedListener, View.OnClickListener, SignUpBase.ToTop {
    private ServiceConnection connection;
    private FrameLayout identityEditorPosition;
    private SignUpBase signUpBase;
    private ScrollView signUpMain;
    private View signUp, signUpLabel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_editor_layout);
        identityEditorPosition = findViewById(R.id.sign_up_detail);
        signUpBase = findViewById(R.id.sign_up_base);
        signUp = findViewById(R.id.sign_up);
        signUpLabel = findViewById(R.id.sign_up_label);
        signUpMain = findViewById(R.id.sign_up_main);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (TaskService.MyBinder) service;
                ContextHolder.setBinder(binder);
                signUpBase.setOnItemSelectedListener(SignUp.this);
                signUp.setOnClickListener(SignUp.this);
                signUpLabel.setOnClickListener(SignUp.this);
                signUpBase.setToTop(SignUp.this);
                editors[0] = new StudentEditor(SignUp.this);
                identityEditorPosition.addView(editors[0]);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(new Intent(this, TaskService.class), connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    private IdentityEditor[] editors = new IdentityEditor[3];
    private int identityType = 0;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (editors[position] == null) {
            switch (position) {
                case 0:
                    editors[position] = new StudentEditor(this);
                    break;
                case 1:
                    editors[position] = new DesignerEditor(this);
                    break;
                case 2:
                    editors[position] = new PublicEditor(this);
                    break;
            }
        }
        identityEditorPosition.removeAllViews();
        identityEditorPosition.addView(editors[position]);
        identityType = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private List<FormField> collectData() throws Exception {
        List<FormField> goal = new LinkedList<>();
        signUpBase.collectData(goal);
        editors[identityType].collectData(goal);
        goal.add(new IntField("identityType", identityType));
        return goal;
    }

    @Override
    public void onClick(View v) {
        try {
            binder.PutTask(new SignUpTask(collectData()) {
                @Override
                protected void onSuccess(String message) {
                    Toast.makeText(SignUp.this, message, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUp.this, LoginPage.class));
                    finish();
                }

                @Override
                protected void onError(int error) {
                    signUpBase.handleError("注册失败错误代码：" + error + " 请您检查网络后重试。");
                }

                @Override
                protected void onSignUpFail(String message) {
                    signUpBase.handleError(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "未知异常请重试", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void toTop() {
        signUpMain.scrollTo(0, 0);
    }
}
