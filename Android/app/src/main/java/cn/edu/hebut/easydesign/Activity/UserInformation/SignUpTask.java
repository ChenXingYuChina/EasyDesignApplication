package cn.edu.hebut.easydesign.Activity.UserInformation;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.HostPostTask;
import okhttp3.Response;

public abstract class SignUpTask extends HostPostTask {
    private List<FormField> fields;
    private String result;

    public SignUpTask(List<FormField> formFields) {
        super("signUp");
        this.fields = formFields;
    }

    @Override
    protected int makeForm(Form form) {
        for (FormField field : fields) {
            if (field != null) {
                form.addFields(field);
            }
        }
        return 0;
    }

    @Override
    protected int onPostFinish(Response response) {
        int code = response.code();
        try {
            if (response.body() != null) {
                result = response.body().string();
            } else {
                result = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
            return 700;
        }

        return code == 200 ? 0 : code;
    }

    protected abstract void onSuccess(String message);

    protected abstract void onError(int error);

    protected abstract void onSignUpFail(String message);

    @Override
    protected void doOnMain() {
        Log.i("signUp", "doOnMain: " + condition.condition + result);
        if (condition.condition == 0) {
            onSuccess(result);
        } else if (result == null) {
            onError(condition.condition);
        } else {
            onSignUpFail(result);
        }
    }
}
