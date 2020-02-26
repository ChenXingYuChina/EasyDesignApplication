package cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Media.Image.ImageHostLoadTask;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageListItem;
import cn.edu.hebut.easydesign.TaskWorker.Condition;

public class PassageMetaHelper extends ViewHelper {
    private static final int[] passageMetaIds = new int[]{R.id.comment_number, R.id.like_label, R.id.like_number, R.id.passage_title, R.id.list_image};
    private TextView commentNumber, likeNumber, passageTitle;
    private ImageView likeLabel, passageListImage;
    private Condition<Boolean> cancel;
    private boolean canLike;
    public PassageListItem item;

    public PassageMetaHelper(ViewGroup target, PassageListItem item, Condition<Boolean> cancel) {
        this(target, item, cancel, false);
    }

    public PassageMetaHelper(ViewGroup target, PassageListItem item, Condition<Boolean> cancel, boolean canLike) {
        this(target);
        this.canLike = canLike;
        setupMeta(item, cancel);
        setupLike();
    }

    public PassageMetaHelper(ViewGroup target) {
        commentNumber = target.findViewById(passageMetaIds[0]);
        likeLabel = target.findViewById(passageMetaIds[1]);
        likeNumber = target.findViewById(passageMetaIds[2]);
        passageTitle = target.findViewById(passageMetaIds[3]);
        passageListImage = target.findViewById(passageMetaIds[4]);
    }

    private void setupMeta(PassageListItem item, Condition<Boolean> cancel) {
        this.item = item;
        this.cancel = cancel;
        if (passageListImage != null) {
            ContextHolder.getBinder().PutTask(new ImageHostLoadTask(item.listImage, cancel) {
                @Override
                protected void setImage(Bitmap bitmap) {
                    passageListImage.setImageBitmap(bitmap);
                }
            });
        }
        if (commentNumber != null) {
            commentNumber.setText(item.commentNumber + "");
        }
        if (likeNumber != null) {
            likeNumber.setText(item.like + "");
        }
        if (passageTitle != null) {
            passageTitle.setText(item.title);
        }
    }

    // 在setData后使用
    private void setupLike() {
        if (!canLike) {
            return;
        }
        setupLike(likeLabel, likeNumber, item, item.new likePassage(), ContextHolder.getBinder());
    }

    public void reset() {
        cancel.condition = false;
        if (passageListImage != null) {
            passageListImage.setImageResource(R.drawable.logo);
        }
        if (commentNumber != null) {
            commentNumber.setText("");
        }
        if (likeNumber != null) {
            likeNumber.setText("");
        }
        if (passageTitle != null) {
            passageTitle.setText("");
        }
        // 重置点赞图标
    }

    public void setData(PassageListItem item, Condition<Boolean> cancel) {
        setupMeta(item, cancel);
        setupLike();
    }
}
