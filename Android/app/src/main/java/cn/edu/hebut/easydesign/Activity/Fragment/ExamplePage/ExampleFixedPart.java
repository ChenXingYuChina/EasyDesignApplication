package cn.edu.hebut.easydesign.Activity.Fragment.ExamplePage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.PassageList.Config.LastByType;
import cn.edu.hebut.easydesign.Activity.PassageList.FixedPart;
import cn.edu.hebut.easydesign.Activity.PassageList.CategoryGroup;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListViewPerformance;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Tools.ResourcesTools;

public class ExampleFixedPart extends FixedPart<ExampleFixedPart> implements CategoryGroup, RadioGroup.OnCheckedChangeListener {
    private RadioGroup radioGroup;
    private onSelectedChangeListener listener;
    public ExampleFixedPart(@NonNull Context context) {
        super(context, null);
        inflate(context, R.layout.example_radio_group, this);
        radioGroup= findViewById(R.id.radio_group);
    }
    public ExampleFixedPart(@NonNull Context context, JSONArray categoryNames) {
        super(context, null);
        inflate(context, R.layout.example_radio_group, this);
        radioGroup = findViewById(R.id.radio_group);
        boolean check = true;
        try {
            Log.i("efp", categoryNames.toString());
            for (int i = 0; i < categoryNames.length(); i++) {
                RadioButton button = (RadioButton) LayoutInflater.from(ContextHolder.getContext()).inflate(R.layout.example_page_radio_button, radioGroup, false);
                button.setText(categoryNames.getString(i));
                button.setChecked(check);
                check = false;
                button.setId(i);
                radioGroup.addView(button);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
                int margin = ResourcesTools.dp2px(5);
                layoutParams.setMargins(margin, margin, margin, margin);
                layoutParams.weight = 1;
                button.setLayoutParams(layoutParams);
            }
        } catch (Exception e) {
            Log.i("efp", e.toString());
        }
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void check(int position) {
        radioGroup.check(position);
    }

    @Override
    public int getSelected() {
        return radioGroup.getCheckedRadioButtonId();
    }

    @Override
    public void setOnSelectedChangeListener(final onSelectedChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public ExampleFixedPart getGroup() {
        return this;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        listener.onChange(checkedId);
    }
}
