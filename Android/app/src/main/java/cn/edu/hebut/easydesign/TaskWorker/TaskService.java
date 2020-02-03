package cn.edu.hebut.easydesign.TaskWorker;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import androidx.annotation.Nullable;

public class TaskService extends Service {
    private TaskWorker worker;
    private Thread childThread;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        worker = new TaskWorker();
        childThread = new Thread(worker);
        childThread.start();
    }

    public class MyBinder extends Binder {
        public void PutTask(Task task) {
            TaskService.this.worker.putTask(task);
        }
        public Service GetServer() {
            return TaskService.this;
        }
        public void StopWorker() {
            TaskService.this.worker.fin();
            TaskService.this.childThread = null;
        }
        public boolean StartWorker() {
            if (childThread != null) {
                return false;
            }
            TaskService.this.onCreate();
            return true;
        }
        public void clear(Object token) {
            TaskService.this.worker.clear(token);
        }
    }
}
