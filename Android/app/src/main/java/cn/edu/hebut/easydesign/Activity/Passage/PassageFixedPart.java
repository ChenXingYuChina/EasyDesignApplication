package cn.edu.hebut.easydesign.Activity.Passage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.R;

public class PassageFixedPart extends FrameLayout {
    private RadioGroup type;
    private RadioButton last;

    public PassageFixedPart(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.passage_fixed_part_layout, this);
        type = findViewById(R.id.comment_type);
        last = findViewById(R.id.last_comment);
        Log.i("PFP", "create");
    }

    public void showLast() {
        Log.i("PFP", "show");
        last.setVisibility(View.VISIBLE);
    }

    public void setOnChangeListener(RadioGroup.OnCheckedChangeListener listener) {
        type.setOnCheckedChangeListener(listener);
    }

    public boolean full(int buttonId) {
        return R.id.hot_comment != buttonId;
    }
}
