package cn.edu.hebut.easydesign.Activity.ContextHelp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class HoldContextAppCompatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextHolder.setContext(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContextHolder.setContext(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        ContextHolder.setContext(null);
    }
}
