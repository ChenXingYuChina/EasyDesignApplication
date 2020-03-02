package cn.edu.hebut.easydesign.Activity.SignUp;

import cn.edu.hebut.easydesign.HttpClient.Form.EmailField;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.IntField;
import cn.edu.hebut.easydesign.HttpClient.Form.JsonField;
import cn.edu.hebut.easydesign.HttpClient.Form.PasswordField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.Session.User.Identity;
import cn.edu.hebut.easydesign.Session.User.User;

class UserInformation extends User {
    int identityType;
    private String password;

    private static final int Password = 0;
    private static final int Name = 1;
    private static final int Email = 2;
    private static final int IdentityType = 3;
    private static final int Identity = 4;
    private FormField[] formFields = new FormField[5];

    void setPassword(String password) throws Exception {
        this.password = password;
        formFields[Password] = new PasswordField("pw", password);
    }

    void setIdentityType(int identityType) {
        this.identityType = identityType;
        formFields[IdentityType] = new IntField("identityType", identityType);
    }

    void setName(String name) {
        this.name = name;
        formFields[Name] = new TextField("name", name);
    }

    void setEmail(String email) throws Exception {
        this.email = email;
        formFields[Email] = new EmailField("email", email);
    }

    void setIdentity(Identity identity) throws Exception {
        this.identity = identity;
        formFields[Identity] = new JsonField("identity", identity.toJson());
    }

    FormField[] getFormFields() {
        return formFields;
    }
}
