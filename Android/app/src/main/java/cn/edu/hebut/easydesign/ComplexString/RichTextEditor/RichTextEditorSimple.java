package cn.edu.hebut.easydesign.ComplexString.RichTextEditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

import androidx.appcompat.R;
import androidx.appcompat.widget.AppCompatEditText;
import cn.edu.hebut.easydesign.ComplexString.ComplexString;

public class RichTextEditorSimple extends AppCompatEditText {
    private ComplexStringProxy text;
    public RichTextEditorSimple(Context context) {
        this(context, null);
    }

    public RichTextEditorSimple(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public RichTextEditorSimple(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setEditableFactory(new ComplexStringProxyFactory());
        setText(new SpannableStringBuilder(), BufferType.EDITABLE);
        text = (ComplexStringProxy) getText();
    }

    public void undo(){
        text.undo();
    }
    public void redo() {
        text.redo();
    }

    public void addSpan(int id, String url) {
        text.addSpanById(id, url, getSelectionStart(), getSelectionEnd());
    }

    public void addImageSpan(Bitmap pic, String path) {
        text.addImageSpan(getSelectionStart(), getSelectionEnd(), pic, path);
    }

    public void delSpan(Class spanType) {
        text.removeSpan(spanType, getSelectionStart(), getSelectionEnd());
    }

    public ComplexString toComplexString() {
        return text.toComplexString();
    }
    public void clear() {
        text.clear();
        text.clearSpans();
    }
}
