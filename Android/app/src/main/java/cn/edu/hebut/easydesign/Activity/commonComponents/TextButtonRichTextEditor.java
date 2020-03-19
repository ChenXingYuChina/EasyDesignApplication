package cn.edu.hebut.easydesign.Activity.commonComponents;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.ComplexString.RichTextEditor.RichTextEditorHelper;
import cn.edu.hebut.easydesign.R;

public class TextButtonRichTextEditor extends FrameLayout {
    private static final String TAG = "TBRTE";
    private RichTextEditorHelper helper;
    private ViewGroup[] spanButtonGroup = new ViewGroup[3];
    private static int[] spanGroupIds = new int[]{R.id.background_color_group, R.id.text_color_group, R.id.font_size_group};
    private static int[] spanTypeIds = new int[]{R.id.background_color, R.id.text_color, R.id.text_font_size};
    public TextButtonRichTextEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.text_button_rich_editor, this);
        helper = new RichTextEditorHelper(this);
        // 将
        for (int i = 0; i < ComplexString.HYPERLINK; i++) {
            if (ComplexString.SMALL_FONT_SIZE + ComplexString.FONT_SIZE_BASE <= i && i <= ComplexString.HUGE_FONT_SIZE + ComplexString.FONT_SIZE_BASE) {
                continue;
            }
            makeUpLabel((TextView) helper.getAddViewBySpanId(i), i);
        }
        RadioGroup spanTypeSelect = findViewById(R.id.span_type_group);
        for (int i = 0; i < spanGroupIds.length; i++) {
            spanButtonGroup[i] = findViewById(spanGroupIds[i]);
        }
        spanTypeSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < spanGroupIds.length; i++) {
                    if (checkedId == spanTypeIds[i]) {
                        spanButtonGroup[i].setVisibility(VISIBLE);
                    } else {
                        spanButtonGroup[i].setVisibility(INVISIBLE);
                    }
                }
            }
        });
    }

    private void makeUpLabel(TextView v, int id) {
        Log.i(TAG, "makeUpLabel: " + v + " " + id );
        if (v == null) {
            return;
        }
        SpannableString mid = new SpannableString(v.getText());
        mid.setSpan(ComplexString.getSpanFromId(id, null), mid.length() - 1, mid.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        v.setText(mid);
    }

    public ComplexString getGoal() {
        return helper.getEditor().toComplexString();
    }
}
