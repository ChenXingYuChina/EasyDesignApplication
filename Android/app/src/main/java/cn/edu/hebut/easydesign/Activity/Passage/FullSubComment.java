package cn.edu.hebut.easydesign.Activity.Passage;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.CommentHelper;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.UserMiniHelper;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Passage.Comment;
import cn.edu.hebut.easydesign.Resources.Passage.SubComment;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class FullSubComment extends FrameLayout {
    private LinearLayout subComments;
    private TextView err;
    private Button send;
    private EditText input;
    private Comment comment;
    private UserMini owner;
    private UserMiniHelper userMiniHelper;
    private CommentHelper commentHelper;
    private Condition<Boolean> cancel;
    private List<SubCommentHolder> subCommentHolders;
    private CommentListAdapter adapter;

    public FullSubComment(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.full_sub_comment, this);

        subComments = findViewById(R.id.sub_comment);

        send = findViewById(R.id.sub_comment_send);
        input = findViewById(R.id.sub_comment_input);

        userMiniHelper = new UserMiniHelper(this);
        commentHelper = new CommentHelper(this);
    }

    FullSubComment show(Comment comment, UserMini owner, CommentListAdapter adapter) {
        this.adapter = adapter;
        this.owner = owner;
        this.comment = comment;
        cancel = new Condition<>(false);
        TaskService.MyBinder binder = ContextHolder.getBinder();
        binder.PutTask(new LoadSubCommentTask(comment.passage, comment.position, cancel) {
            @Override
            protected void onError(int errCode) {
                if (err == null) {
                    err = new TextView(ContextHolder.getContext());
                    err.setText("出错了：" + errCode);
                }
                subComments.addView(err);
            }

            @Override
            protected void onSuccess(List<SubComment> subComments) {
                setup(subComments);
            }
        });
        return this;
    }

    void reset() {
        if (subComments.getChildAt(0) != err) {
            cancel.condition = true;
            cancel = new Condition<>(false);
            if (subCommentHolders != null) {
                for (SubCommentHolder holder : subCommentHolders) {
                    holder.reset();
                    adapter.pool.putRecycledView(holder);
                }
            }
            commentHelper.reset();
            userMiniHelper.reset();
        }
        subComments.removeAllViews();

    }

    private void setup(List<SubComment> subComments) {
        userMiniHelper.setData(owner, null, cancel);
        commentHelper.setData(comment);
        TaskService.MyBinder binder = ContextHolder.getBinder();
        subCommentHolders = new LinkedList<>();
        for (SubComment subComment : subComments) {
            Log.i("CH", "name: " + subComment.content);
            SubCommentHolder holder = adapter.makeSubCommentHolder();
            holder.setData(subComment, binder, cancel);
            this.subComments.addView(holder.itemView);
            subCommentHolders.add(holder);
        }
    }

}
