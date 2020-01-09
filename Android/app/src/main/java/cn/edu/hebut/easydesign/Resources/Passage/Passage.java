package cn.edu.hebut.easydesign.Resources.Passage;

import android.content.Context;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.ComplexString.ComplexStringLoader;
import cn.edu.hebut.easydesign.DataManagement.Data;
import cn.edu.hebut.easydesign.DataManagement.DataType;
import cn.edu.hebut.easydesign.Resources.Media.MultiMedia.MultiMedia;

public class Passage implements Data {
    public ComplexString content;
    public MultiMedia media;
    public long id;
    public transient ArrayList<Comment> comments;

    public Passage(Context ctx, IBinder binder, JSONObject passage, boolean full) throws Exception {
        try {
            content = ComplexStringLoader.getInstance().LoadFromNet(ctx, binder, passage.getJSONObject("body"));
        } catch (Exception ignored) {
        }
        try {
            media = new MultiMedia(passage.getJSONObject("media"));
        } catch (Exception ignored) {
        }
        if (content == null && media == null) {
            throw new Exception();
        }
        id = passage.getLong("id");
        if (full) {
            JSONArray comments = passage.getJSONArray("com");
            JSONArray subComments = passage.getJSONArray("sub_com");
            this.comments = new ArrayList<>();
            int length = comments.length();
            for (int i = 0; i < length; i++) {
                try {
                    this.comments.add(new Comment(ctx, binder, comments.getJSONObject(i), subComments.getJSONArray(i)));
                } catch (Exception e) {
                    Log.i("Pass", "Passage: pass comment at order: "+ i + ", total: " + length);
                }
            }
        }
    }

    @Override
    public long GetId() {
        return id;
    }

    @Override
    public DataType GetType() {
        return DataType.Passage;
    }

    @Override
    public boolean onCache() {
        if (id == 0) {
            return false;
        }
        try {
            content.parseToServerFormat();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
