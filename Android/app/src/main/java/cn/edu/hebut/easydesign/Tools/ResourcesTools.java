package cn.edu.hebut.easydesign.Tools;

import android.content.Context;

import androidx.annotation.AnyRes;
import androidx.annotation.Px;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;

public class ResourcesTools {


    public static final String RES_ID = "id";
    public static final String RES_STRING = "string";
    public static final String RES_DRAWABLE = "drawable";
    public static final String RES_LAYOUT = "layout";
    public static final String RES_STYLE = "style";
    public static final String RES_COLOR = "color";
    public static final String RES_DIMEN = "dimen";
    public static final String RES_ANIM = "anim";
    public static final String RES_MENU = "menu";

    @AnyRes
    public static int getResFromString(String name, String type) {
        Context context = ContextHolder.getContext();
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }

    public static int px2dp(@Px int value) {
        final float scale = ContextHolder.getContext().getResources().getDisplayMetrics().density;
        return (int) (value / scale + 0.5f);
    }

    public static int dp2px(float value) {
        final float scale = ContextHolder.getContext().getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }
}
