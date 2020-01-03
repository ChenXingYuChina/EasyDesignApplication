package cn.edu.hebut.easydesign.ComplexString;

import android.content.Context;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import cn.edu.hebut.easydesign.DataManagement.Data;
import cn.edu.hebut.easydesign.DataManagement.DataLoader;
import cn.edu.hebut.easydesign.DataManagement.DataManagement;
import cn.edu.hebut.easydesign.DataManagement.DataType;
import cn.edu.hebut.easydesign.R;

import static cn.edu.hebut.easydesign.ComplexString.ComplexString.getSpanFromId;

public class ComplexStringLoader implements DataLoader {
    public ComplexStringLoader() {

    }

    // exception which is not caught in any step means the making process failure
    public Data LoadFromNet(Context ctx, InputStream in, long id) throws Exception {
        JSONObject complexString = new JSONObject(new BufferedReader(new InputStreamReader(in)).readLine());
        SpannableString goal = new SpannableString(complexString.getString("content"));
        JSONArray positions, widths, resources;
        positions = complexString.getJSONArray("position");
        try {
            widths = complexString.getJSONArray("width");
        } catch (Exception e) {
            return new ComplexString(goal, id);
        }
        resources = complexString.getJSONArray("resources");
        int length = positions.length();
        for (int i = 0; i < length; i++) {
            int start = positions.getInt(i);
            int end = start + widths.getInt(i);
            try {
                goal.setSpan(getSpanFromJson(ctx, resources.getJSONObject(i)), start, end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                Log.i("PASS", "MakeComplexString: pass resource looks like" + resources.get(i).toString());
            }
        }
        return new ComplexString(goal, id);
    }

    private Object getSpanFromJson(Context ctx, JSONObject object) throws Exception {
        String url;
        try {
            url = object.getString("url");
        } catch (JSONException e) {
            url = null;
        }
        return ComplexString.getSpanFromId(ctx, object.getLong("id"), url);
    }

    @Override
    public Data LoadFromCache(Context ctx, InputStream stream, long id) throws Exception {
        ObjectInputStream objectInputStream = new ObjectInputStream(stream);
        return (Data) objectInputStream.readObject();
    }
}
