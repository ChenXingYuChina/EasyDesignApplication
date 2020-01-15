package cn.edu.hebut.easydesign.TaskWorker;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import androidx.annotation.NonNull;


public class TaskWorker implements Runnable {
    private LinkedBlockingQueue<Task> in = new LinkedBlockingQueue<>();
    private Handler handler = new Handler();
    private volatile boolean c = true;
    @Override
    public void run() {
        while (c) {
            Task task = in.poll();
            if (task != null) {
                if (task.doOnService()) {
                    if (task.delay != 0) {
                        handler.postDelayed(task, task.delay);
                        Log.i("ED", "send task");
                    } else {
                        handler.post(task);
                        Log.i("ED", "send task");
                    }
                }
            }
        }
    }
    void fin() {
        c = false;
        putTask(new Task() {
            // a empty task let the worker stop
            @Override
            protected void doOnMain() {
            }

            @Override
            protected boolean doOnService() {
                return false;
            }
        });
    }
    void putTask(Task task) {
        Log.i("ED", "putTask: " + task);
        in.add(task);
    }

    void clear(Object token) {
        handler.removeCallbacksAndMessages(token);
    }
}
