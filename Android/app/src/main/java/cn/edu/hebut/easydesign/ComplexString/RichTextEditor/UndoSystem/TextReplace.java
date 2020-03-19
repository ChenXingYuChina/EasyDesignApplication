package cn.edu.hebut.easydesign.ComplexString.RichTextEditor.UndoSystem;

import android.text.Editable;

public class TextReplace implements TextChangeAction {
    private ReplaceHelper helper;

    /*
    * for method {@link Editable#replace(int, int, CharSequence)}
     */
    TextReplace(Editable old, int st, int en, CharSequence text) {
        helper = new ReplaceHelper(st, text, old.subSequence(st, en));
    }

    @Override
    public void redo(Editable text) {
        text.replace(helper.sourceStart, helper.oldText.length() + helper.sourceStart, helper.newText);
    }

    @Override
    public void undo(Editable text) {
        text.replace(helper.sourceStart, helper.newText.length() + helper.sourceStart, helper.oldText);
    }
}
