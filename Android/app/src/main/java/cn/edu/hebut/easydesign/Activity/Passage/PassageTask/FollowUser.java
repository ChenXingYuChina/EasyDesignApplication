package cn.edu.hebut.easydesign.Activity.Passage.PassageTask;

import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.LongField;
import cn.edu.hebut.easydesign.Session.NeedSessionTask.NoReplySessionHostPostTask;
import cn.edu.hebut.easydesign.Session.Session;

public class FollowUser extends NoReplySessionHostPostTask {
    private long id;
    public FollowUser(long id) {
        super("follow");
        this.id = id;
    }

    @Override
    protected int makeForm(Form form, Session session) {
        form.addFields(new LongField("who", id));
        return 0;
    }
}
