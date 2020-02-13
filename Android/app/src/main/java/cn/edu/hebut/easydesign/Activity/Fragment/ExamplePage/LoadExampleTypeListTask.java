package cn.edu.hebut.easydesign.Activity.Fragment.ExamplePage;

import android.content.Context;

import org.json.JSONObject;

import java.io.InputStream;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.HostGetTask;

public abstract class LoadExampleTypeListTask extends HostGetTask<JSONObject> {

    public LoadExampleTypeListTask(String api) {
        super(api);
    }

    @Override
    protected boolean doOnService() {
        return super.doOnService();
    }

    @Override
    protected int HandleInput(String result) {
        try {
            data2 = new JSONObject(result);
        } catch (Exception e) {
            return 703;
        }
        return 0;
    }

    @Override
    protected void doOnMain() {
        if (c.condition != 0) {
            handleError(c.condition);
        } else {
            handleResult(data2);
        }
    }

    protected abstract void handleResult(JSONObject result);

    protected abstract void handleError(int errorCode);
}
