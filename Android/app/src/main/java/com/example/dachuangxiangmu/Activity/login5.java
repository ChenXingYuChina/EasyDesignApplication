package com.example.dachuangxiangmu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dachuangxiangmu.R;

public class login5 extends AppCompatActivity {
    private TextureView textureView;
    private ImageView next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login5);
        next = findViewById(R.id.next);
        textureView = (TextureView) findViewById(R.id.textureView);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login5.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
