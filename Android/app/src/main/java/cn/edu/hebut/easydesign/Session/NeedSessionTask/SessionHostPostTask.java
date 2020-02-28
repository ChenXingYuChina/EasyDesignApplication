package cn.edu.hebut.easydesign.Session.NeedSessionTask;

import android.util.Log;

import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.LongField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.Session.LoginTask;
import cn.edu.hebut.easydesign.Session.Session;
import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.HostPostTask;
import cn.edu.hebut.easydesign.TaskWorker.Task;
import okhttp3.Response;

public abstract class SessionHostPostTask extends HostPostTask {
    public SessionHostPostTask(String url) {
        super(url);
    }

    @Override
    protected int onPostFinish(Response response) {
        int code = response.code();
        if (code == 401) {
            try {
                loginTask task = new loginTask(Session.getSession().getCachedId(), Session.getSession().getCachedPassword());
                task.doOnService();
                if (task.getCondition().condition != 0) {
                    return task.getCondition().condition;
                }
                doOnService();
            } catch (Exception e) {
                Log.e("ED", "onPostFinish: retry login fail", e);
                code = 700;
            }
        }
        if (code != 200) {
            return code;
        }
        return onPostSuccess(response);
    }

    @Override
    protected int makeForm(Form form) {
        Session session = Session.getSession();
        form.addFields(new TextField("sessionId", "" + session.sessionKey)).addFields(new LongField("userId", session.user.id));
        return makeForm(form, session);
    }

    protected abstract int onPostSuccess(Response response);

    protected abstract int makeForm(Form form, Session session);

    private class loginTask extends LoginTask {

        public loginTask(long id, String pw) throws Exception {
            super(id, pw);
        }

        @Override
        public boolean doOnService() {
            return super.doOnService();
        }

        @Override
        protected void doOnMainNormal() {

        }
    }

}
