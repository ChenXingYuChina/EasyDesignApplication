package cn.edu.hebut.easydesign.Activity.UserInformation;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.Activity.LoginPage;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;
import cn.edu.hebut.easydesign.Tools.ObjectHolder;

public class SignUpBase extends HoldContextAppCompatActivity implements View.OnClickListener {
    private EditText userName, password, passwordCopy, email;
    private volatile int identityType;
    private TextView toDetailLabel, errTip;
    private ImageView toDetail;
    private ServiceConnection connection;

    private UserInformation information;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_base_layout);
        userName = findViewById(R.id.user_name_input);
        password = findViewById(R.id.password);
        passwordCopy = findViewById(R.id.password_copy);
        email = findViewById(R.id.email_input);
        toDetail = findViewById(R.id.to_detail);
        Spinner identity = findViewById(R.id.identity_select);
        final TextView pass = findViewById(R.id.pass_sign_up_detail);
        toDetailLabel = findViewById(R.id.to_detail_label);
        errTip = findViewById(R.id.err_tip);
        if (ObjectHolder.getInstance().has("signUpDataBasic")) {
            information = ObjectHolder.getInstance().get("signUpDataBasic");
            userName.setText(information.name);
            identity.setSelection(information.identityType);
            email.setText(information.email);
        } else {
            information = new UserInformation();
        }
        identity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                identityType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                identityType = -1;
            }
        });
        toDetailLabel.setOnClickListener(this);
        toDetail.setOnClickListener(this);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                final TaskService.MyBinder binder = (TaskService.MyBinder) service;
                ContextHolder.setBinder(binder);
                pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pass.setClickable(false);
                        final int err = loadData();
                        if (err == 0) {
                            binder.PutTask(new SignUpQuickTask(information.getFormFields()) {
                                @Override
                                protected void onSuccess(String message) {
                                    Toast.makeText(ContextHolder.getContext(), message, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpBase.this, LoginPage.class));
                                    finish();
                                }

                                @Override
                                protected void onSignUpFail(String message) {
                                    pass.setClickable(true);
                                    handleError(message);
                                }

                            });
                            pass.setClickable(true);
                        } else {
                            handleError(err);
                            pass.setClickable(true);
                        }
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        bindService(new Intent(this, TaskService.class), connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        toDetail.setClickable(false);
        toDetailLabel.setClickable(false);
        int err = loadData();
        if (err == 0) {
            ObjectHolder.getInstance().put("signUpDataBasic", information);
            startActivity(new Intent(this, SignUpDetail.class));
            finish();
        } else {
            handleError(err);
        }
        toDetail.setClickable(true);
        toDetailLabel.setClickable(true);
    }



    private int loadData() {
        String password = this.password.getText().toString();
        String passwordCopy = this.passwordCopy.getText().toString();
        if (!passwordCopy.equals(password)) {
            return 1;
        }
        String name = userName.getText().toString();
        String email = this.email.getText().toString();
        if (name.length() == 0 || email.length() == 0 || identityType == -1) {
            return 2;
        }
        try {
            information.setPassword(password);
        } catch (Exception e) {
            e.printStackTrace();
            return 3;
        }
        try {
            information.setEmail(email);
        } catch (Exception e) {
            return 4;
        }
        information.setName(name);
        information.setIdentityType(identityType);
        return 0;
    }

    private void handleError(int error) {
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

    private void handleError(String message) {
        errTip.setVisibility(View.VISIBLE);
        errTip.setText(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
