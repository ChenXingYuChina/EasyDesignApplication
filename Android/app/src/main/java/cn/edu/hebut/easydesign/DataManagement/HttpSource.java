package cn.edu.hebut.easydesign.DataManagement;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpSource implements DataSource {
    private String netAddress;

    HttpSource(String hostName) {
        this.netAddress = hostName + "/";
    }

    @Override
    public Uri UriOf(DataType type, long id) {
        return Uri.parse(makePath(type, id));
    }
    public String makePath(DataType type, long id) {
        return netAddress + type.path + "?id=" + id;
    }

    @Override
    public InputStream Load(DataType type, long id) {
        try {
            return new URL(netAddress + type.path + "?id=" + id).openConnection().getInputStream();
        } catch (Exception e) {
            return null;
        }
    }
}
