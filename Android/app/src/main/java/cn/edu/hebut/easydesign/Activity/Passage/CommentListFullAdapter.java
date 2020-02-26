package cn.edu.hebut.easydesign.Activity.Passage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Passage.Comment;

public class CommentListFullAdapter extends CommentListAdapter {
    private long pid;
    private FootHolder foot = null;

    CommentListFullAdapter(List<Comment> comments, long pid) {
        super(comments);
        this.pid = pid;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentHolder) {
            ((CommentHolder) holder).reset(pool);
            ((CommentHolder) holder).setData(comments.get(position), this);
        } else if (foot == null && holder instanceof FootHolder) {
            foot = (FootHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    void loadMore() {
        binder.PutTask(new LoadCommentTask(pid, comments.size(), cancel) {
            @Override
            protected void onSuccess(List<Comment> comments) {
                if (comments.size() == 0) {
                    foot.setTip(R.string.finish_list);
                }
                CommentListFullAdapter.this.comments.addAll(comments);
                int length = comments.size();
                notifyItemRangeInserted(CommentListFullAdapter.this.comments.size() - length, length);
            }

            @Override
            protected void onError(int errCode) {
                foot.setTip("错误，代码：" + errCode);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == comments.size()) return FootHolder.type;
        return CommentHolder.type;
    }
}
