package cn.edu.hebut.easydesign.Activity.ContextHelp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public abstract class HoldContextActivity extends Activity {
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
    }



    @Override
    protected void onPause() {
        super.onPause();
        ContextHolder.setContext(null);
    }
}
