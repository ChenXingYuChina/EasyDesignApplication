package cn.edu.hebut.easydesign.Activity.UserInformation;

import cn.edu.hebut.easydesign.Activity.UserInformation.CachedIdentity.DesignerCached;
import cn.edu.hebut.easydesign.Activity.UserInformation.CachedIdentity.PublicCached;
import cn.edu.hebut.easydesign.Activity.UserInformation.CachedIdentity.StudentCached;
import cn.edu.hebut.easydesign.HttpClient.Form.EmailField;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.IntField;
import cn.edu.hebut.easydesign.HttpClient.Form.JsonField;
import cn.edu.hebut.easydesign.HttpClient.Form.PasswordField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.Session.User.User;

public class UserInformation extends User {
    int identityType;
    private String password;
    private PublicCached publicCached;
    private StudentCached studentCached;
    private DesignerCached designerCached;

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

    void useStudent() throws Exception {
        this.identity = studentCached.student;
        formFields[Identity] = new JsonField("identity", identity.toJson());
    }

    void usePublic() throws Exception {
        identity = publicCached.goal;
        formFields[Identity] = new JsonField("identity", identity.toJson());
    }

    void userDesigner() throws Exception {
        identity = designerCached.designer;
        formFields[Identity] = new JsonField("identity", identity.toJson());
    }


    FormField[] getFormFields() {
        return formFields;
    }

    public void setPublicCached(PublicCached publicCached) {
        this.publicCached = publicCached;
    }

    public void setStudentCached(StudentCached studentCached) {
        this.studentCached = studentCached;
    }

    public void setDesignerCached(DesignerCached designerCached) {
        this.designerCached = designerCached;
    }

    public static class publicCached {

    }

}
