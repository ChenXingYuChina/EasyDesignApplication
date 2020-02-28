package cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Passage.SubComment;

public class SubCommentHelper extends ViewHelper {
    private static final int[] subCommentIds = new int[]{R.id.comment_content, R.id.like_number, R.id.like_label};
    private TextView commentContent, likeNumber;
    private ImageView likeLabel;
    private boolean canLike;
    public SubComment subComment;

    public SubCommentHelper(ViewGroup target, SubComment subComment) {
        this(target, subComment, false);
    }

    public SubCommentHelper(ViewGroup target, SubComment subComment, boolean canLike) {
        this(target);
        this.subComment = subComment;
        this.canLike = canLike;
        setupData();
        setupLike();
        Log.i("SCH", "SubCommentHelper: ");
    }

    public SubCommentHelper(ViewGroup target) {
        commentContent = target.findViewById(subCommentIds[0]);
        likeNumber = target.findViewById(subCommentIds[1]);
        likeLabel = target.findViewById(subCommentIds[2]);
    }

    private void setupData() {
        if (commentContent != null) {
            commentContent.setText(subComment.content);
        }
        if (likeNumber != null) {
            likeNumber.setText(subComment.like + "");
        }
    }

    private void setupLike() {
        Log.i("SCH", "setupLike: " +subComment + subComment.isLiked());
        if (canLike)
            setupLike(likeLabel, likeNumber, subComment, subComment.new likeSubComment(), ContextHolder.getBinder());
    }

    public void setData(SubComment subComment) {
        Log.i("SCH", "setData: ");
        this.subComment = subComment;
        setupData();
        setupLike();
    }
}
