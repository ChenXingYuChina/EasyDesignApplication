package cn.edu.hebut.easydesign.Resources.Passage;

import android.content.Context;
import android.os.IBinder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import cn.edu.hebut.easydesign.DataManagement.Data;
import cn.edu.hebut.easydesign.DataManagement.DataLoader;

public class PassageLoader implements DataLoader {
    @Override
    public Data LoadFromNet(Context ctx, IBinder binder, InputStream stream, long id) throws Exception {
        JSONObject object = new JSONObject(new BufferedReader(new InputStreamReader(stream)).readLine());
        return new Passage(ctx, binder, object, object.getBoolean("full"));
    }

    @Override
    public Data LoadFromCache(Context ctx, IBinder binder, InputStream stream, long id) throws Exception {
        return (Data) new ObjectInputStream(stream).readObject();
    }
}
