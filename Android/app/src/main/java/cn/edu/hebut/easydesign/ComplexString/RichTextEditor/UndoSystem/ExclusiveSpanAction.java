package cn.edu.hebut.easydesign.ComplexString.RichTextEditor.UndoSystem;

import android.text.Editable;

import java.util.List;

public class ExclusiveSpanAction extends SpanActionBase implements TextChangeAction {

    protected ExclusiveSpanAction(Editable text, Object span, int start, int end) {
        super(text, span, start, end);
    }

    @Override
    public void redo(Editable text) {
        action(text);
    }

    @Override
    public void undo(Editable text) {
        unAction(text);
    }

    @Override
    protected void forDelete(List<SpanHelper> outAdd, List<SpanHelper> outDelete, Editable text, int start, int end, Class spanType) {
        throw new IllegalArgumentException("can not unAction this");
    }

    @Override
    protected void forAdd(List<SpanHelper> outAdd, List<SpanHelper> outDelete, Editable text, Object span, int start, int end) {
        for (Object other : text.getSpans(start, end, span.getClass())) {
            outDelete.add(new SpanHelper(other, start, end));
            text.removeSpan(other);
        }
        outAdd.add(new SpanHelper(span, start, end));
    }
}
