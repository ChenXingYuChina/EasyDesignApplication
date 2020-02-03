package cn.edu.hebut.easydesign.Resources.Passage;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import cn.edu.hebut.easydesign.DataManagement.DataLoader;

public class PassageLoader implements DataLoader {
    @Override
    public Passage LoadFromNet(String r, long id) throws Exception {
        JSONObject object = new JSONObject(r);
        return new Passage(object, object.getBoolean("full"));
    }

    @Override
    public Passage LoadFromCache(InputStream stream, long id) throws Exception {
        return (Passage) new ObjectInputStream(stream).readObject();
    }
}
