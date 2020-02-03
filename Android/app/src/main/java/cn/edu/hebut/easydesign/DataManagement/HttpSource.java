package cn.edu.hebut.easydesign.DataManagement;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.edu.hebut.easydesign.HttpClient.Client;
import okhttp3.Response;

public class HttpSource {
    private String netAddress;

    HttpSource(String hostName) {
        this.netAddress = "http://" +hostName + "/";
    }

    public Uri UriOf(DataType type, long id) {
        return Uri.parse(makePath(type, id));
    }
    public String makePath(DataType type, long id) {
        return netAddress + type.path + "?id=" + id;
    }

    public String Load(DataType type, long id) {
        try {
            Response r = Client.getInstance().GetFromHost(type.path + "?id=" + id);
            if (r.body() != null) {
                return r.body().string();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
