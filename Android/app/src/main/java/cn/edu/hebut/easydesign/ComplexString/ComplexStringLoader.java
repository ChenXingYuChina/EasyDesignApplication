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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.edu.hebut.easydesign.R;

public class ComplexStringLoader {
    private String ImageUrl;
    private static ComplexStringLoader instance = new ComplexStringLoader();
    private ComplexStringLoader() {
        ImageUrl = "localhost:80/getImage?id=";
    }
    public static ComplexStringLoader GetInstant() {
        return instance;
    }

    // exception which is not caught in any step means the making process failure
    public SpannableString MakeComplexString(Context ctx, InputStream in) throws Exception {
        return MakeComplexString(ctx, (new BufferedReader(new InputStreamReader(in))).readLine());
    }

    public SpannableString MakeComplexString(Context ctx, String json) throws Exception {
        JSONObject complexString = new JSONObject(json);
        SpannableString goal = new SpannableString(complexString.getString("content"));
        JSONArray positions, widths, resources;
        positions = complexString.getJSONArray("position");
        try {
            widths = complexString.getJSONArray("width");
        } catch (Exception e) {
            return goal;
        }
        resources = complexString.getJSONArray("resources");
        int length = positions.length();
        for (int i = 0; i < length; i++) {
            int start = positions.getInt(i);
            int end = start + widths.getInt(i);
            try {
                goal.setSpan(getSpanFromId(ctx, resources.getJSONObject(i)), start, end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                Log.i("PASS", "MakeComplexString: pass resource looks like" + resources.get(i).toString());
            }
        }
        return goal;
    }

    public static final int UNDERLINE = 0;
    public static final int STRIKE_THROUGH = 1;
    public static final int SUPERSCRIPT = 2;
    public static final int SUBSCRIPT = 3;
    public static final int FONT_SIZE_BASE = 4;
    public static final int TEXT_COLOR_BASE = FONT_SIZE_BASE + 3;
    public static final int BACKGROUND_BASE = TEXT_COLOR_BASE + 5;
    public static final int HYPERLINK = BACKGROUND_BASE + 5;
    public static final int IMAGE = HYPERLINK + 1;
    public static final int SMALL_FONT_SIZE = 0;
    public static final int BIG_FONT_SIZE = 1;
    public static final int HUGE_FONT_SIZE = 2;
    public static final int RED_COLOR = 0;
    public static final int BLUE_COLOR = 1;
    public static final int YELLOW_COLOR = 2;
    public static final int GREEN_COLOR = 3;
    public static final int PURPLE_COLOR = 4;

    private Object getSpanFromId(Context ctx, JSONObject object) throws Exception {
        return getSpanFromId(ctx, object.getInt("id"), object.getString("url"));
    }

    public Object getSpanFromId(Context ctx, int id, String url) {
        switch (id) {
            case HYPERLINK:
                return new URLSpan(url);
            case UNDERLINE:
                return new UnderlineSpan();
            case FONT_SIZE_BASE + SMALL_FONT_SIZE:
                return new RelativeSizeSpan(0.5f);
            case FONT_SIZE_BASE + BIG_FONT_SIZE:
                return new RelativeSizeSpan(1.5f);
            case FONT_SIZE_BASE + HUGE_FONT_SIZE:
                return new RelativeSizeSpan(2.0f);
            case TEXT_COLOR_BASE + RED_COLOR:
                return new ForegroundColorSpan(ctx.getResources().getColor(R.color.redForComplexString));
            case TEXT_COLOR_BASE + BLUE_COLOR:
                return new ForegroundColorSpan(ctx.getResources().getColor(R.color.blueForComplexString));
            case TEXT_COLOR_BASE + YELLOW_COLOR:
                return new ForegroundColorSpan(ctx.getResources().getColor(R.color.yellowForComplexString));
            case TEXT_COLOR_BASE + GREEN_COLOR:
                return new ForegroundColorSpan(ctx.getResources().getColor(R.color.greenForComplexString));
            case TEXT_COLOR_BASE + PURPLE_COLOR:
                return new ForegroundColorSpan(ctx.getResources().getColor(R.color.purpleForComplexString));
            case BACKGROUND_BASE + RED_COLOR:
                return new BackgroundColorSpan(ctx.getResources().getColor(R.color.redForComplexString));
            case BACKGROUND_BASE + BLUE_COLOR:
                return new BackgroundColorSpan(ctx.getResources().getColor(R.color.blueForComplexString));
            case BACKGROUND_BASE + YELLOW_COLOR:
                return new BackgroundColorSpan(ctx.getResources().getColor(R.color.yellowForComplexString));
            case BACKGROUND_BASE + GREEN_COLOR:
                return new BackgroundColorSpan(ctx.getResources().getColor(R.color.greenForComplexString));
            case BACKGROUND_BASE + PURPLE_COLOR:
                return new BackgroundColorSpan(ctx.getResources().getColor(R.color.purpleForComplexString));
            case STRIKE_THROUGH:
                return new StrikethroughSpan();
            case SUPERSCRIPT:
                return new SuperscriptSpan();
            case SUBSCRIPT:
                return new SubscriptSpan();
        }
        return new ImageSpan(ctx, Uri.parse(ImageUrl + id));
    }
}
