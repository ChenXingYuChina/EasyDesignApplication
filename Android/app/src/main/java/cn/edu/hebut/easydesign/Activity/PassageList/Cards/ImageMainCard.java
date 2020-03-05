package cn.edu.hebut.easydesign.Activity.PassageList.Cards;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.PassageMetaHelper;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.UserMiniHelper;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageItemCard;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageListItem;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;

public class ImageMainCard extends PassageItemCard {
    private UserMiniHelper userMiniHelper;
    private PassageMetaHelper passageMetaHelper;
    public ImageMainCard(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.image_main_list_card_frame, this);
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
