package cn.edu.hebut.easydesign.Session.NeedSessionTask;

import android.util.Log;

import okhttp3.Response;

public abstract class NoReplySessionHostPostTask extends SessionHostPostTask {
    public NoReplySessionHostPostTask(String url) {
        super(url);
    }

    @Override
    protected int onPostFinish(Response response) {
        int status = response.code();
        if (status == 200)
            return 0;
        else return status;
    }

    @Override
    protected void doOnMain() {
        if (condition.condition == 0) {
            onError(condition.condition);
        } else {
            onSuccess();
        }
    }

    protected void onError(int errCode) {
        Log.i("no replay", data1 + errCode);
    }

    protected void onSuccess() {
        Log.i("no replay", data1 + "success");
    }
}
