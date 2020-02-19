package cn.edu.hebut.easydesign.Session.User;


import org.json.JSONObject;

public abstract class Identity {
    public abstract JSONObject toJson() throws Exception;

    public abstract String toString();
}
