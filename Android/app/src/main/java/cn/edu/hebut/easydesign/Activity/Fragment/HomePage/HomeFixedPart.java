package cn.edu.hebut.easydesign.Activity.Fragment.HomePage;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.PassageList.CategoryGroup;
import cn.edu.hebut.easydesign.Activity.PassageList.FixedPart;
import cn.edu.hebut.easydesign.Activity.commonComponents.ImageWithTextView;
import cn.edu.hebut.easydesign.R;

public class HomeFixedPart extends FixedPart<HomeFixedPart> implements CategoryGroup {
    onSelectedChangeListener onSelectListener = null;
    ImageWithTextView[] buttons = new ImageWithTextView[4];
    int nowChecked = -1;
    public HomeFixedPart(@NonNull Context context) {
        super(context, null);
        inflate(context, R.layout.home_fixed_layout, this);
    }

    @Override
    public HomeFixedPart getGroup() {
        if (nowChecked < 0) {
            LinearLayout group = findViewById(R.id.home_group);
            for (int i = 0; i < 4; i++) {
                buttons[i] = (ImageWithTextView) group.getChildAt(i);
                final int finalI = i;
                buttons[i].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        check(finalI);
                        onSelectListener.onChange(nowChecked);
                    }
                });
            }
            nowChecked = 0;
            buttons[0].setChecked(true);
        }
        return this;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void check(int position) {
        if (position >= 0 && position < 4 && nowChecked != position) {
            buttons[nowChecked].setChecked(false);
            buttons[position].setChecked(true);
            nowChecked = position;
        }
    }

    @Override
    public int getSelected() {
        return nowChecked;
    }

    @Override
    public void setOnSelectedChangeListener(onSelectedChangeListener listener) {
        onSelectListener = listener;
    }
}
