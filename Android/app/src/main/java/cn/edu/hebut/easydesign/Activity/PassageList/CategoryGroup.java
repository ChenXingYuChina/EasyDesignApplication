package cn.edu.hebut.easydesign.Activity.PassageList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

public interface CategoryGroup {
    View getView();
    void check(int position);
    int getSelected();
    void setOnSelectedChangeListener(onSelectedChangeListener listener);
    interface onSelectedChangeListener {
        void onChange(int position);
    }
}
