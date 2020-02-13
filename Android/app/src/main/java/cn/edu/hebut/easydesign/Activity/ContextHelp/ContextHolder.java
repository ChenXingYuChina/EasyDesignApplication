package cn.edu.hebut.easydesign.Activity.ContextHelp;

import android.content.Context;
import android.util.Log;

import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class ContextHolder {
    private static final ContextHolder holder = new ContextHolder();
    private Context context;
    private TaskService.MyBinder binder;

    public static void setContext(Context context) {
        holder.context = context;
    }

    public static Context getContext() {
        return holder.context;
    }

    public static TaskService.MyBinder getBinder() {
        return holder.binder;
    }

    public static void setBinder(TaskService.MyBinder binder) {
        holder.binder = binder;
    }
}
