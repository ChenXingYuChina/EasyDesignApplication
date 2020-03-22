package cn.edu.hebut.easydesign.HttpClient.Form;

import android.util.Log;

import java.security.MessageDigest;
import java.util.Date;

import androidx.annotation.Nullable;
import okhttp3.FormBody;
import okhttp3.MultipartBody;

public class PasswordField implements FormField {
    private String fieldName;
    private String pw;
    private boolean md5;
    public static final char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public PasswordField(String fieldName, String pw) throws Exception {
        this.fieldName = fieldName;
        this.pw = pw;
        check();
    }

    private void check() throws Exception {
        if(pw.length()<6 || pw.length()>20)
        {
            throw new Exception("密码长度应在6-20位之间!");
        }
        else
        {
            char[] chs= pw.toCharArray();
            int[] index= {0,0,0};
            String regix = ".,;_-+!@#$%^&*";
            for(int i = 0;i<chs.length;i++) {
                if(Character.isLowerCase(chs[i]) || Character.isUpperCase(chs[i]))
                {
                    index[0]=1;
                }
                else if(Character.isDigit(chs[i]))
                {
                    index[1]=1;
                }
                else if(regix.contains(String.valueOf(chs[i])))
                {
                    index[2]=1;
                }
                else {
                    String warning = "符号'" + chs[i] + "'不合法，特殊符号只能从.,;_-+!@#$%^&*中选择!";
                    throw new Exception(warning);
                }
            }
            int count=0;
            for(int j=0;j<index.length;j++)
            {
                if(index[j]==1)
                {
                    count++;
                }
            }
            if(count<2) {
                throw new Exception("密码至少由数字、字母、特殊符号中的两种组成!");
            }
        }
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
        int minute = (int) (new Date().getTime() / (60000L));
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
