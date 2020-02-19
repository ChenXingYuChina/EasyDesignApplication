package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.PassageList.CategoryGroup;
import cn.edu.hebut.easydesign.Activity.PassageList.FixedPart;
import cn.edu.hebut.easydesign.R;

public class UserFixedPart extends FixedPart<UserFixedPart> implements CategoryGroup, RadioGroup.OnCheckedChangeListener {
    @IdRes
    private int[] buttonIds = new int[]{R.id.user_description_category, R.id.product_category, R.id.star_category, R.id.passage_category};
    private RadioGroup group;
    private onSelectedChangeListener listener;

    public UserFixedPart(@NonNull Context context) {
        super(context, null);
        inflate(context, R.layout.user_fixed_layout, this);
        group = findViewById(R.id.user_category);
        group.setOnCheckedChangeListener(this);
        Log.i("UFP", group.getChildCount()+ " ");
        group.check(R.id.user_description_category);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void check(int position) {
        group.check(buttonIds[position]);
    }

    @Override
    public int getSelected() {
        int s = group.getCheckedRadioButtonId();
        Log.i("UFP", "getSelected: " + s);
        for (int i = 0; i < 3; i++) {
            if (s == buttonIds[i]) return i;
        }
        return 3;
    }

    @Override
    public void setOnSelectedChangeListener(onSelectedChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public UserFixedPart getGroup() {
        return this;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.i("UFP", "onCheckedChanged: " + checkedId);
        int i;
        for (i = 0; i < 3; i++) {
            if (checkedId == buttonIds[i]) break;
        }
        listener.onChange(i);
    }
}
