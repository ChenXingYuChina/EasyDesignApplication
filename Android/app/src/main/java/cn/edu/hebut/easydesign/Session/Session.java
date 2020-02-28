package cn.edu.hebut.easydesign.Session;

import android.app.Activity;
import android.content.SharedPreferences;

import org.json.JSONObject;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
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
    private long cachedId = 0;
    private String cachedPassword = "";
    public static ComplexString defaultDescription = new ComplexString("没有简介");
    public boolean login(JSONObject session, String cachedPassword) {
        try {
            sessionKey = session.getLong("session_key");
            user = new User(session.getJSONObject("user"));
            cachedId = user.id;
            this.cachedPassword = cachedPassword;
            SharedPreferences loginInformation = ContextHolder.getContext().getSharedPreferences("loginInformation", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = loginInformation.edit();
            editor.putString("pw", cachedPassword);
            editor.putLong("id", cachedId);
            editor.apply();
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

    public long getCachedId() {
        if (cachedId == 0) {
            SharedPreferences read = ContextHolder.getContext().getSharedPreferences("loginInformation", Activity.MODE_PRIVATE);;
            cachedId = read.getLong("id", 0);
            cachedPassword = read.getString("pw", "");
        }
        return cachedId;
    }

    public String getCachedPassword() {
        if (cachedPassword.length() == 0) {
            SharedPreferences read = ContextHolder.getContext().getSharedPreferences("loginInformation", Activity.MODE_PRIVATE);;
            cachedId = read.getLong("id", 0);
            cachedPassword = read.getString("pw", "");
        }
        return cachedPassword;
    }
}
