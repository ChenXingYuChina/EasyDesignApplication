package cn.edu.hebut.easydesign.Activity.PassageList.Cards;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.PassageMetaHelper;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.UserMiniHelper;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageItemCard;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageListItem;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;

public class TitleMainCard extends PassageItemCard {
    UserMiniHelper userMiniHelper;
    PassageMetaHelper passageMetaHelper;
    public TitleMainCard(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.title_main_list_card_frame, this);
        userMiniHelper = new UserMiniHelper(this);
        passageMetaHelper = new PassageMetaHelper(this);
    }

    @Override
    protected void setItem(PassageListItem item, UserMini userMini) {
        this.item = item;
        this.userMini = userMini;
        userMiniHelper.setData(userMini, null, cancel);
        passageMetaHelper.setData(item, cancel);
    }

    @Override
    protected void reset() {
        userMiniHelper.reset();
        passageMetaHelper.reset();
    }
}
