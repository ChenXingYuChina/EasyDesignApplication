package cn.edu.hebut.easydesign.Session;

import org.json.JSONObject;

public class User {
    public long id, coin, fansNumber, followNumber, passageNumber, headImage, backImage;
    public String name, phone, email;
    public Identity identity;

    public User() {

    }
    public User(JSONObject user) {

    }
}
