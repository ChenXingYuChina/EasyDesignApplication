package cn.edu.hebut.easydesign.Activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.Activity.UserInformation.SignUp;
import cn.edu.hebut.easydesign.Activity.UserInformation.SignUpBase;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.LoginTask;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class LoginPage extends HoldContextAppCompatActivity implements View.OnClickListener {
    private Button login;
    private ImageView signUp;
    private EditText account, password;
    private TextView forgetPassword, visitor, signUpLabel;
    private ServiceConnection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        signUp = findViewById(R.id.sign_up);
        signUpLabel = findViewById(R.id.sign_up_label);
        forgetPassword = findViewById(R.id.foget_password);
        visitor = findViewById(R.id.visitor);
        login = findViewById(R.id.denglu);
        signUpLabel.setOnClickListener(this);
        signUp.setOnClickListener(this);
        account = findViewById(R.id.name);
        password = findViewById(R.id.email);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String accountString = account.getText().toString();
                final String passwordString = password.getText().toString();
                Log.i("login", "onClick: " + accountString + passwordString);
                connection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        TaskService.MyBinder binder = (TaskService.MyBinder) service;
                        ContextHolder.setBinder(binder);
                        try {
                            binder.PutTask(new LoginTask(accountString, passwordString) {

                                @Override
                                protected void loginSuccess() {
                                    startActivity(new Intent(LoginPage.this, MainActivity.class));
                                    finish();
                                }

                                @Override
                                protected void loginFail(int code) {
                                    String message;
                                    if (code > 700) {
                                        message = "请检查网络后重试";
                                    } else if (code == 401) {
                                        message = "账户或者密码错误";
                                    } else {
                                        message = "未知异常请联系客户";
                                    }
                                    Toast.makeText(LoginPage.this, message, Toast.LENGTH_LONG).show();

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                };
                bindService(new Intent(LoginPage.this, TaskService.class), connection, Service.BIND_AUTO_CREATE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (connection != null) {
            unbindService(connection);
        }
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, SignUp.class));
        finish();
    }
}
