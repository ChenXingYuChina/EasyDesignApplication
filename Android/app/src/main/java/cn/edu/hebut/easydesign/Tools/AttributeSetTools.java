package cn.edu.hebut.easydesign.Tools;

import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.StringRes;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;

public class AttributeSetTools {
    public static String getStringFromAttrs(AttributeSet attrs, String key, @StringRes int defaultValue) {
        String attr = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", key);
        if (attr == null) {
//            Log.i("ATTR", ContextHolder.getContext() + " ");
            attr = ContextHolder.getContext().getResources().getString(defaultValue);
        } else if (attr.startsWith("@string/")) {
            attr = ContextHolder.getContext().getResources().getString(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", key, defaultValue));
        } else if (attr.matches("@[a-zA-Z]+/")) {
            attr = ContextHolder.getContext().getResources().getString(defaultValue);
        }
//        Log.i("ATTR", attr + " ");
        return attr;
    }
}
