package cn.edu.hebut.easydesign.Session.User;

import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {
    public long id, coin, fansNumber, followNumber, passageNumber, headImage, backImage;
    public String name, phone, email;
    public Identity identity;

    public User() {

    }

    public User(JSONObject user) throws Exception {
        id = user.getLong("id");
        name = user.getString("name");
        email = user.getString("email");
        coin = user.getLong("coin");
        fansNumber = user.getLong("fans_number");
        followNumber = user.getLong("follow_number");
        passageNumber = user.getLong("passage_number");
        headImage = user.getLong("head_image");
        JSONObject identity = user.getJSONObject("identity");
        if (identity.length() == 2) {
            this.identity = new Public(identity);
        } else if (identity.has("schools")) {
            this.identity = new Student(identity);
        } else {
            this.identity = new Designer(identity);
        }
    }
}
