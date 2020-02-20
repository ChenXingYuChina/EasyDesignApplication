package cn.edu.hebut.easydesign.Resources.UserMini;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.charset.Charset;

import cn.edu.hebut.easydesign.DataManager.DataLoader;
import cn.edu.hebut.easydesign.DataManager.DataType;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.LongField;

public class UserMiniLoader extends DataLoader<UserMini> {
    private static UserMiniLoader instance;

    private UserMiniLoader() {
        super();
    }

    public static UserMiniLoader getInstance() {
        if (instance == null) {
            synchronized (UserMiniLoader.class) {
                if (instance == null) {
                    instance = new UserMiniLoader();
                }
            }
        }
        return instance;
    }

    public UserMini load(long id) {
        return loadData(DataType.UserMini, id);
    }

    @Override
    protected Form makeForm(DataType type, long id, Object... extraArgs) {
        Form form = new Form();
        form.addFields(new LongField("id", id));
        return form;
    }

    @Override
    protected UserMini buildDataFromNet(byte[] data, long id) {
        try {
            return UserMini.parseJson(new JSONObject(new String(data, Charset.defaultCharset())));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected UserMini rebuildFromCache(InputStream stream, long id) {
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(stream);
            return (UserMini) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
