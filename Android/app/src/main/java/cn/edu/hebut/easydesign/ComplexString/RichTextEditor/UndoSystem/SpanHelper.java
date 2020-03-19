package cn.edu.hebut.easydesign.ComplexString.RichTextEditor.UndoSystem;

import android.text.Editable;
import android.text.Spanned;

class SpanHelper {
    Object span;
    int start, end;

    SpanHelper(Object span, int start, int end) {
        this.span = span;
        this.start = start;
        this.end = end;
    }

    void setToEditable(Editable text) {
        text.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }
}
