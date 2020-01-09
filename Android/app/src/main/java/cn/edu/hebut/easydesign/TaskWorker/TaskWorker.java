package cn.edu.hebut.easydesign.TaskWorker;

import android.os.Handler;
import android.os.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;

import androidx.annotation.NonNull;


public class TaskWorker implements Runnable {
    private LinkedBlockingDeque<Task> in = new LinkedBlockingDeque<>();
    private Handler handler = new Handler();
    private volatile boolean c = true;
    @Override
    public void run() {
        while (c) {
            Task task = in.poll();
            if (task != null) {
                if (task.doOnService()) {
                    handler.post(task);
                }
            }
        }
    }
    public void fin() {
        c = false;
        putTask(new Task() {
            // a empty task let the worker stop
            @Override
            protected void doOnMain() {

            }

            @Override
            protected boolean doOnService() {
                return true;
            }
        });
    }
    public void putTask(Task task) {
        in.add(task);
    }
}
