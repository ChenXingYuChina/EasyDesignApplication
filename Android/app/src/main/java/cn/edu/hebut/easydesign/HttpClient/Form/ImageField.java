package cn.edu.hebut.easydesign.HttpClient.Form;

import android.net.Uri;
import android.util.Log;

import java.io.File;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImageField implements FormField {
    private File file;
    private String fieldName;

    public ImageField(String fieldName, String path) {
        file = new File(path);
        this.fieldName = fieldName;
    }

    public ImageField(String fieldName, Uri uri) {
        this(fieldName, uri.getPath());
    }

    @Override
    public void addToFormBuilder(MultipartBody.Builder builder) throws Exception {
        RequestBody body = RequestBody.create(MultipartBody.FORM, file);
        builder.addFormDataPart(fieldName, file.getName(), body);
        Log.i("image", "addToFormBuilder: add success");
    }

    @Override
    public void addToFormBuilder(FormBody.Builder builder) throws Exception {
        throw new IllegalArgumentException("add image to the normal form");
    }

    @Override
    public boolean ifMultiPart() {
        return true;
    }
}
