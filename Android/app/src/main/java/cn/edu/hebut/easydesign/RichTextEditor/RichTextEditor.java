package cn.edu.hebut.easydesign.RichTextEditor;

import android.content.Context;
import android.text.*;
import android.text.style.*;
import android.util.AttributeSet;
import android.util.Log;
import androidx.appcompat.widget.AppCompatEditText;
import cn.edu.hebut.easydesign.ComplexString.ComplexString;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 0.1
 * 这是一个富文本编辑器控件，实现了ComplexString中规定的样式
 * 具体的使用例子详见：Android/app/src/main/java/cn/edu/hebut/easydesign/Activity/RichEditorTest.java
 * 还未完成的工作：ImageSpan，撤销功能
 */

public class RichTextEditor extends AppCompatEditText implements TextWatcher {
    private static final String TAG = "Rich Text Editor";
    // 以下部分是将ComplexString中规定的样式整理合并，并设置为静态
    public static final int SMALL_FONT = ComplexString.FONT_SIZE_BASE + ComplexString.SMALL_FONT_SIZE;
    public static final int NORMAL_FONT = ComplexString.FONT_SIZE_BASE + ComplexString.BIG_FONT_SIZE;
    public static final int HUGE_FONT = ComplexString.FONT_SIZE_BASE + ComplexString.HUGE_FONT_SIZE;
    public static final int TEXT_FOREGROUND_COLOR_RED = ComplexString.TEXT_COLOR_BASE + ComplexString.RED_COLOR;
    public static final int TEXT_FOREGROUND_COLOR_BLUE = ComplexString.TEXT_COLOR_BASE + ComplexString.BLUE_COLOR;
    public static final int TEXT_FOREGROUND_COLOR_YELLOW = ComplexString.TEXT_COLOR_BASE + ComplexString.YELLOW_COLOR;
    public static final int TEXT_FOREGROUND_COLOR_GREEN = ComplexString.TEXT_COLOR_BASE + ComplexString.GREEN_COLOR;
    public static final int TEXT_FOREGROUND_COLOR_PURPLE = ComplexString.TEXT_COLOR_BASE + ComplexString.PURPLE_COLOR;
    public static final int TEXT_BACKGROUND_COLOR_RED = ComplexString.BACKGROUND_BASE + ComplexString.RED_COLOR;
    public static final int TEXT_BACKGROUND_COLOR_BLUE = ComplexString.BACKGROUND_BASE + ComplexString.BLUE_COLOR;
    public static final int TEXT_BACKGROUND_COLOR_GREEN = ComplexString.BACKGROUND_BASE + ComplexString.GREEN_COLOR;
    public static final int TEXT_BACKGROUND_COLOR_PURPLE = ComplexString.BACKGROUND_BASE + ComplexString.PURPLE_COLOR;
    public static final int TEXT_BACKGROUND_COLOR_YELLOW = ComplexString.BACKGROUND_BASE + ComplexString.YELLOW_COLOR;

    // 这是撤回操作所需要的变量，截至2020/2/22 该功能尚未完成，以后很快会更新
    private List<Editable> historyList = new ArrayList<>();
    private boolean historyWorking = false;
    private int historyCursor = 0;

    private SpannableStringBuilder inputBefore;
    private Editable inputLast;

    // 构造器，全部默认继承父类
    public RichTextEditor(Context context) {
        super(context);
    }

    public RichTextEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RichTextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * This is called when the view is detached from a window.  At this point it
     * no longer has a surface for drawing.
     *
     * @see #onAttachedToWindow()
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeTextChangedListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // addTextChangeListener是TextView的一个方法
        addTextChangedListener(this);
    }

    /**
     * Common ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * Common样式包括 下划线，上角标，下角标，删除线
     */

    /**
     *
     * @param styleId ComplexString规定的样式，只有下划线，上角标，下角标，删除线被接受
     * @param ifAble true的时候启用样式，false的时候删除样式
     */
    public void commonFontStyle(int styleId, boolean ifAble) {
//        Log.d(TAG, "commonFontStyle: Being Click");
//        Log.d(TAG, "commonFontStyle: styleAble=" + !ifAble);
        if (ifAble) {
            styleEnable(styleId, getSelectionStart(), getSelectionEnd());
        } else {
            styleDisable(styleId, getSelectionStart(), getSelectionEnd());
        }
    }

    /**
     * 该方法是 commonFontStyle 方法的辅助，用来启用传入的样式
     * @param styleId ComplexString规定的样式，只有下划线，上角标，下角标，删除线被接受
     * @param selectionStart 字符串的开始
     * @param selectionEnd 字符串的结束
     */

    private void styleEnable(int styleId, int selectionStart, int selectionEnd) {
//        Log.d(TAG, "styleEnable: Being Called");
//        Log.d(TAG, "styleEnable: styleId=" + styleId +
//                " selectionStart=" + selectionStart + " selectionEnd=" + selectionEnd);
        switch (styleId) {
            case ComplexString.UNDERLINE:
            case ComplexString.SUPERSCRIPT:
            case ComplexString.SUBSCRIPT:
            case ComplexString.STRIKE_THROUGH:
                break;
            default:
                return;
        }

        if (selectionEnd <= selectionStart) {
            return;
        }
        Log.d(TAG, "styleEnable: span=" + ComplexString.getSpanFromId(styleId, null));
        getEditableText().setSpan(ComplexString.getSpanFromId(styleId, null),
                selectionStart, selectionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 该方法是 commonFontStyle 方法的辅助，用来删除传入的样式
     * @param styleId ComplexString规定的样式，只有下划线，上角标，下角标，删除线被接受
     * @param selectionStart 字符串的开始
     * @param selectionEnd 字符串的结束
     */
    private void styleDisable(int styleId, int selectionStart, int selectionEnd) {
        switch (styleId) {
            case ComplexString.UNDERLINE:
                DeleteUnderline(selectionStart, selectionEnd);
                break;
            case ComplexString.STRIKE_THROUGH:
                DeleteStrikeThrough(selectionStart, selectionEnd);
            case ComplexString.SUBSCRIPT:
                DeleteSubscript(selectionStart, selectionEnd);
            case ComplexString.SUPERSCRIPT:
                DeleteSuperscript(selectionStart, selectionEnd);
        }
    }

    private void DeleteSuperscript(int selectionStart, int selectionEnd) {
        if (selectionStart >= selectionEnd) {
            return;
        }
        SuperscriptSpan[] spans = getEditableText().getSpans(selectionStart, selectionEnd, SuperscriptSpan.class);
        List<RichTextEditorHelper> list = new ArrayList<>();

        for (SuperscriptSpan span : spans) {
            list.add(new RichTextEditorHelper(getEditableText().getSpanStart(span), getEditableText().getSpanEnd(span)));
            getEditableText().removeSpan(span);
        }

        for (RichTextEditorHelper helper : list) {
            if (helper.isValid()) {
                if (helper.getStart() < selectionStart) {
                    styleEnable(ComplexString.SUPERSCRIPT, helper.getStart(), selectionStart);
                }

                if (helper.getEnd() > selectionEnd) {
                    styleEnable(ComplexString.SUPERSCRIPT, selectionEnd, helper.getEnd());
                }
            }
        }
    }

    private void DeleteSubscript(int selectionStart, int selectionEnd) {
        if (selectionStart >= selectionEnd) {
            return;
        }
        SubscriptSpan[] spans = getEditableText().getSpans(selectionStart, selectionEnd, SubscriptSpan.class);
        List<RichTextEditorHelper> list = new ArrayList<>();

        for (SubscriptSpan span : spans) {
            list.add(new RichTextEditorHelper(getEditableText().getSpanStart(span), getEditableText().getSpanEnd(span)));
            getEditableText().removeSpan(span);
        }

        for (RichTextEditorHelper helper : list) {
            if (helper.isValid()) {
                if (helper.getStart() < selectionStart) {
                    styleEnable(ComplexString.SUBSCRIPT, helper.getStart(), selectionStart);
                }

                if (helper.getEnd() > selectionEnd) {
                    styleEnable(ComplexString.SUBSCRIPT, selectionEnd, helper.getEnd());
                }
            }
        }
    }

    private void DeleteStrikeThrough(int selectionStart, int selectionEnd) {
        if (selectionStart >= selectionEnd) {
            return;
        }
        StrikethroughSpan[] spans = getEditableText().getSpans(selectionStart, selectionEnd, StrikethroughSpan.class);
        List<RichTextEditorHelper> list = new ArrayList<>();

        for (StrikethroughSpan span : spans) {
            list.add(new RichTextEditorHelper(getEditableText().getSpanStart(span), getEditableText().getSpanEnd(span)));
            getEditableText().removeSpan(span);
        }

        for (RichTextEditorHelper helper : list) {
            if (helper.isValid()) {
                if (helper.getStart() < selectionStart) {
                    styleEnable(ComplexString.STRIKE_THROUGH, helper.getStart(), selectionStart);
                }

                if (helper.getEnd() > selectionEnd) {
                    styleEnable(ComplexString.STRIKE_THROUGH, selectionEnd, helper.getEnd());
                }
            }
        }
    }

    private void DeleteUnderline(int selectionStart, int selectionEnd) {
        if (selectionStart >= selectionEnd) {
            return;
        }
        UnderlineSpan[] spans = getEditableText().getSpans(selectionStart, selectionEnd, UnderlineSpan.class);
        List<RichTextEditorHelper> list = new ArrayList<>();

        for (UnderlineSpan span : spans) {
            list.add(new RichTextEditorHelper(getEditableText().getSpanStart(span), getEditableText().getSpanEnd(span)));
            getEditableText().removeSpan(span);
        }

        for (RichTextEditorHelper helper : list) {
            if (helper.isValid()) {
                if (helper.getStart() < selectionStart) {
                    styleEnable(ComplexString.UNDERLINE, helper.getStart(), selectionStart);
                }

                if (helper.getEnd() > selectionEnd) {
                    styleEnable(ComplexString.UNDERLINE, selectionEnd, helper.getEnd());
                }
            }
        }
    }

    /**
     * 简化了输入，将检测范围默认为用户所选
     * @param styleId ComplexString规定的样式，只有下划线，上角标，下角标，删除线被接受
     * @return boolean 是否含有规定的样式
     */
    public boolean containCommonStyle(int styleId) {
        return containCommonStyle(styleId, getSelectionStart(), getSelectionEnd());
    }

    /**
     * 该方法用来检测规定的文本中有没有传入的样式
     * @param styleId ComplexString规定的样式，只有下划线，上角标，下角标，删除线被接受
     * @param start 规定检测范围的开始
     * @param end 规定检测范围的结束
     * @return boolean 是否含有规定的样式
     *
     */
    public boolean containCommonStyle(int styleId, int start, int end) {
        switch (styleId) {
            case ComplexString.UNDERLINE:
                return containUnderline(start, end);
            case ComplexString.STRIKE_THROUGH:
                return containStrikeThrough(start, end);
            case ComplexString.SUBSCRIPT:
                return containSubscript(start, end);
            case ComplexString.SUPERSCRIPT:
                return containSuperscript(start, end);
            default:
                return false;
        }
    }

    private boolean containUnderline(int start, int end) {
        if (start > end) {
            return false;
        }

        if (start == end) {
            if (start - 1 < 0 || start + 1 > getEditableText().length()) {
                return false;
            } else {
                UnderlineSpan[] before = getEditableText().getSpans(start - 1, start, UnderlineSpan.class);
                UnderlineSpan[] after = getEditableText().getSpans(start, start + 1, UnderlineSpan.class);
                return before.length > 0 && after.length > 0;
            }
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = start; i < end; i++) {
                if (getEditableText().getSpans(i, i + 1, UnderlineSpan.class).length > 0) {
                    builder.append(getEditableText().subSequence(i, i + 1).toString());
                }
            }
            return getEditableText().subSequence(start, end).toString().equals(builder.toString());
        }
    }

    private boolean containStrikeThrough(int start, int end) {
        if (start > end) {
            return false;
        }

        if (start == end) {
            if (start - 1 < 0 || start + 1 > getEditableText().length()) {
                return false;
            } else {
                StrikethroughSpan[] before = getEditableText().getSpans(start - 1, start, StrikethroughSpan.class);
                StrikethroughSpan[] after = getEditableText().getSpans(start, start + 1, StrikethroughSpan.class);
                return before.length > 0 && after.length > 0;
            }
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = start; i < end; i++) {
                if (getEditableText().getSpans(i, i + 1, StrikethroughSpan.class).length > 0) {
                    builder.append(getEditableText().subSequence(i, i + 1).toString());
                }
            }
            return getEditableText().subSequence(start, end).toString().equals(builder.toString());
        }
    }

    private boolean containSuperscript(int start, int end) {
        if (start > end) {
            return false;
        }

        if (start == end) {
            if (start - 1 < 0 || start + 1 > getEditableText().length()) {
                return false;
            } else {
                SuperscriptSpan[] before = getEditableText().getSpans(start - 1, start, SuperscriptSpan.class);
                SuperscriptSpan[] after = getEditableText().getSpans(start, start + 1, SuperscriptSpan.class);
                return before.length > 0 && after.length > 0;
            }
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = start; i < end; i++) {
                if (getEditableText().getSpans(i, i + 1, SuperscriptSpan.class).length > 0) {
                    builder.append(getEditableText().subSequence(i, i + 1).toString());
                }
            }
            return getEditableText().subSequence(start, end).toString().equals(builder.toString());
        }
    }

    private boolean containSubscript(int start, int end) {
        if (start > end) {
            return false;
        }

        if (start == end) {
            if (start - 1 < 0 || start + 1 > getEditableText().length()) {
                return false;
            } else {
                SubscriptSpan[] before = getEditableText().getSpans(start - 1, start, SubscriptSpan.class);
                SubscriptSpan[] after = getEditableText().getSpans(start, start + 1, SubscriptSpan.class);
                return before.length > 0 && after.length > 0;
            }
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = start; i < end; i++) {
                if (getEditableText().getSpans(i, i + 1, SubscriptSpan.class).length > 0) {
                    builder.append(getEditableText().subSequence(i, i + 1).toString());
                }
            }
            return getEditableText().subSequence(start, end).toString().equals(builder.toString());
        }
    }


    //BackGround color++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * 确定的文字背景颜色的样式
     * @param colorId ComplexString中规定的文字背景颜色样式
     * @param ifAble true：设置样式，false：取消样式
     */
    public void backgroundColor(int colorId, boolean ifAble) {
        switch (colorId) {
            case TEXT_BACKGROUND_COLOR_RED:
            case TEXT_BACKGROUND_COLOR_BLUE:
            case TEXT_BACKGROUND_COLOR_PURPLE:
            case TEXT_BACKGROUND_COLOR_GREEN:
            case TEXT_BACKGROUND_COLOR_YELLOW:
                break;
            default:
                return;
        }
        if (ifAble) {
            backgroundColorEnable(colorId, getSelectionStart(), getSelectionEnd());
        } else {
            backgroundColorDisable(colorId, getSelectionStart(), getSelectionEnd());
        }
    }

    private void backgroundColorDisable(int colorId, int selectionStart, int selectionEnd) {
        if (selectionStart >= selectionEnd) {
            return;
        }

        BackgroundColorSpan[] spans = getEditableText().getSpans(selectionStart, selectionEnd, BackgroundColorSpan.class);
        List<RichTextEditorHelper> list = new ArrayList<>();

        for (BackgroundColorSpan span : spans) {
            list.add(new RichTextEditorHelper(getEditableText().getSpanStart(span), getEditableText().getSpanEnd(span)));
            getEditableText().removeSpan(span);
        }

        for (RichTextEditorHelper helper : list) {
            if (helper.getStart() < selectionStart) {
                getEditableText().setSpan(ComplexString.getSpanFromId(colorId, null),
                        helper.getStart(), selectionStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (helper.getEnd() > selectionEnd) {
                getEditableText().setSpan(ComplexString.getSpanFromId(colorId, null),
                        helper.getStart(), selectionStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

    }

    private void backgroundColorEnable(int colorId, int selectionStart, int selectionEnd) {

        if (selectionStart >= selectionEnd) {
            return;
        }
        getEditableText().setSpan(ComplexString.getSpanFromId(colorId, null),
                selectionStart, selectionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    /**
     * 简化了输入，将检测范围默认为用户所选
     * @param colorId ComplexString规定的样式，只有下划线，上角标，下角标，删除线被接受
     * @return boolean 是否含有规定的样式
     */
    public boolean containBackgroundColor(int colorId) {
        return containBackgroundColor(colorId, getSelectionStart(), getSelectionEnd());
    }
    /**
     * 该方法用来检测规定的文本中有没有传入的样式
     * @param colorId ComplexString规定的样式，只有相关的值被接受
     * @param start 规定检测范围的开始
     * @param end 规定检测范围的结束
     * @return boolean 是否含有规定的样式
     *
     */
    public boolean containBackgroundColor(int colorId, int start, int end) {
        switch (colorId) {
            case TEXT_BACKGROUND_COLOR_RED:
            case TEXT_BACKGROUND_COLOR_BLUE:
            case TEXT_BACKGROUND_COLOR_PURPLE:
            case TEXT_BACKGROUND_COLOR_GREEN:
                break;
            default:
                return false;
        }
        if (start >= end) {
            return false;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            if (getEditableText().getSpans(i, i + 1, BackgroundColorSpan.class).length > 0) {
                builder.append(getEditableText().subSequence(i, i + 1).toString());
            }
        }
        return builder.toString().equals(getEditableText().subSequence(start, end).toString());
    }

    // ForeGround Color +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /**
     * 确定的文字颜色的样式
     * @param colorId ComplexString中规定的文字颜色样式
     * @param ifAble true：设置样式，false：取消样式
     */
    public void foregroundColor(int colorId, boolean ifAble) {
        switch (colorId) {
            case TEXT_FOREGROUND_COLOR_RED:
            case TEXT_FOREGROUND_COLOR_BLUE:
            case TEXT_FOREGROUND_COLOR_PURPLE:
            case TEXT_FOREGROUND_COLOR_GREEN:
            case TEXT_FOREGROUND_COLOR_YELLOW:
                break;
            default:
                return;
        }

        if (ifAble) {
            foregroundColorEnable(colorId, getSelectionStart(), getSelectionEnd());
        } else {
            foregroundColorDisable(colorId, getSelectionStart(), getSelectionEnd());
        }
    }

    private void foregroundColorDisable(int colorId, int selectionStart, int selectionEnd) {
        if (selectionStart >= selectionEnd) {
            return;
        }

        ForegroundColorSpan[] spans = getEditableText().getSpans(selectionStart, selectionEnd, ForegroundColorSpan.class);
        List<RichTextEditorHelper> list = new ArrayList<>();

        for (ForegroundColorSpan span : spans) {
            list.add(new RichTextEditorHelper(getEditableText().getSpanStart(span), getEditableText().getSpanEnd(span)));
            getEditableText().removeSpan(span);
        }

        for (RichTextEditorHelper helper : list) {
            if (helper.getStart() < selectionStart) {
                getEditableText().setSpan(ComplexString.getSpanFromId(colorId, null),
                        helper.getStart(), selectionStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (helper.getEnd() > selectionEnd) {
                getEditableText().setSpan(ComplexString.getSpanFromId(colorId, null),
                        helper.getStart(), selectionStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

    }

    private void foregroundColorEnable(int colorId, int selectionStart, int selectionEnd) {
        if (selectionStart >= selectionEnd) {
            return;
        }
        getEditableText().setSpan(ComplexString.getSpanFromId(colorId, null),
                selectionStart, selectionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 该方法用来检测规定的文本中有没有传入的样式
     * @param colorId ComplexString规定的样式，只有相关的值被接受
     * @param start 规定检测范围的开始
     * @param end 规定检测范围的结束
     * @return boolean 是否含有规定的样式
     */
    private boolean containForegroundColor(int colorId, int start, int end) {
        switch (colorId) {
            case TEXT_FOREGROUND_COLOR_RED:
            case TEXT_FOREGROUND_COLOR_BLUE:
            case TEXT_FOREGROUND_COLOR_PURPLE:
            case TEXT_FOREGROUND_COLOR_GREEN:
                break;
            default:
                return false;
        }
        if (start >= end) {
            return false;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            if (getEditableText().getSpans(i, i + 1, ForegroundColorSpan.class).length > 0) {
                builder.append(getEditableText().subSequence(i, i + 1).toString());
            }
        }
        return builder.toString().equals(getEditableText().subSequence(start, end).toString());
    }

    // hyperlink ++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * 超链接样式
     * @param urlString 网址，不做规范检查
     */
    public void hyperlink(String urlString) {
        hyperlinkEnable(urlString, getSelectionStart(), getSelectionEnd());
    }

    private void hyperlinkEnable(String urlString, int selectionStart, int selectionEnd) {
        if (selectionStart >= selectionEnd) {
            return;
        }

        getEditableText().setSpan(ComplexString.getSpanFromId(ComplexString.HYPERLINK, urlString),
                selectionStart, selectionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    //font size +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * 设置字体大小，有三种选项
     * @param sizeId 文字大小， ComplexString中规定的文字大小
     * @param ifAble true：设置样式，false：取消样式
     */
    public void fontSize(int sizeId, boolean ifAble) {
        switch (sizeId) {
            case SMALL_FONT:
            case NORMAL_FONT:
            case HUGE_FONT:
                break;
            default:
                return;
        }
        if (ifAble) {
            fontSizeEnable(sizeId, getSelectionStart(), getSelectionEnd());
        } else {
            fontSizeDisable(sizeId, getSelectionStart(), getSelectionEnd());
        }
    }

    private void fontSizeDisable(int sizeId, int selectionStart, int selectionEnd) {
        if (selectionStart >= selectionEnd) {
            return;
        }

        RelativeSizeSpan[] spans = getEditableText().getSpans(selectionStart, selectionEnd, RelativeSizeSpan.class);
        List<RichTextEditorHelper> list = new ArrayList<>();

        for (RelativeSizeSpan span : spans) {
            list.add(new RichTextEditorHelper(getEditableText().getSpanStart(span), getEditableText().getSpanEnd(span)));
            getEditableText().removeSpan(span);
        }

        for (RichTextEditorHelper helper : list) {
            if (helper.getStart() < selectionStart) {
                getEditableText().setSpan(ComplexString.getSpanFromId(sizeId, null),
                        helper.getStart(), selectionStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (helper.getEnd() > selectionEnd) {
                getEditableText().setSpan(ComplexString.getSpanFromId(sizeId, null),
                        helper.getStart(), selectionStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

    }

    private void fontSizeEnable(int sizeId, int selectionStart, int selectionEnd) {
        if (selectionStart >= selectionEnd) {
            return;
        }
        getEditableText().setSpan(ComplexString.getSpanFromId(sizeId, null),
                selectionStart, selectionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 该方法用来检测规定的文本中有没有传入的样式
     * @return boolean 是否含有规定的样式
     */
    private boolean containFontSizeChange() {
        return containFontSizeChange(getSelectionStart(), getSelectionEnd());
    }

    private boolean containFontSizeChange(int start, int end) {
        if (start >= end) {
            return false;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            if (getEditableText().getSpans(i, i + 1, RelativeSizeSpan.class).length > 0) {
                builder.append(getEditableText().subSequence(i, i + 1).toString());
            }
        }
        return builder.toString().equals(getEditableText().subSequence(start, end).toString());
    }

    // 将全文转换为ComplexString的类型的变量

    /**
     *
     * @return 返回一个ComplexString对象
     */
    public ComplexString getComplexString() {
        SpannableString builder = new SpannableString(getEditableText());
//        Log.d(TAG, "getComplexString: " + builder);
        ComplexString complexString = new ComplexString(builder);
        return complexString;
    }

    //以下部分是做撤销用的
    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * are about to be replaced by new text with length <code>after</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * have just replaced old text that had length <code>before</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * This method is called to notify you that, somewhere within
     * <code>s</code>, the text has been changed.
     * It is legitimate to make further changes to <code>s</code> from
     * this callback, but be careful not to get yourself into an infinite
     * loop, because any changes you make will cause this method to be
     * called again recursively.
     * (You are not told where the change took place because other
     * afterTextChanged() methods may already have made other changes
     * and invalidated the offsets.  But if you need to know here,
     * you can use {@link Spannable#setSpan} in {@link #onTextChanged}
     * to mark your place and then look up from here where the span
     * ended up.
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {

    }
}
