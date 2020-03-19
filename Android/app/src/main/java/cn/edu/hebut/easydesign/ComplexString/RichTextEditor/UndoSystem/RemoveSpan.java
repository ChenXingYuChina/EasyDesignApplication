package cn.edu.hebut.easydesign.ComplexString.RichTextEditor.UndoSystem;

import android.text.Editable;
import android.text.Spanned;

public class RemoveSpan implements TextChangeAction {
    SpanHelper helper;

    RemoveSpan(Editable source, Object what) {
        helper = new SpanHelper(what, source.getSpanStart(what), source.getSpanEnd(what));
    }
    @Override
    public void redo(Editable text) {
        text.setSpan(helper.span, helper.start, helper.end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    @Override
    public void undo(Editable text) {
        text.removeSpan(helper.span);
    }
}
