package cn.edu.hebut.easydesign.Activity.PassageList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class FixedPart<T extends CategoryGroup> extends FrameLayout {

    public FixedPart(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract T getGroup();
}
