package cn.edu.hebut.easydesign.Activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.LoginTask;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class LoginPage extends HoldContextAppCompatActivity {
    private Button login;
    private ImageView signUp;
    private EditText account, password;
    private TextView forgetPassword, visitor;
    private ServiceConnection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        signUp = findViewById(R.id.sign_up);
        forgetPassword = findViewById(R.id.foget_password);
        visitor = findViewById(R.id.visitor);
        login = findViewById(R.id.denglu);
//        signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginPage.this,login3.class);
//                startActivity(intent);
//            }
//        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = findViewById(R.id.name);
                password = findViewById(R.id.email);
                final String accountString = account.getText().toString();
                final String passwordString = password.getText().toString();
                connection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        TaskService.MyBinder binder = (TaskService.MyBinder) service;
                        try {
                            binder.PutTask(new LoginTask(accountString, passwordString) {
                                @Override
                                protected void doOnMainNormal() {
                                    startActivity(new Intent(LoginPage.this, MainActivity.class));
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
        unbindService(connection);
    }
}
