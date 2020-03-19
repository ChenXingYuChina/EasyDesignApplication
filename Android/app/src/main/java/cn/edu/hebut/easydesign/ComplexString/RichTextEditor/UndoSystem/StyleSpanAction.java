package cn.edu.hebut.easydesign.ComplexString.RichTextEditor.UndoSystem;

import android.text.Editable;

import java.util.List;

public class StyleSpanAction extends SpanActionBase {
    private boolean s;

    // 不接收图片和链接
    StyleSpanAction(Editable text, Object span, int start, int end) {
        super(text, span, start, end);
        s = true;
    }

    StyleSpanAction(Editable text, Class spanType, int start, int end) {
        super(text, start, end, spanType);
        s = false;
    }

    @Override
    protected void forDelete(List<SpanHelper> outAdd, List<SpanHelper> outDelete, Editable text, int start, int end, Class spanType) {
        for (Object otherSpan : text.getSpans(start, end, spanType)) {
            int st = text.getSpanStart(otherSpan);
            int en = text.getSpanEnd(otherSpan);
            if (en == start) {
                continue;
            }

            outDelete.add(new SpanHelper(otherSpan, st, en));
            if (st < start) {
                outAdd.add(new SpanHelper(SpanTools.copySpan(otherSpan), st, start));
            }
            if (en > end) {
                outAdd.add(new SpanHelper(SpanTools.copySpan(otherSpan), end, en));
            }
        }
    }

    @Override
    protected void forAdd(List<SpanHelper> outAdd, List<SpanHelper> outDelete, Editable text, Object span, int start, int end) {

        int newStart = start;
        int newEnd = end;

        for (Object otherSpan : text.getSpans(start, end, span.getClass())) {
            int st = text.getSpanStart(otherSpan);
            int en = text.getSpanEnd(otherSpan);
            if (en == start || st == end) {
                continue;
            }
            outDelete.add(new SpanHelper(otherSpan, st, en));
            if (SpanTools.checkSpanSame(span, otherSpan)) {
                newStart = Math.min(newStart, st);
                newEnd = Math.max(newEnd, en);
            } else {
                if (st < start) {
                    outAdd.add(new SpanHelper(SpanTools.copySpan(otherSpan), st, start));
                }
                if (en > end) {
                    outAdd.add(new SpanHelper(SpanTools.copySpan(otherSpan), end, en));
                }
            }
        }
        outAdd.add(new SpanHelper(span, newStart, newEnd));
    }

    @Override
    public void redo(Editable text) {
        if (s) {
            action(text);
        } else {
            unAction(text);
        }
    }

    @Override
    public void undo(Editable text) {
        if (s) {
            unAction(text);
        } else {
            action(text);
        }
    }
}
