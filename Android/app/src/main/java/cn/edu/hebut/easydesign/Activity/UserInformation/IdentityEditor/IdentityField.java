package cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor;

import java.io.Serializable;

import cn.edu.hebut.easydesign.HttpClient.Form.JsonField;
import cn.edu.hebut.easydesign.Session.User.Identity;

public class IdentityField extends JsonField implements Serializable {
    private Identity cache;
    public IdentityField(String fieldName, Identity identity) throws Exception {
        super(fieldName, identity.toJson());
        this.cache = identity;
    }
}
