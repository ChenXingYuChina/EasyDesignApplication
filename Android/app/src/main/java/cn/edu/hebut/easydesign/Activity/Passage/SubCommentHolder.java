package cn.edu.hebut.easydesign.Activity.Passage;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.SubCommentHelper;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.UserMiniHelper;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Passage.SubComment;
import cn.edu.hebut.easydesign.Resources.UserMini.LoadUserMiniTask;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

class SubCommentHolder extends RecyclerView.ViewHolder {
    static final int type = 1;
    private UserMini owner;
    private UserMiniHelper userMiniHelper;
    private SubCommentHelper subCommentHelper;

    private SubCommentHolder(@NonNull View itemView) {
        super(itemView);
        userMiniHelper = new UserMiniHelper((ViewGroup) itemView);
        subCommentHelper = new SubCommentHelper((ViewGroup) itemView);
    }

    void setData(final SubComment data, final TaskService.MyBinder binder, final Condition<Boolean> cancel) {
        subCommentHelper.setData(data);
        binder.PutTask(new LoadUserMiniTask(data.owner, cancel, false) {
            @Override
            protected void putInformation(UserMini userMini, Bitmap userHeadImage) {
                userMiniHelper.setData(userMini, userHeadImage, cancel);
                owner = userMini;
            }
        });
    }

    static SubCommentHolder makeHolder(ViewGroup parent) {
        return new SubCommentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_comment_item_layout, parent, false));
    }

    void reset() {
        userMiniHelper.reset();
        subCommentHelper.reset();
    }
}
