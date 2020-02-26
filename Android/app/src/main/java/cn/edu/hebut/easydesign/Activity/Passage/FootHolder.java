package cn.edu.hebut.easydesign.Activity.Passage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.R;

class FootHolder extends RecyclerView.ViewHolder {
    static final int type = 2;
    private TextView tip;

    FootHolder(@NonNull View itemView, final CommentListAdapter adapter) {
        super(itemView);
        tip = itemView.findViewById(R.id.foot);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.loadMore();
                tip.setText(R.string.loading_list);
            }
        });
    }

    static FootHolder getInstance(ViewGroup parent, CommentListAdapter adapter) {
        return new FootHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_foot_layout, parent, false), adapter);
    }

    void setTip(String text) {
        tip.setText(text);
    }

    void setTip(@StringRes int text) {
        tip.setText(text);
    }
}
