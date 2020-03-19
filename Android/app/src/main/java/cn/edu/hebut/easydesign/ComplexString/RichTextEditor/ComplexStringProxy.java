package cn.edu.hebut.easydesign.ComplexString.RichTextEditor;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.ComplexString.RichTextEditor.UndoSystem.EditableProxy;

import android.graphics.Bitmap;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.util.Log;

public class ComplexStringProxy extends EditableProxy{
    private ComplexString complexString;
    ComplexStringProxy(Editable text) {
        super(text);
        Log.i(EditableProxy.TAG, "ComplexStringProxy: " + text.getClass() + text);
        complexString = new ComplexString(this);
    }

    void addImageSpan(int start, int width, Bitmap bitmap, String path) {
        complexString.addImageSpan(start, width, bitmap, path);
    }

    void addSpanById(int id, String url, int start, int end) {
        complexString.addSpan(id, start, end, url);
    }

    ComplexString toComplexString() {
        return complexString;
    }
}
