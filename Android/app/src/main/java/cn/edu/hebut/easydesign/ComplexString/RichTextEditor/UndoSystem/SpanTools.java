package cn.edu.hebut.easydesign.ComplexString.RichTextEditor.UndoSystem;

import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import java.lang.reflect.Method;

class SpanTools {
    static boolean checkSpanSame(Object one, Object other) {
        if (!one.getClass().isAssignableFrom(other.getClass())) {
            return false;
        }
        if (one instanceof RelativeSizeSpan ) {
            return ((RelativeSizeSpan) one).getSizeChange() == ((RelativeSizeSpan) other).getSizeChange();
        } else if (one instanceof UnderlineSpan||one instanceof SuperscriptSpan||one instanceof SubscriptSpan || one instanceof StrikethroughSpan) {
            return true;
        } else if (one instanceof ForegroundColorSpan) {
            return ((ForegroundColorSpan) one).getForegroundColor() == ((ForegroundColorSpan) other).getForegroundColor();
        } else if (one instanceof BackgroundColorSpan) {
            return ((BackgroundColorSpan) one).getBackgroundColor() == ((BackgroundColorSpan) other).getBackgroundColor();
        }
        return one == other;
    }
    static boolean checkReplace(Object one, Object other) {
        return one.getClass().isAssignableFrom(other.getClass());
    }

    static Object copySpan(Object one) {
        try {
            if (one instanceof RelativeSizeSpan) {
                return one.getClass().getConstructor(float.class).newInstance(((RelativeSizeSpan) one).getSizeChange());
            } else if (one instanceof UnderlineSpan || one instanceof SuperscriptSpan || one instanceof SubscriptSpan || one instanceof StrikethroughSpan) {
                return one.getClass().getConstructor().newInstance();
            } else if (one instanceof ForegroundColorSpan) {
                return one.getClass().getConstructor(int.class).newInstance(((ForegroundColorSpan) one).getForegroundColor());
            } else if (one instanceof BackgroundColorSpan) {
                return one.getClass().getConstructor(int.class).newInstance(((BackgroundColorSpan) one).getBackgroundColor());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
