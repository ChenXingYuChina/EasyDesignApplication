package cn.edu.hebut.easydesign.Activity.Passage.PassageTask;

import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.LongField;
import cn.edu.hebut.easydesign.Session.NeedSessionTask.NoReplySessionHostPostTask;
import cn.edu.hebut.easydesign.Session.Session;

public class StarPassageTask extends NoReplySessionHostPostTask {
    long pid;
    public StarPassageTask(long pid) {
        super("starThePassage");
        this.pid = pid;
    }

    @Override
    protected int makeForm(Form form, Session session) {
        form.addFields(new LongField("pid", pid));
        return 0;
    }
}
