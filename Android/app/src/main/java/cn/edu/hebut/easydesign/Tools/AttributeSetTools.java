package cn.edu.hebut.easydesign.Tools;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.StringRes;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;

public class AttributeSetTools {
    public static String getStringFromAttrs(Context context, AttributeSet attrs, String key, @StringRes int defaultValue) {
        String attr = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", key);
        if (attr == null) {
            Log.i("ATTR", attr + " ");
            attr = context.getResources().getString(defaultValue);
        } else if (attr.startsWith("@string/")) {
            Log.i("ATTR", attr + " ");
            attr = context.getResources().getString(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", key, defaultValue));
        } else if (attr.matches("@[a-zA-Z]+/")) {
            Log.i("ATTR", attr + " ");
            attr = context.getResources().getString(defaultValue);
        } else if (attr.matches("^@[0-9]+$")) {
            Log.i("ATTR", attr + " ");
            attr = context.getResources().getString(Integer.valueOf(attr.substring(1)));
        }
        Log.i("ATTR", attr + " ");
        return attr;
    }
}
