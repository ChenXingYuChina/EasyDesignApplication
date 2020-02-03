package cn.edu.hebut.easydesign.TaskWorker;

import android.os.Handler;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


public class TaskWorker implements Runnable {
    private LinkedBlockingQueue<Task> in = new LinkedBlockingQueue<>();
    private Handler handler = new Handler();
    private ExecutorService executor = Executors.newFixedThreadPool(2);
    private volatile boolean c = true;
    @Override
    public void run() {
        while (c) {
            Task task = in.poll();
            if (task != null) {
                if (task.async) {
                    executor.submit(new asyncRun(task));
                } else {
                    if (task.doOnService()) {
                        if (task.delay != 0) {
                            handler.postDelayed(task, task.delay);
                        } else {
                            handler.post(task);
                        }
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
        Log.i("ED", "putTask: " + task.getClass());
        in.offer(task);
    }

    void clear(Object token) {
        handler.removeCallbacksAndMessages(token);
    }

    private class asyncRun implements Runnable {
        /*package*/ Task task;
        /*package*/ asyncRun(Task task) {
            this.task = task;
        }
        @Override
        public void run() {
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
