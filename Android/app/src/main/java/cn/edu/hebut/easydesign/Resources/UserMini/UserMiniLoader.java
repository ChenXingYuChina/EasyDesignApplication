package cn.edu.hebut.easydesign.Resources.UserMini;

import android.content.Context;
import android.os.IBinder;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

import cn.edu.hebut.easydesign.DataManagement.Data;
import cn.edu.hebut.easydesign.DataManagement.DataLoader;

public class UserMiniLoader implements DataLoader {
    @Override
    public Data LoadFromNet(Context ctx, IBinder binder, InputStream stream, long id) throws Exception {
        JSONObject userMini = new JSONObject(new BufferedInputStream(stream).toString());
        UserMini goal = new UserMini();
        goal.id = userMini.getLong("id");
        goal.name = userMini.getString("name");
        goal.headImage = userMini.getLong("head_image");
        goal.identity = (byte) userMini.getInt("identity");
        return goal;
    }

    @Override
    public Data LoadFromCache(Context ctx, IBinder binder, InputStream stream, long id) throws Exception {
        return (Data) new ObjectInputStream(stream).readObject();
    }
}
