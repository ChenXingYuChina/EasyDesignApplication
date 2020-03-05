package cn.edu.hebut.easydesign.Resources.Passage;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.charset.Charset;

import cn.edu.hebut.easydesign.DataManager.DataLoader;
import cn.edu.hebut.easydesign.DataManager.DataType;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.LongField;
import cn.edu.hebut.easydesign.HttpClient.Form.ShortField;

public class PassageLoader extends DataLoader<Passage> {
    private PassageLoader() {
        super();
    }

    private static final PassageLoader instance = new PassageLoader();

    public static PassageLoader getInstance() {
        return instance;
    }

    public Passage load(long id, short type) {
        return loadData(DataType.Passage, id, type);
    }

    private Form makeForm(long id, short type) {
        return (new Form()).addFields(new LongField("id", id)).addFields(new ShortField("type", type));
    }

    @Override
    protected Form makeForm(DataType type, long id, Object... extraArgs) {
        return makeForm(id, (Short) extraArgs[0]);
    }

    @Override
    protected Passage buildDataFromNet(byte[] data, long id) {
        try {
            JSONObject json = new JSONObject(new String(data, Charset.defaultCharset()));
            return new Passage(json, json.getBoolean("full"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Passage rebuildFromCache(InputStream stream, long id) {
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(stream);
            return (Passage) inputStream.readObject();
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
