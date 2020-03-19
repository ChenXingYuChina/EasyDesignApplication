package cn.edu.hebut.easydesign.ComplexString.RichTextEditor.UndoSystem;

import android.text.Editable;

interface TextChangeAction {
    void redo(Editable text);
    void undo(Editable text);
}
