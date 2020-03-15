package cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

import java.util.List;

import cn.edu.hebut.easydesign.Activity.UserInformation.UserInformation;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.TwoSpinnerHelper;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.User.Public;
import cn.edu.hebut.easydesign.Session.User.UserStringResources;
import okhttp3.FormBody;
import okhttp3.MultipartBody;

public class PublicEditor extends IdentityEditor<Public> {
    private Spinner industry, position;
    private TwoSpinnerHelper helper;

    public PublicEditor(Context context) {
        super(context);
        initView(context);
    }

    public PublicEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.user_public_editor_layout, this);
        industry = findViewById(R.id.industry_input);
        position = findViewById(R.id.position_input);
        helper = new TwoSpinnerHelper(industry, UserStringResources.getIndustryNames(), position, UserStringResources.getPositionNames());
    }

    public void collectData(List<FormField> goal) throws Exception {
        goal.add(new IdentityField(fieldName, new Public(helper.getSelectedFather(), helper.getSelectedChild())));
    }

    @Override
    public void setDate(Public data) {
        industry.setSelection(data.industry);
        position.setSelection(data.position);
    }
}
