package cn.edu.hebut.easydesign.ComplexString.RichTextEditor.UndoSystem;

import android.text.Editable;
import android.util.Log;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public abstract class SpanActionBase implements TextChangeAction{
    private List<SpanHelper> add = new LinkedList<>();
    private List<SpanHelper> delete = new LinkedList<>();
    // for action
    protected SpanActionBase(Editable text, Object span, int start, int end) {
        forAdd(add, delete, text, span, start, end);
        makeup(text, add, delete);
    }

    //for unAction
    protected  SpanActionBase(Editable text, int start, int end, Class spanType) {
        forDelete(add, delete, text, start, end, spanType);
        makeup(text, add, delete);
    }

    protected void action(Editable text) {
        makeup(text, add, delete);
    }

    protected void unAction(Editable text) {
        makeup(text, delete, add);
    }

    private void makeup(Editable text, List<SpanHelper> add, List<SpanHelper> delete) {
        Log.i(EditableProxy.TAG, "makeup: " + add.size() + delete.size());
        for (SpanHelper span : delete) {
            text.removeSpan(span.span);
        }
        for (SpanHelper span : add) {
            span.setToEditable(text);
        }
    }

    protected abstract void forDelete(List<SpanHelper> outAdd, List<SpanHelper> outDelete, Editable text, int start, int end, Class spanType);

    protected abstract void forAdd(List<SpanHelper> outAdd, List<SpanHelper> outDelete, Editable text, Object span, int start, int end);

    public void getAdd() {

    }

    public void getDelete() {

    }
}
