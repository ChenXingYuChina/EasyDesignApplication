package cn.edu.hebut.easydesign;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import cn.edu.hebut.easydesign.HttpClient.Client;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String path = appContext.getFilesDir().getAbsolutePath();
        try {
            if (path != null) {
                File f = new File(new URI(Uri.parse("file://" + path + "/abc").toString()));
//                f.mkdir();
                Log.i("path", "" + f.exists());
                Log.i("path", "top");
            } else {
                Log.i("path", appContext.getCacheDir().getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("cn.edu.hebut.easydesign", appContext.getPackageName());
    }

    @Test
    public void TestLogin() {
        FormBody.Builder builder = new FormBody.Builder().add("id", 36+"").add("pw", "assg");
        try {
            Request.Builder builder1 = new Request.Builder();
            builder1.url("http://192.168.31.216/" + "loginId");
            builder1.method("POST", builder.build());
            System.out.println(Client.getInstance().client);
            Request request = builder1.build();
            Call call = Client.getInstance().client.newCall(request);
            Response r = call.execute();
            System.out.println(r.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
