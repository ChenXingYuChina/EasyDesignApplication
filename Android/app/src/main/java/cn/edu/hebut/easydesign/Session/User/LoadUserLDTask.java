package cn.edu.hebut.easydesign.Session.User;

import android.util.Log;

import org.json.JSONObject;

import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.ComplexString.ComplexStringLoader;
import cn.edu.hebut.easydesign.Session.Session;
import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.HostGetTask;

public abstract class LoadUserLDTask extends HostGetTask<ComplexString> {
    public LoadUserLDTask(long id) {
        super("userLD?id=" + id);
    }

    @Override
    protected int HandleInput(String string) {
        try {
            Session session = Session.getSession();
            if (string.equals("null")) {
                session.longDescription = Session.defaultDescription;
            } else {
                session.longDescription = ComplexStringLoader.getInstance().LoadFromNet(new JSONObject(string));
            }
            data2 = session.longDescription;
        } catch (Exception e) {
            Log.i("LULDT", "HandleInput: ");
            return 702;
        }
        return 0;
    }

    @Override
    protected void doOnMain() {
        if (c.condition == 0) {
            setLongDescription(data2);
        } else {
            onError(c.condition);
        }
    }

    protected abstract void setLongDescription(ComplexString longDescription);

    protected abstract void onError(int errorCode);
}
