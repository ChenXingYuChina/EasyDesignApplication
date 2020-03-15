package cn.edu.hebut.easydesign.Activity.Fragment.UserPage.ChangeInformation;

import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.Session.NeedSessionTask.NoReplySessionHostPostTask;
import cn.edu.hebut.easydesign.Session.Session;

public class LogoutTask extends NoReplySessionHostPostTask {

    public LogoutTask() {
        super("logout");
    }

    @Override
    protected int makeForm(Form form, Session session) {
        return 0;
    }
}
