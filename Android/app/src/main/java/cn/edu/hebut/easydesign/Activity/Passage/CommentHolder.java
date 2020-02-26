package cn.edu.hebut.easydesign.Activity.Passage;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.commonComponents.HalfAboveDialog;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.CommentHelper;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.UserMiniHelper;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Passage.Comment;
import cn.edu.hebut.easydesign.Resources.Passage.SubComment;
import cn.edu.hebut.easydesign.Resources.UserMini.LoadUserMiniTask;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class CommentHolder extends RecyclerView.ViewHolder {
    static final int type = 0;
    private UserMiniHelper userMiniHelper;
    private CommentHelper commentHelper;
    private TextView allSub;
    private Comment comment;
    private UserMini owner;
    private TaskService.MyBinder binder;
    private Condition<Boolean> cancel;
    private List<SubCommentHolder> subCommentHolders;

    public CommentHolder(View view) {
        super(view);
        userMiniHelper = new UserMiniHelper((ViewGroup) view);
        commentHelper = new CommentHelper((ViewGroup) view);
        allSub = view.findViewById(R.id.all_sub);
    }

    public void setData(final Comment comment, final CommentListAdapter adapter) {
        commentHelper.setData(comment);
        if (binder == null) {
            binder = ContextHolder.getBinder();
        }
        binder.PutTask(new LoadUserMiniTask(comment.owner, cancel) {
            @Override
            protected void putInformation(UserMini userMini, Bitmap userHeadImage) {
                owner = userMini;
                userMiniHelper.setData(userMini, userHeadImage, cancel);
            }
        });
        if (comment.subComments != null && comment.subComments.size() >= 1) {
            subCommentHolders = new LinkedList<>();
            for (SubComment subComment : comment.subComments) {
                Log.i("CH", "name: " + subComment.content);
                SubCommentHolder holder = adapter.makeSubCommentHolder();
                holder.setData(subComment, binder, cancel);
                commentHelper.addToSubCommentLayout(holder.itemView);
                subCommentHolders.add(holder);
            }
//            if (true) {
            if (comment.subCommentNumber > comment.subComments.size()) {
                allSub.setVisibility(View.VISIBLE);
                allSub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final HalfAboveDialog dialog = adapter.getDialog.dialog();
                        dialog.show(adapter.fullSubComment.show(comment, owner, adapter));
                        dialog.setOnClose(new HalfAboveDialog.onClose() {
                            @Override
                            public void onClose(View content) {
                                adapter.fullSubComment.reset();
                            }
                        });
                    }
                });
            } else {
                allSub.setVisibility(View.GONE);
            }
            Log.i("CH", "show sub comment");
        } else {
            allSub.setVisibility(View.GONE);
            commentHelper.getSubCommentLayout().setVisibility(View.GONE);
        }
    }

    static CommentHolder makeHolder(ViewGroup parent) {
        return new CommentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_layout, parent, false));
    }

    void reset(RecyclerView.RecycledViewPool pool) {
        if (cancel != null) {
            cancel.condition = true;
            cancel = new Condition<>(false);
            commentHelper.reset();
            if (subCommentHolders != null) {
                for (SubCommentHolder holder : subCommentHolders) {
                    holder.reset();
                    pool.putRecycledView(holder);
                }
            }
            userMiniHelper.reset();
            comment = null;
            owner = null;
        } else {
            cancel = new Condition<>(false);
        }
    }
}
