package cn.edu.hebut.easydesign.HttpClient.Form;

import okhttp3.FormBody;
import okhttp3.MultipartBody;

interface FormField {
    void addToFormBuilder(MultipartBody.Builder builder) throws Exception;
    void addToFormBuilder(FormBody.Builder builder) throws Exception;
    boolean ifMultiPart();
}
