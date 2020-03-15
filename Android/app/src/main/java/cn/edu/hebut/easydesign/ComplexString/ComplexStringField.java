package cn.edu.hebut.easydesign.ComplexString;

import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import okhttp3.FormBody;
import okhttp3.MultipartBody;

public class ComplexStringField implements FormField {
    @Override
    public void addToFormBuilder(MultipartBody.Builder builder) throws Exception {

    }

    @Override
    public void addToFormBuilder(FormBody.Builder builder) throws Exception {

    }

    @Override
    public boolean ifMultiPart() {
        return false;
    }
}
