package cn.edu.hebut.easydesign.TaskWorker.BaseTasks;

import android.util.Log;

import cn.edu.hebut.easydesign.TaskWorker.Condition;
import okhttp3.Response;

public abstract class CancelableHostPostTask extends HostPostTask {
    protected Condition<Boolean> cancel;

    public CancelableHostPostTask(String api, Condition<Boolean> cancel) {
        super(api);
        this.cancel = cancel;
    }

    @Override
    protected int onPostFinish(Response response) {
        if (cancel.condition) {
            return -1;
        }
        return onReceiveResponse(response);
    }

    protected abstract int onReceiveResponse(Response response);

    @Override
    protected void doOnMain() {
        if (condition.condition == -1 && cancel.condition) {
            onCancel();
        }
        doOnMainNormal();
    }

    protected abstract void doOnMainNormal();

    protected void onCancel() {
        Log.i("cancel: ", "cancel");
    }
}
