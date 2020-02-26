package cn.edu.hebut.easydesign.Session.NeedSessionTask;

import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.LongField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.Session.Session;
import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.HostPostTask;

public abstract class SessionHostPostTask extends HostPostTask {
    public SessionHostPostTask(String url) {
        super(url);
    }

    @Override
    protected int makeForm(Form form) {
        Session session = Session.getSession();
        form.addFields(new TextField("session_key", "" + session.sessionKey)).addFields(new LongField("UserId", session.user.id));
        return makeForm(form, session);
    }

    protected abstract int makeForm(Form form, Session session);
}