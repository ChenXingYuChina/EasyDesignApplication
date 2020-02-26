package cn.edu.hebut.easydesign.TaskWorker.BaseTasks;

import cn.edu.hebut.easydesign.TaskWorker.Condition;
import okhttp3.Response;

public abstract class StringHostPostTask extends CancelableHostPostTask {
    public StringHostPostTask(String api, Condition<Boolean> cancel) {
        super(api, cancel);
    }

    @Override
    protected int onReceiveResponse(Response response) {
        if (response.code() != 200) {
            return response.code();
        }
        if (response.body() == null) {
            return 700;
        }
        try {
            return handleResult(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
            return 701;
        }
    }

    protected abstract int handleResult(String result);
}
