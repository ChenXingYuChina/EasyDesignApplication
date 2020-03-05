package cn.edu.hebut.easydesign.Activity.UserInformation.Editor;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Spinner;

import cn.edu.hebut.easydesign.Activity.UserInformation.CachedIdentity.PublicCached;
import cn.edu.hebut.easydesign.Activity.UserInformation.UserInformation;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.User.Public;

public class PublicEditor extends IdentityEditor<PublicCached> {
    private Spinner industry, position;
    private TwoSpinnerHelper helper;
    private PublicCached iPublic;
    private static int[] childrenLabels = new int[]{R.array.position_it, R.array.position_medical};

    public PublicEditor(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.user_public_editor_layout, this, false);
        industry = findViewById(R.id.industry_input);
        position = findViewById(R.id.position_input);
        helper = new TwoSpinnerHelper(industry, R.array.industry, position, childrenLabels);
    }

    @Override
    public void setData(PublicCached iPublic) {
        if (iPublic == null) {
            return;
        }
        this.iPublic = iPublic;
        industry.setSelection(iPublic.industryPosition);
        position.setSelection(iPublic.positionPosition);
    }

    @Override
    protected void setToCache(UserInformation userInformation) {
        iPublic.positionPosition = helper.getSelectedChild();
        iPublic.industryPosition = helper.getSelectedFather();
        iPublic.goal = new Public(helper.getSelectedFatherString(), helper.getSelectedChildString());
        userInformation.setPublicCached(iPublic);
    }
}
