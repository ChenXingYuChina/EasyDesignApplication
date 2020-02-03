package cn.edu.hebut.easydesign.Activity.PassageList.Cards;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageItemCard;

public class CardFactory {
    public static PassageItemCard makeCard(@LayoutRes int card, ViewGroup parent) {
        return (PassageItemCard) LayoutInflater.from(ContextHolder.getContext()).inflate(card, parent, false);
    }
}
