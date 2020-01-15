package cn.edu.hebut.easydesign.TaskWorker.BaseTasks;

import java.io.InputStream;

import cn.edu.hebut.easydesign.HttpClient.Client;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.Task;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class HostGetTask<T> extends Task<String, T> {
    private Condition<Integer> c;

    @Override
    protected boolean doOnService() {
        Response r = null;
        try {
            r = Client.getInstance().Get(data1);
            int code = r.code();
            if (code != 200) {
                c.condition = code;
                return true;
            }
            ResponseBody body = r.body();
            if (body != null) {
                InputStream stream = body.byteStream();
                if (stream != null) {
                    c.condition = HandleInput(stream);
                    return true;
                }
            }
            c.condition = 700;
            return true;
        } catch (Exception e) {
            c.condition = 700;
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
    protected abstract int HandleInput(InputStream inputStream);
}
