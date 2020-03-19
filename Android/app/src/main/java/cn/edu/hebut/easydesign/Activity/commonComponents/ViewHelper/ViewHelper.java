package cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.TaskWorker.Task;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class ViewHelper {
    @DrawableRes
    protected int likeNormal = R.drawable.ic_like, liked = R.drawable.ic_like_2;

    ViewHelper() {

    }

    public ViewHelper(@DrawableRes int normal, @DrawableRes int liked) {
        this.likeNormal = normal;
        this.liked = liked;
    }

    /* action a click listener for provided view and it will send the task on click event, the binder
     * 传入控件和一个任务，点击时将会自动发送任务
     */
    public void setupLike(final ImageView label, final TextView numberLabel, final LikeAble likeAble, final Task task, @Nullable TaskService.MyBinder binder) {
        Log.i("onLike", "onMake: " + likeAble + likeAble.isLiked());
        if (likeAble.isLiked()) {
            numberLabel.setText((likeAble.likeNumber() + 1) + "");
            label.setImageResource(liked);
        } else {
            if (binder == null) {
                binder = ContextHolder.getBinder();
            }
            final TaskService.MyBinder finalBinder = binder;
            View.OnClickListener like = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("onLike", "onClick: ");
                    if (!likeAble.isLiked()) {
                        finalBinder.PutTask(task);
                        numberLabel.setText((likeAble.likeNumber() + 1) + "");
                        likeAble.setLiked();
                        label.setImageResource(liked);
                    }
                }
            };
            label.setOnClickListener(like);
            numberLabel.setOnClickListener(like);
        }
    }

    public void reset() {
    }

    public void setLikeImage(@DrawableRes int normal, @DrawableRes int liked) {
        this.likeNormal = normal;
        this.liked = liked;
    }

}
