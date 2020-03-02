package cn.edu.hebut.easydesign.Activity.SignUp;

import cn.edu.hebut.easydesign.HttpClient.Form.BooleanField;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.StringHostPostTask;
import cn.edu.hebut.easydesign.TaskWorker.Condition;

public abstract class SignUpTask extends StringHostPostTask {
    private boolean quick;
    private FormField[] fields;
    private String result;
    private boolean success;

    public SignUpTask(boolean quick, FormField[] fields) {
        super("signUp", new Condition<Boolean>(false));
        this.quick = quick;
        this.fields = fields;
    }

    @Override
    protected void doOnMainNormal() {
        if (success) {
            onSuccess(result);
        } else {
            if (condition.condition != 0) {
                onError(condition.condition);
            } else {
                onSignUpFail(result);
            }
        }
    }

    @Override
    protected int handleResult(String result) {
        this.result = result;
        if (result.startsWith("注册"))
            success = true;
        return 0;
    }

    @Override
    protected int makeForm(Form form) {
        form.addFields(new BooleanField("quick", quick));
        for (FormField field : fields) {
            if (field != null) {
                form.addFields(field);
            }
        }
        return 0;
    }

    protected abstract void onSuccess(String message);

    protected abstract void onError(int error);

    protected abstract void onSignUpFail(String message);
}
