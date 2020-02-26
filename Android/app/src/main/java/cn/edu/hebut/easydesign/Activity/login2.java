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

import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.LoginTask;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class login2 extends HoldContextAppCompatActivity {
    private Button login;
    private ImageView next;
    private EditText account;
    private EditText password;
    ServiceConnection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        next = findViewById(R.id.next);
        login = findViewById(R.id.denglu);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login2.this,login3.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = findViewById(R.id.zhanghao);
                password = findViewById(R.id.mima);
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
                                    startActivity(new Intent(login2.this, MainActivity.class));
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
                bindService(new Intent(login2.this, TaskService.class), connection, Service.BIND_AUTO_CREATE);
                System.out.println(accountString);
                System.out.println(passwordString);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(connection);
    }
}
