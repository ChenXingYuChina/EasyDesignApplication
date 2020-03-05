package cn.edu.hebut.easydesign.Activity.Passage;

import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.commonComponents.HalfAboveDialog;
import cn.edu.hebut.easydesign.Resources.Passage.Comment;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public abstract class CommentListAdapter extends RecyclerView.Adapter {
    protected List<Comment> comments;
    protected RecyclerView.RecycledViewPool pool;
    protected RecyclerView father;
    protected TaskService.MyBinder binder;
    protected Condition<Boolean> cancel = new Condition<>(false);
    protected FullSubComment fullSubComment;


    CommentListAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case SubCommentHolder.type:
                return SubCommentHolder.makeHolder(parent);
            case CommentHolder.type:
                return CommentHolder.makeHolder(parent);
            case FootHolder.type:
                return FootHolder.getInstance(parent, this);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        father = recyclerView;
        pool = father.getRecycledViewPool();
        binder = ContextHolder.getBinder();
    }

    SubCommentHolder makeSubCommentHolder() {
        SubCommentHolder goal = (SubCommentHolder) pool.getRecycledView(SubCommentHolder.type);
        if (goal == null) {
            goal = (SubCommentHolder) createViewHolder(father, SubCommentHolder.type);
        }
        return goal;
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    abstract void loadMore();

    static CommentListAdapter getInstance(List<Comment> comments, long pid, boolean full, FullSubComment fullSubComment) {
        CommentListAdapter goal;
        if (full) {
            goal = new CommentListFullAdapter(comments, pid);
        } else {
            goal = new CommentListHotAdapter(comments);
        }
        goal.fullSubComment = fullSubComment;
        return goal;
    }

    public void cancel() {
        cancel.condition = true;
    }

    getDialog getDialog;

    void setGetDialog(getDialog getDialog) {
        this.getDialog = getDialog;
    }

    interface getDialog {
        HalfAboveDialog dialog();
    }

    CommentHolder provideCommentHolder() {
        CommentHolder goal = (CommentHolder) pool.getRecycledView(CommentHolder.type);
        if (goal == null) {
            goal = (CommentHolder) createViewHolder(father, CommentHolder.type);
        }

        return goal;
    }

}
