package cn.edu.hebut.easydesign.ComplexString;

import android.content.Context;
import android.net.Uri;
import android.os.IBinder;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import cn.edu.hebut.easydesign.Resources.Media.Image.ImageHostLoadTask;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

import static cn.edu.hebut.easydesign.ComplexString.ComplexString.HYPERLINK;

public class ComplexStringLoader {
    private static ComplexStringLoader instance = new ComplexStringLoader();

    public static ComplexStringLoader getInstance() {
        return instance;
    }

    private ComplexStringLoader() {

    }

    // exception which is not caught in any step means the making process failure
    public ComplexString LoadFromNet(Context ctx, IBinder binder, InputStream in) throws Exception {
        return LoadFromNet(ctx, binder, new JSONObject(new BufferedReader(new InputStreamReader(in)).readLine()));
    }

    public ComplexString LoadFromNet(Context ctx, IBinder binder, JSONObject complexString) throws Exception {
        SpannableString string = new SpannableString(complexString.getString("content"));
        JSONArray positions, widths, resources;
        positions = complexString.getJSONArray("position");
        try {
            widths = complexString.getJSONArray("width");
            resources = complexString.getJSONArray("resources");
        } catch (Exception e) {
            return new ComplexString(string);
        }
        int length = positions.length();
        for (int i = 0; i < length; i++) {
            int start = positions.getInt(i);
            int end = start + widths.getInt(i);
            AddSpan(ctx, string, resources.getJSONObject(i), (TaskService.MyBinder) binder, start, end);
        }
        return new ComplexString(string);
    }

    private void AddSpan(final Context ctx, final SpannableString string, JSONObject object, TaskService.MyBinder binder, final int start, final int end) throws Exception {
        String url;
        try {
            url = object.getString("url");
        } catch (JSONException e) {
            url = null;
        }
        AddSpan(ctx, string, binder, object.getLong("id"), url, start, end);
    }

    private void AddSpan(final Context ctx, final SpannableString string, TaskService.MyBinder binder, long id, String url, final int start, final int end) throws Exception {
        if (id >= ComplexString.IMAGE) {
            binder.PutTask(new ImageHostLoadTask(id){
                @Override
                protected long getId() {
                    return 0;
                }

                @Override
                protected void doOnMain() {
                    string.setSpan(new ImageSpan(ctx, data2), start, end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            });
            return;
        }
        string.setSpan(ComplexString.getSpanFromId(ctx, id, url), start, end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    public ComplexString LoadFromCache(Context ctx, IBinder binder, InputStream stream) throws Exception {
        ObjectInputStream objectInputStream = new ObjectInputStream(stream);
        ComplexString complexString = (ComplexString) objectInputStream.readObject();
        SpannableString goal = new SpannableString(complexString.content);
        int c = 0;
        for (int i : complexString.position) {
            int start = complexString.position[i];
            int end = start + complexString.width[i];
            long rid = complexString.resourcesId[i];
            try {
                AddSpan(ctx, complexString.GetSpannableString(), (TaskService.MyBinder) binder, rid, complexString.urls.get(c), start, end);
                if (rid == HYPERLINK) c++;
            } catch (Exception e) {
                Log.i("PASS", "LoadComplexStringFromCache: pass resource's id: " + rid);
            }
        }
        complexString.content = goal;
        return complexString;
    }

}
