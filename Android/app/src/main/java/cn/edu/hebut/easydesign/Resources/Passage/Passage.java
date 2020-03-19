package cn.edu.hebut.easydesign.Resources.Passage;

import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.RequiresApi;
import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.ComplexString.ComplexStringLoader;
import cn.edu.hebut.easydesign.DataManager.Data;
import cn.edu.hebut.easydesign.DataManager.DataType;
import cn.edu.hebut.easydesign.Resources.Media.MultiMedia.MultiMedia;

public class Passage implements Data {
    public ComplexString content;
    public MultiMedia media;
    public long id;
    public transient List<Comment> comments;

    public Passage(JSONObject passage, boolean full) throws Exception {
        try {
            content = ComplexStringLoader.LoadFromNet(passage.getJSONObject("body"));
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
                    this.comments.add(new Comment(comments.getJSONObject(i), subComments.getJSONArray(i)));
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
    public void cache(FileOutputStream stream) throws Exception {
        if (id == 0) {
            return;
        }
        content.parseToServerFormat();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream);
        objectOutputStream.writeObject(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Comment> GetCommentList() {

        List<Comment> commentList = this.comments;
        commentList.sort(new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                if (o1.likeNumber() <= o2.likeNumber()) {
                    return 1;
                } else {
                    return -1;
                }

            }
        });
        return commentList;
    }

    public ComplexString GetContent() {
        return this.content;
    }

    public void AddComment(Comment comment) {
        this.comments.add(comment);

    }

}
