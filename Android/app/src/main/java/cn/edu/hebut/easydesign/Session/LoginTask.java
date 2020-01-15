package cn.edu.hebut.easydesign.Session;

import android.util.Log;

import java.io.IOException;

import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.HostPostTask;
import cn.edu.hebut.easydesign.HttpClient.Client;
import cn.edu.hebut.easydesign.HttpClient.Form.EmailField;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.PasswordField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class LoginTask extends HostPostTask {
    private TextField account;
    private PasswordField pw;
    public LoginTask(long id, String pw) throws Exception {
        super("loginId");
        if (id == 0) {
            throw new IllegalArgumentException();
        }
        this.account = new TextField("id", id+"");
        this.pw = new PasswordField("pw", pw);
    }

    public LoginTask(String email, String pw) throws Exception {
        super("loginEmail");
        this.account = new EmailField("email", email);
        this.pw = new PasswordField("pw", pw);
    }

    @Override
    protected int makeForm(Form form) {
        form.AddFields(account).AddFields(pw);
        return 0;
    }

    @Override
    protected int onPostFinish(Response response) {
        int code = Client.GetStatusCode(response);
        if (code != 200) {
            return code;
        }
        try {
            ResponseBody body = response.body();
            if (body != null) {
                String s = body.string();
                if (s == null) {
                    return 701;
                }
                if (!Session.getSession().login(s)) {
                    return 702;
                }
            } else {
                return 703;
            }
        } catch (Exception e) {
            Log.i("ED", "onPostFinish: "+e);
            return 704;
        }
        return 0;
    }

}
