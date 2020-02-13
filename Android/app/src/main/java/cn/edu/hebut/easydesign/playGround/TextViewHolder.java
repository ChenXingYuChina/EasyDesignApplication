package cn.edu.hebut.easydesign.playGround;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.R;

public class TextViewHolder extends RecyclerView.ViewHolder {
    TextView view;
    public TextViewHolder(View view) {
        super(view);
        this.view = view.findViewById(R.id.testText);
    }
    void setText(String text) {
        view.setText(text);
    }
}
