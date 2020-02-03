package cn.edu.hebut.easydesign.Resources.UserMini;

import android.content.Context;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.StringReader;

import cn.edu.hebut.easydesign.DataManagement.Data;
import cn.edu.hebut.easydesign.DataManagement.DataLoader;

public class UserMiniLoader implements DataLoader {
    @Override
    public UserMini LoadFromNet(String r, long id) throws Exception {
        return UserMini.parseJson(new JSONObject(r));
    }

    @Override
    public UserMini LoadFromCache(InputStream stream, long id) throws Exception {
        return (UserMini) new ObjectInputStream(stream).readObject();
    }
}
