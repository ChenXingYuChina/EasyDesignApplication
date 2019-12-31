package cn.edu.hebut.easydesign.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import cn.edu.hebut.easydesign.R;

public class login2 extends AppCompatActivity {
    private Button login;
    private ImageView next;
    private EditText account;
    private EditText password;
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
                String accountString = account.getText().toString();
                String passwordString = password.getText().toString();

                System.out.println(accountString);
                System.out.println(passwordString);
            }
        });
    }
}
