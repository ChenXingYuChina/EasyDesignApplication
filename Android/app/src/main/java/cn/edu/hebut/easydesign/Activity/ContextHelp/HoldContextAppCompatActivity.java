package cn.edu.hebut.easydesign.Activity.ContextHelp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public abstract class HoldContextAppCompatActivity extends AppCompatActivity {
    protected TaskService.MyBinder binder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextHolder.setContext(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("holder", "onResume: " + this);
        ContextHolder.setContext(this);
        ContextHolder.setBinder(binder);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("holder", "onRestart: ");
        ContextHolder.setContext(this);
        ContextHolder.setBinder(binder);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ContextHolder.setContext(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ContextHolder.setContext(this);
        ContextHolder.setBinder(binder);
    }
}
