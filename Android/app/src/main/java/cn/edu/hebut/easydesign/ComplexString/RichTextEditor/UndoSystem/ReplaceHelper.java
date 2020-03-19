package cn.edu.hebut.easydesign.ComplexString.RichTextEditor.UndoSystem;

class ReplaceHelper {
    int sourceStart;
    CharSequence newText, oldText;

    public ReplaceHelper(int sourceStart, CharSequence newText, CharSequence oldText) {
        this.sourceStart = sourceStart;
        this.newText = newText;
        this.oldText = oldText;
    }
}
