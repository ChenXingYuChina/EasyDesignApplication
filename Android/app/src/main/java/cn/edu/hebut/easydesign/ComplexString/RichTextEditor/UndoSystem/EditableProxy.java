package cn.edu.hebut.easydesign.ComplexString.RichTextEditor.UndoSystem;

import android.text.Editable;
import android.text.SpannableStringBuilder;
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

public class EditableProxy extends SpannableStringBuilder {
    public static final String TAG = "proxy";
    private UndoManager undoManager = new UndoManager();

    public EditableProxy(Editable editable) {
        super(editable);
    }


    @Override
    public SpannableStringBuilder replace(int st, int en, CharSequence text) {
        Log.i(TAG, "replace: "+ st + " " + en + " " + text);
        undoManager.addAction(new TextReplace(this, st, en, text));
        return super.replace(st, en, text);
    }

    private boolean firstCall = true;
    @Override
    public void setSpan(Object what, int start, int end, int flags) {
        if (firstCall) {
            firstCall = false;
            Log.i(TAG, "setSpan: " + what + " " + start + " " + end + " " + flags);
//            undoManager.disable();
            if (what instanceof ImageSpan || what instanceof URLSpan) {
                undoManager.addAction(new ExclusiveSpanAction(this, what, start, end));
            } else if (what instanceof UnderlineSpan || what instanceof StrikethroughSpan || what instanceof SuperscriptSpan || what instanceof SubscriptSpan || what instanceof ForegroundColorSpan || what instanceof BackgroundColorSpan || what instanceof RelativeSizeSpan) {
                undoManager.addAction(new StyleSpanAction(this, what, start, end));
            } else {
                super.setSpan(what, start, end, flags);
            }
//            undoManager.enable();
            firstCall = true;
        } else {
            super.setSpan(what, start, end, flags);
        }
    }

    @Override
    public SpannableStringBuilder delete(int start, int end) {
        Log.i(TAG, "delete: " + start + " " + end);
        undoManager.addAction(new TextReplace(this, start, end, ""));
        return super.delete(start, end);
    }

    @Override
    public void clear() {
        super.clear();
        undoManager.clear();
    }

    @Override
    public void clearSpans() {
        super.clearSpans();
        undoManager.clear();
    }




    public void undo() {
        undoManager.undo(this);
    }

    public void redo() {
        undoManager.redo(this);
    }

    public void removeSpan(Class type, int start, int end) {
        firstCall = false;
        undoManager.addAction(new StyleSpanAction(this, type, start, end));
        firstCall = true;
    }
}
