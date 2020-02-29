package cn.edu.hebut.easydesign.Activity.Passage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.LoginPage;
import cn.edu.hebut.easydesign.Activity.Passage.PassageTask.LoadSubCommentTask;
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
    }

    FullSubComment show(Comment comment, UserMini owner, CommentListAdapter adapter) {
        this.adapter = adapter;
        this.owner = owner;
        this.comment = comment;
        cancel = new Condition<>(false);
        if (comment.subComments != null) {
            if (comment.subComments.size() < comment.subCommentNumber) {
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
            } else {
                List<SubComment> comments = (ArrayList<SubComment>) comment.subComments.clone();
                Collections.reverse(comments);
                setup(comments);
            }
        } else {
            setup(new LinkedList<SubComment>());
        }
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
        if (commentHelper != null) {
            commentHelper.setData(comment);
        } else {
            commentHelper = new CommentHelper(this, comment, true, true);
        }
        TaskService.MyBinder binder = ContextHolder.getBinder();
        subCommentHolders = new LinkedList<>();
        for (SubComment subComment : subComments) {
            Log.i("CH", "name: " + subComment.content);
            SubCommentHolder holder = adapter.makeSubCommentHolder();
            holder.setData(subComment, binder, cancel);
            this.subComments.addView(holder.itemView);
            subCommentHolders.add(holder);
        }
        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FSC", "onClick: ");
                sendSubComment();
            }
        });
    }

    private void sendSubComment() {
        String subCommentContent = input.getText().toString();
        if (subCommentContent.length() != 0) {
            send.setClickable(false);
            ContextHolder.getBinder().PutTask(comment.new subCommentTo(subCommentContent) {
                @Override
                protected void onError(int errCode) {
                    if (errCode == 401) {
                        Context context = ContextHolder.getContext();
                        context.startActivity(new Intent(context, LoginPage.class));
                    }
                }

                @Override
                protected void onSuccess() {
                    Toast.makeText(ContextHolder.getContext(), "评论成功", Toast.LENGTH_SHORT).show();
                    send.setClickable(true);
                    input.setText("");
                }
            });
        }
    }
    public String getInput() {
        return input.getText().toString();
    }
    public void setInput(String input) {
        this.input.setText(input);
    }
}
