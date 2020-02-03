package cn.edu.hebut.easydesign.ComplexString;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.ObjectInputStream;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Resources.Media.Image.ImageHostLoadTask;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

import static cn.edu.hebut.easydesign.ComplexString.ComplexString.HYPERLINK;

public class ComplexStringLoader {
    private static ComplexStringLoader instance = new ComplexStringLoader();

    public static ComplexStringLoader getInstance() {
        return instance;
    }

    private ComplexStringLoader() {

    }

    public ComplexString LoadFromNet(JSONObject complexString) throws Exception {
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
        Condition<Boolean> condition = new Condition<>(false);
        for (int i = 0; i < length; i++) {
            int start = positions.getInt(i);
            int end = start + widths.getInt(i);
            AddSpan(condition, string, resources.getJSONObject(i), start, end);
        }
        ComplexString goal = new ComplexString(string);
        goal.cancelLoadImage = condition;
        return goal;
    }

    private void AddSpan(Condition<Boolean> c, final SpannableString string, JSONObject object, final int start, final int end) throws Exception {
        String url;
        try {
            url = object.getString("url");
        } catch (JSONException e) {
            url = null;
        }
        AddSpan(c, string, object.getLong("id"), url, start, end);
    }

    private void AddSpan(final Condition<Boolean> c, final SpannableString string, long id, String url, final int start, final int end) {
        if (id >= ComplexString.IMAGE) {
            ContextHolder.getBinder().PutTask(new ImageHostLoadTask(id, c){
                @Override
                protected long getId() {
                    return 0;
                }

                @Override
                protected void setImage(Bitmap bitmap) {
                    Context ctx = ContextHolder.getContext();
                    if (ctx == null) {
                        return;
                    }
                    string.setSpan(new ImageSpan(ctx, data2), start, end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            });
            return;
        }
        string.setSpan(ComplexString.getSpanFromId(id, url), start, end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    public ComplexString LoadFromCache(InputStream stream) throws Exception {
        ObjectInputStream objectInputStream = new ObjectInputStream(stream);
        ComplexString complexString = (ComplexString) objectInputStream.readObject();
        SpannableString goal = new SpannableString(complexString.content);
        int c = 0;
        Condition<Boolean> condition = new Condition<>(false);
        for (int i : complexString.position) {
            int start = complexString.position[i];
            int end = start + complexString.width[i];
            long rid = complexString.resourcesId[i];
            try {
                AddSpan(condition, complexString.GetSpannableString(), rid, complexString.urls.get(c), start, end);
                if (rid == HYPERLINK) c++;
            } catch (Exception e) {
                Log.i("PASS", "LoadComplexStringFromCache: pass resource's id: " + rid);
            }
        }
        complexString.content = goal;
        complexString.cancelLoadImage = condition;
        return complexString;
    }

}
