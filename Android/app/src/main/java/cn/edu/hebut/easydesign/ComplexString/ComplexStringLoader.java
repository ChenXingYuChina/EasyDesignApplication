package cn.edu.hebut.easydesign.ComplexString;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import cn.edu.hebut.easydesign.Tools.ResourcesTools;

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
        ComplexString goal = new ComplexString(string);
        int length = positions.length();
        Condition<Boolean> condition = new Condition<>(false);
        for (int i = 0; i < length; i++) {
            int start = positions.getInt(i);
            int end = start + widths.getInt(i);
            AddSpan(condition, goal, resources.getJSONObject(i), start, end);
        }
        goal.cancelLoadImage = condition;
        return goal;
    }

    private void AddSpan(Condition<Boolean> c, final ComplexString string, JSONObject object, final int start, final int end) throws Exception {
        String url;
        try {
            url = object.getString("url");
        } catch (JSONException e) {
            url = null;
        }
        AddSpan(c, string, object.getLong("id"), url, start, end);
    }


    private static final int maxWidth = ResourcesTools.dp2px(300);
    private static final int maxHeight = ResourcesTools.dp2px(700);

    private void AddSpan(final Condition<Boolean> c, final ComplexString complexString, long id, String url, final int start, final int end) {
        final SpannableString string = complexString.GetSpannableString();
        if (id >= ComplexString.IMAGE) {
            ContextHolder.getBinder().PutTask(new ImageHostLoadTask(id, c){
                @Override
                protected long getId() {
                    return 0;
                }

                @Override
                protected void setImage(Bitmap bitmap) {
                    Context ctx = ContextHolder.getContext();
                    Log.i("CS", "the context: " + ctx);
                    if (ctx == null) {
                        return;
                    }
                    int oWith = bitmap.getWidth();
                    int oHeight = bitmap.getHeight();
                    int height = oHeight;
                    int width = oWith;
                    if (width > maxWidth) {
                        height = height * maxWidth / width;
                        width = maxWidth;
                    }
                    if (width != oWith) {
                        Matrix translate = new Matrix();
                        translate.preScale(((float) width) / oWith, ((float) height) / oHeight);
//                        Log.i("cs", width + " " +height);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, oWith, oHeight, translate, false);
                    }
                    string.setSpan(new ImageSpan(ctx, bitmap), start, end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
                    complexString.refresh();
                    Log.i("CS", "start " + start + " size " + bitmap.getByteCount());
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
                AddSpan(condition, complexString, rid, complexString.urls.get(c), start, end);
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
