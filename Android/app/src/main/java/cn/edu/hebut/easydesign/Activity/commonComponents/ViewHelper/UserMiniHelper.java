package cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Media.Image.ImageHostLoadTask;
import cn.edu.hebut.easydesign.Resources.UserMini.LoadUserMiniTask;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.TaskWorker.Condition;

public class UserMiniHelper extends ViewHelper {
    private static final String[] userIdentityString = new String[]{"学生", "公众", "设计师"};
    private static final int[] miniUserIds = new int[]{R.id.user_head, R.id.user_name, R.id.user_identity};
    private ImageView userHead;
    private TextView userName, userIdentity;
    public UserMini userMini;
    private Condition<Boolean> cancel;

    public UserMiniHelper(ViewGroup target, UserMini userMini, Condition<Boolean> cancel) {
        this(target, userMini, null, cancel);
    }

    public UserMiniHelper(ViewGroup target, UserMini userMini, Bitmap bitmap, Condition<Boolean> cancel) {
        this(target);
        this.cancel = cancel;
        setupUserMini(userMini, bitmap);
    }

    public UserMiniHelper(ViewGroup target, Condition<Boolean> cancel, long userId) {
        this(target);
        this.cancel = cancel;
        ContextHolder.getBinder().PutTask(new LoadUserMiniTask(userId, cancel, userHead != null) {
            @Override
            protected void putInformation(UserMini userMini, Bitmap userHeadImage) {
                setupUserMini(userMini, userHeadImage);
            }
        });
    }

    public UserMiniHelper(ViewGroup target, Condition<Boolean> cancel, long userId, boolean canJump) {
        throw new IllegalArgumentException("尚未完成，敬请期待");
    }

    public UserMiniHelper(ViewGroup target) {
        userHead = target.findViewById(miniUserIds[0]);
        userName = target.findViewById(miniUserIds[1]);
        userIdentity = target.findViewById(miniUserIds[2]);

    }

    private void setupUserMini(UserMini userMini, Bitmap userHeadImage) {
        this.userMini = userMini;
        if (userHead != null) {
            if (userHeadImage == null) {
                ContextHolder.getBinder().PutTask(new ImageHostLoadTask(userMini.headImage, cancel) {
                    @Override
                    protected void setImage(Bitmap bitmap) {
                        userHead.setImageBitmap(bitmap);
                    }
                });
            } else {
                userHead.setImageBitmap(userHeadImage);
            }
        }
        if (userName != null) {
            userName.setText(userMini.name);
        }
        if (userIdentity != null) {
            userIdentity.setText(userIdentityString[userMini.identity]);
        }
    }

    public void reset() {
        cancel.condition = true;
        if (userHead != null) {
            userHead.setImageResource(R.drawable.logo);
        }
        if (userName != null) {
            userName.setText("");
        }
        if (userIdentity != null) {
            userIdentity.setText("");
        }
    }

    public void setData(UserMini userMini, Bitmap userHeadImage, Condition<Boolean> cancel) {
        this.cancel = cancel;
        setupUserMini(userMini, userHeadImage);
    }
}
