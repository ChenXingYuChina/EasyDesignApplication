package cn.edu.hebut.easydesign.Session;

import org.json.JSONObject;

import cn.edu.hebut.easydesign.HttpClient.Form.EmailField;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.PasswordField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.StringHostPostTask;
import cn.edu.hebut.easydesign.TaskWorker.Condition;

public abstract class LoginTask extends StringHostPostTask {
    private TextField account;
    private PasswordField pw;
    private String cachedPassword;
    public LoginTask(long id, String pw) throws Exception {
        super("loginId", new Condition<Boolean>(false));
        if (id == 0) {
            throw new IllegalArgumentException();
        }
        this.account = new TextField("id", id+"");
        cachedPassword = pw;
        this.pw = new PasswordField("pw", pw);
    }

    public LoginTask(String email, String pw) throws Exception {
        super("loginEmail", new Condition<>(false));
        this.account = new EmailField("email", email);
        cachedPassword = pw;
        this.pw = new PasswordField("pw", pw);
    }

    @Override
    protected int makeForm(Form form) {
        form.addFields(account).addFields(pw);
        return 0;
    }

    @Override
    protected int handleResult(String result) {
        try {
            if (!Session.getSession().login(new JSONObject(result), cachedPassword)) {
                return 703;
            }
        } catch (Exception e) {
            return 702;
        }
        return 0;
    }

    @Override
    protected void doOnMainNormal() {
        if (condition.condition == 0) {
            loginSuccess();
        } else {
            loginFail(condition.condition);
        }
    }

    protected abstract void loginSuccess();

    protected abstract void loginFail(int code);
}
