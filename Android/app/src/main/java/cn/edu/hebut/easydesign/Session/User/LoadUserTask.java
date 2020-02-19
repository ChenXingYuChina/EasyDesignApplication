package cn.edu.hebut.easydesign.Session.User;

import android.util.Log;

import org.json.JSONObject;

import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.HostGetTask;

public abstract class LoadUserTask extends HostGetTask<User> {
    public LoadUserTask(long id) {
        super("user?id=" + id);
    }

    @Override
    protected int HandleInput(String string) {
        try {
            data2 = new User(new JSONObject(string));
        } catch (Exception e) {
            Log.e("LUT", "error: ", e);
            return 702;
        }
        return 0;
    }

    @Override
    protected void doOnMain() {
        if (c.condition == 0) {
            onSuccess(data2);
        } else {
            onError(c.condition);
        }
    }

    protected abstract void onSuccess(User user);

    protected abstract void onError(int errCode);
}
