package cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Passage.Comment;

public class CommentHelper extends ViewHelper {
    private static final int[] commentIds = new int[]{
            R.id.like_number,
            R.id.comment_number,
            R.id.like_label,
            R.id.comment_content,
            R.id.sub_comment_layout
    };
    private TextView likeNumber, commentNumber, commentContent;
    private ImageView likeLabel;
    private ViewGroup subCommentLayout;
    private boolean full, canLike;
    public Comment comment;

    public CommentHelper(ViewGroup target) {
        likeNumber = target.findViewById(commentIds[0]);
        commentNumber = target.findViewById(commentIds[1]);
        likeLabel = target.findViewById(commentIds[2]);
        commentContent = target.findViewById(commentIds[3]);
        subCommentLayout = target.findViewById(commentIds[4]);
    }

    public CommentHelper(ViewGroup target, Comment comment) {
        this(target, comment, false);
    }

    public CommentHelper(ViewGroup target, Comment comment, boolean full) {
        this(target, comment, full, false);
    }

    public CommentHelper(ViewGroup target, Comment comment, boolean full, boolean canLike) {
        this(target);
        this.comment = comment;
        this.full = full;
        this.canLike = canLike;
        setupData();
        setupLike();
    }

    private void setupLike() {
        if (canLike)
            setupLike(likeLabel, likeNumber, comment, comment.new likeComment(), ContextHolder.getBinder());
    }

    private void setupData() {
        if (likeNumber != null) {
            likeNumber.setText(comment.like + "");
        }
        if (commentNumber != null) {
            commentNumber.setText(comment.subCommentNumber + "");
        }
        if (commentContent != null) {
            comment.content.SetToTextView(commentContent);
        }
    }

    public void setData(Comment comment) {
        this.comment = comment;
        setupData();
        setupLike();
    }

    @Override
    public void reset() {

        if (likeNumber != null) {
            likeNumber.setText("");
        }
        if (commentNumber != null) {
            commentNumber.setText("");
        }
        if (commentContent != null) {
            commentContent.setText("");
        }
        if (full && subCommentLayout != null) {
            subCommentLayout.removeAllViews();
            subCommentLayout.setVisibility(View.GONE);
        }
    }

    public ViewGroup getSubCommentLayout() {
        return subCommentLayout;
    }

    public void addToSubCommentLayout(View view) {
        subCommentLayout.setVisibility(View.VISIBLE);
        subCommentLayout.addView(view);
    }
}
