package cn.edu.hebut.easydesign.ComplexString;

import java.io.File;
import java.util.Map;
import java.util.Set;

import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ComplexStringField implements FormField {
    private ComplexString string;

    public ComplexStringField(ComplexString string) {
        this.string = string;
    }

    @Override
    public void addToFormBuilder(MultipartBody.Builder builder) throws Exception {
        builder.addFormDataPart("complexBase", string.toJson().toString());
        for (Map.Entry<Integer, String> paths : string.getImagePath().entrySet()) {
            builder.addFormDataPart("image" + paths.getKey().toString(), "", RequestBody.create(MultipartBody.FORM, new File(paths.getValue())));
        }
    }

    @Override
    public void addToFormBuilder(FormBody.Builder builder) throws Exception {
        throw new IllegalArgumentException("构建方式有误");
    }

    @Override
    public boolean ifMultiPart() {
        return true;
    }
}
