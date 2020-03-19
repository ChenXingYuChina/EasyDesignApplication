package cn.edu.hebut.easydesign.ComplexString.RichTextEditor;

import android.text.Editable;
import android.text.SpannableStringBuilder;


public class ComplexStringProxyFactory extends Editable.Factory {
    @Override
    public Editable newEditable(CharSequence source) {
        return new ComplexStringProxy(new SpannableStringBuilder(source));
//        return new SpannableStringBuilder(source);
    }
}
