package cn.edu.hebut.easydesign.HttpClient.Form;

import android.util.Log;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.VisibleForTesting;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okio.ByteString;

public class PasswordField implements FormField {
    private String fieldName;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public String pw;
    private boolean md5;
    public static final char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public PasswordField(String fieldName, String pw) throws Exception {
        this.fieldName = fieldName;
        this.pw = pw;
        check();
    }

    private void check() throws Exception {
        // todo check if the password is right throw Exception means wrong
    }

    private void toMD5() {
        MessageDigest a;
        try {
            a = MessageDigest.getInstance("md5");
        } catch (Exception e) {
            Log.e("ED", "toMD5: ", e);
            return;
        }
        a.update(pw.getBytes());
        byte[] mid = a.digest();
//        System.out.println(ByteString.of(mid).hex());
        mid[15] = 0;
        for (int i = 0; i < 15; i++) {
            mid[15] ^= mid[i];
        }
        int minute = (int) (new Date().getTime() / (60000));
//        System.out.println(minute);
        char[] goal = new char[32];
        for (int i = 0; i < 16; i++) {
            int m = 0xff & (mid[i] ^ (minute + i));
//            System.out.println(m);
            goal[2*i+1] = hex[m & 0xf];
            goal[2*i] = hex[(m)>>4 & 0xf];
        }
        pw = String.valueOf(goal);
        md5 = true;
    }

    @Override
    public void addToFormBuilder(MultipartBody.Builder builder) throws Exception {
        if (!md5) {
            toMD5();
        }
        builder.addFormDataPart(fieldName, pw);
    }

    @Override
    public void addToFormBuilder(FormBody.Builder builder) throws Exception {
        if (!md5) {
            toMD5();
        }
        builder.add(fieldName, pw);
    }

    @Override
    public boolean ifMultiPart() {
        return false;
    }

    public void setPw(String pw) throws Exception {
        check();
        this.pw = pw;
        this.md5 = false;
    }
}
