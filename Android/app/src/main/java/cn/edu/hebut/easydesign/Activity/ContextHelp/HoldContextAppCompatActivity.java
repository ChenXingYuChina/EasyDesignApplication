package cn.edu.hebut.easydesign.Activity.ContextHelp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public abstract class HoldContextAppCompatActivity extends AppCompatActivity {
    protected TaskService.MyBinder binder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextHolder.setContext(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContextHolder.setContext(this);
        ContextHolder.setBinder(binder);
    }


    @Override
    protected void onPause() {
        super.onPause();
        ContextHolder.setContext(null);
    }
}
