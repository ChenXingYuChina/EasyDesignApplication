package cn.edu.hebut.easydesign;

import android.util.Log;

import org.junit.Test;

import java.security.MessageDigest;
import java.util.Arrays;

import cn.edu.hebut.easydesign.HttpClient.Form.PasswordField;
import okhttp3.MultipartBody;

public class MD5_test {
    @Test
    public void Md5Test() {
        try {
            PasswordField field = new PasswordField("pw", "hello world");
            field.addToFormBuilder(new MultipartBody.Builder());
//            System.out.println(field.pw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Md52Test() {
        MessageDigest a;
        try {
            a = MessageDigest.getInstance("md5");
            System.out.println(Arrays.toString("hello world".getBytes()));
            byte[] mid = a.digest("hello world".getBytes());
            System.out.println(Arrays.toString(mid));
        } catch (Exception e) {
            return;
        }
//        a.update("hello world".getBytes());
    }
}
