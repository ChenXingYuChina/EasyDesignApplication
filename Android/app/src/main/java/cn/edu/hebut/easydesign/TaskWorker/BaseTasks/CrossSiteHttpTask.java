package cn.edu.hebut.easydesign.TaskWorker.BaseTasks;

import android.net.Uri;

import java.net.URL;

import cn.edu.hebut.easydesign.HttpClient.Client;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.Task;
import okhttp3.Request;
import okhttp3.Response;

public abstract class CrossSiteHttpTask<T> extends Task<URL, T> {
    protected Condition<Integer> condition = new Condition<>(0);
    public CrossSiteHttpTask(URL url) {
        data1 = url;
    }
    @Override
    protected boolean doOnService() {
        Response response = null;
        try {
            condition.condition = beforeProcess();
            if (condition.condition != 0) {
                return true;
            }
            Request.Builder builder = new Request.Builder().url(data1);
            condition.condition = beforeRequest(builder);
            if (condition.condition != 0) {
                return true;
            }
            response = Client.getInstance().client.newCall(builder.build()).execute();
            condition.condition = processResponse(response);
            if (condition.condition != 0) {
                return true;
            }
        } catch (Exception e) {
            condition.condition = onError(e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return true;
    }
    protected abstract int processResponse(Response r);
    protected abstract int beforeRequest(Request.Builder r);
    protected abstract int beforeProcess();
    protected abstract int onError(Exception e);
}
