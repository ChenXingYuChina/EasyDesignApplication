package cn.edu.hebut.easydesign.Activity.PassageList;

import android.util.AttributeSet;

import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Tools.AttributeSetTools;

public class TipResources {
    String[] texts = new String[6];
    static final int text_foot_onError = 0;
    static final int text_foot_onNoNew = 1;
    static final int text_foot_onLoading = 2;
    static final int text_foot_toLoad = 3;
    static final int text_refresh_onError = 4;
    static final int text_refresh_onNoNew = 5;

    TipResources(AttributeSet attrs) {

        texts[text_foot_onError] = AttributeSetTools.getStringFromAttrs(attrs, "text_foot_onError", R.string.error);
        texts[text_foot_onNoNew] = AttributeSetTools.getStringFromAttrs(attrs, "text_foot_onNoNew", R.string.finish_list);
        texts[text_foot_onLoading] = AttributeSetTools.getStringFromAttrs(attrs, "text_foot_onLoading", R.string.loading_list);
        texts[text_foot_toLoad] = AttributeSetTools.getStringFromAttrs(attrs, "text_foot_toLoad", R.string.load);
        texts[text_refresh_onError] = AttributeSetTools.getStringFromAttrs(attrs, "text_refresh_onError", R.string.error);
        texts[text_refresh_onNoNew] = AttributeSetTools.getStringFromAttrs(attrs, "text_refresh_onNoNew", R.string.no_news);
    }
}
