package cn.edu.hebut.easydesign.Activity.UserInformation;

import cn.edu.hebut.easydesign.HttpClient.Form.FormField;

public abstract class SignUpQuickTask extends SignUpTask {
    public SignUpQuickTask(FormField[] fields) {
        super(true, fields);
    }


    @Override
    protected void onError(int error) {
        onSignUpFail("注册失败，错误代码：" + error);
    }
}
