package cn.edu.hebut.easydesign.TaskWorker.BaseTasks;

import java.io.InputStream;

import cn.edu.hebut.easydesign.HttpClient.Client;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.Task;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class HostGetTask<T> extends Task<String, T> {
    protected Condition<Integer> c = new Condition<>(0);
    public HostGetTask(String api) {
        this.data1 = api;
    }

    @Override
    protected boolean doOnService() {
        Response r = null;
        try {
            r = Client.getInstance().GetFromHost(data1);
            int code = r.code();
            if (code != 200) {
                c.condition = code;
                return true;
            }
            ResponseBody body = r.body();
            if (body != null) {
                c.condition = HandleInput(body.string());

            } else {
                c.condition = 700;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            c.condition = 710;
        } finally {
            if (r != null) {
                r.close();
            }
        }
        return true;
    }

    /*
    set data to data2, and return error code.
     */
    protected abstract int HandleInput(String string);
}
