package cn.edu.hebut.easydesign.Activity.Passage;

import android.util.Log;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.Resources.Passage.Comment;

public class CommentListHotAdapter extends CommentListAdapter {

    CommentListHotAdapter(List<Comment> comments) {
        super(comments);
    }

    @Override
    void loadMore() {
        throw new IllegalArgumentException();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.i("CLHA", "onBindViewHolder: ");
        CommentHolder commentHolder = (CommentHolder) holder;
        commentHolder.reset(pool);
        commentHolder.setData(comments.get(position), this);
    }

    @Override
    public int getItemViewType(int position) {
        return CommentHolder.type;
    }

}
