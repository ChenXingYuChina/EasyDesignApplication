package cn.edu.hebut.easydesign.Session;

import org.json.JSONObject;

import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.DataNet.DataNetClient;
import cn.edu.hebut.easydesign.Session.User.User;

public class Session {
    private static Session session = new Session();
    private Session(){}
    public static Session getSession() {
        return session;
    }

    private boolean loginState = false;
    public long sessionKey = -1;
    public User user = null;
    public ComplexString longDescription = null;
    public DataNetClient dataNetClient = null;
    public static ComplexString defaultDescription = new ComplexString("没有简介");
    public boolean login(JSONObject session) {
        try {
            sessionKey = session.getLong("session_key");
            user = new User(session.getJSONObject("user"));
            loginState = true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void logout() {
        loginState = false;
    }

    public boolean isLogin() {
        return loginState;
    }

    public boolean isTheLoginUser(long id) {
        return loginState && user != null && user.id == id;
    }
}
