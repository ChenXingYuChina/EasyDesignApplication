package cn.edu.hebut.easydesign.Session;

import org.json.JSONObject;

import cn.edu.hebut.easydesign.DataNet.DataNetClient;

public class Session {
    private static Session session = new Session();
    private Session(){}
    public static Session getSession() {
        return session;
    }
    long sessionKey;
    long userId;
    DataNetClient dataNetClient;
    public boolean login(String session) {
        try {
            JSONObject s = new JSONObject(session);
            sessionKey = s.getLong("session_key");
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
