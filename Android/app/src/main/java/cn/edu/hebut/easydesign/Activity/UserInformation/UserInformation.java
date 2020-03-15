package cn.edu.hebut.easydesign.Activity.UserInformation;

import cn.edu.hebut.easydesign.HttpClient.Form.EmailField;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.IntField;
import cn.edu.hebut.easydesign.HttpClient.Form.JsonField;
import cn.edu.hebut.easydesign.HttpClient.Form.PasswordField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.Session.User.Identity;
import cn.edu.hebut.easydesign.Session.User.User;

public class UserInformation extends User {
    private int identityType;
    private String password;

    private static final int Password = 0;
    private static final int Name = 1;
    private static final int Email = 2;
    private static final int IdentityType = 3;
    private static final int Identity = 4;
    private FormField[] formFields = new FormField[5];

    public void setPassword(String password) throws Exception {
        this.password = password;
        formFields[Password] = new PasswordField("pw", password);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) throws Exception {
        this.email = email;
        formFields[Email] = new EmailField("email", email);
    }

    public void setIdentity(Identity identity) {
        this.identityType = identity.getType();
        this.identity = identity;
    }


    FormField[] getFormFields() throws Exception {
        formFields[Name] = new TextField("name", name);
        formFields[Identity] = new JsonField("identity", identity.toJson());
        formFields[IdentityType] = new IntField("identityType", identityType);
        return formFields;
    }
}
