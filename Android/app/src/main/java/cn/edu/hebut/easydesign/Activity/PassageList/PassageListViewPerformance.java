package cn.edu.hebut.easydesign.Activity.PassageList;

import android.util.AttributeSet;

import androidx.annotation.LayoutRes;
import cn.edu.hebut.easydesign.R;

import static android.view.View.NO_ID;

public class PassageListViewPerformance {
    @LayoutRes int card, head, layout;

    public static PassageListViewPerformance getFromAttributeSet(AttributeSet attrs) {
        int card = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "card", R.layout.title_main_list_card_frame);
        int layout = attrs.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "layout_manager", -1);
        int head = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "head", NO_ID);
        if (card == NO_ID || layout == NO_ID || head == NO_ID) {
            return null;
        }
        return new PassageListViewPerformance(card, head, layout);
    }

    public PassageListViewPerformance(@LayoutRes int card, @LayoutRes int head, @LayoutRes int layout) {
        this.card = card;
        this.head = head;
        this.layout = layout;
    }
}
