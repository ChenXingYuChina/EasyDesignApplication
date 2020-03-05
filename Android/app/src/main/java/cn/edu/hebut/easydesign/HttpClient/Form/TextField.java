package cn.edu.hebut.easydesign.HttpClient.Form;

import android.util.Log;

import okhttp3.FormBody;
import okhttp3.MultipartBody;

/*
this can be extends to add a check method to let it meet email or others.
 */
public class TextField implements FormField {

    protected String fieldName, data;

    public TextField(String fieldName, String data) {
        this.fieldName = fieldName;
        this.data = data;
    }

    @Override
    public void addToFormBuilder(MultipartBody.Builder builder) throws Exception {
        builder.addFormDataPart(fieldName, data);
    }

    @Override
    public void addToFormBuilder(FormBody.Builder builder) throws Exception {
        Log.i("build", fieldName + data);
        builder.add(fieldName, data);
    }

    @Override
    public boolean ifMultiPart() {
        return false;
    }


    public void setData(String data) {
        this.data = data;
        Log.i("build", this.data);
    }
}
