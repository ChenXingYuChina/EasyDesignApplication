package cn.edu.hebut.easydesign.Activity.PassageList;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageItemCard;

public class ItemHolder extends RecyclerView.ViewHolder {
    PassageItemCard card;
    ItemHolder(PassageItemCard card) {
        super(card);
        this.card = card;
    }
}
