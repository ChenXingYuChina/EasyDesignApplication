package cn.edu.hebut.easydesign.Activity.ActivityTask;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.Task;

public abstract class DelayJumpTask extends Task<Condition<Integer>, Object> {
    public DelayJumpTask(long delay, Condition<Integer> condition) {
        this.delay = delay;
        this.data1 = condition;
    }

    @Override
    protected boolean doOnService() {
        Log.i("ED", "h");
        return true;
    }

}
