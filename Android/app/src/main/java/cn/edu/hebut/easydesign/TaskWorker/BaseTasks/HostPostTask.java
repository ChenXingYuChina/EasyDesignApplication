package cn.edu.hebut.easydesign.TaskWorker.BaseTasks;

import android.util.Log;

import cn.edu.hebut.easydesign.HttpClient.Client;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.Task;
import okhttp3.Response;

public abstract class HostPostTask extends Task<String, Form> {
    protected Condition<Integer> condition = new Condition<>();
    public HostPostTask(String api){
        this.data1 = api;
        this.data2 = new Form();
        this.delay = 0;
    }

    public Condition<Integer> getCondition() {
        return condition;
    }

    @Override
    protected boolean doOnService() {
        Log.i("post", "doOnService: before");
        condition.condition = makeForm(data2);
        Log.i("post", condition.condition + "");
        if (condition.condition != 0) {
            return true;
        }
        Response r = null;
        try {
            r = Client.getInstance().PostToHost(data1, data2);
            Log.i("post", "doOnService: finish");
            condition.condition = onPostFinish(r);
            Log.i("HPT", "do");
        } catch (Exception e) {
            Log.i("HPT", e.toString());
            condition.condition = 700;
        } finally {
            if (r != null) {
                r.close();

                Log.i("HPT", "do2");
            }
        }
        return true;
    }

    /*
        set error as condition.
     */
    protected abstract int makeForm(Form form);
    protected abstract int onPostFinish(Response response);
}
