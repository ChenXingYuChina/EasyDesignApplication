package cn.edu.hebut.easydesign.HttpClient.Form;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MultipartBody;

public class JsonField implements FormField {
    private String fieldName;
    private JSONObject dataObject;
    private JSONArray dataArray;

    public JsonField(String fieldName, JSONObject object) {
        this.fieldName = fieldName;
        dataObject = object;
        dataArray = null;
    }

    public JsonField(String fieldName, JSONArray array) {
        dataArray = array;
        dataObject = null;
        this.fieldName = fieldName;
    }

    @Override
    public void addToFormBuilder(MultipartBody.Builder builder) throws Exception {
        builder.addFormDataPart(fieldName, dataArray == null ? dataObject.toString() : dataArray.toString());
    }

    @Override
    public void addToFormBuilder(FormBody.Builder builder) throws Exception {
        Log.i("signUp", "addToFormBuilder: " + (dataArray == null));
        builder.add(fieldName, dataArray == null ? dataObject.toString() : dataArray.toString());
    }

    @Override
    public boolean ifMultiPart() {
        return false;
    }
}
