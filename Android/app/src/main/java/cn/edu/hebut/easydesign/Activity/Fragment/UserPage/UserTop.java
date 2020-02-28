package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Media.Image.ImageHostLoadTask;
import cn.edu.hebut.easydesign.Session.User.User;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;


// TODO: 2020/2/15 add click listener to finish edit and other things.
public class UserTop extends FrameLayout {
    ImageView back, head, editDescription;
    TextView name, identity, passageNumber, followNumber, coinNumber, fansNumber, followLabel;
    User user;
    TaskService.MyBinder binder;
    Condition<Boolean> cancel;

    public UserTop(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.user_top_layout, this);
        back = findViewById(R.id.user_back);
        head = findViewById(R.id.user_head_image);
        name = findViewById(R.id.user_name);
        identity = findViewById(R.id.identify);
        passageNumber = findViewById(R.id.passage_number);
        followNumber = findViewById(R.id.follow_number);
        coinNumber = findViewById(R.id.coin_number);
        fansNumber = findViewById(R.id.fans_number);
        editDescription = findViewById(R.id.edit_description);
        followLabel = findViewById(R.id.follow);
    }

    void setUser(User user) {
        binder = ContextHolder.getBinder();
        cancel = new Condition<>(false);
        binder.PutTask(new ImageHostLoadTask(user.backImage, cancel) {
            @Override
            protected void setImage(Bitmap bitmap) {
                back.setImageBitmap(bitmap);
            }
        });
        binder.PutTask(new ImageHostLoadTask(user.headImage, cancel) {
            @Override
            protected void setImage(Bitmap bitmap) {
                head.setImageBitmap(bitmap);
            }
        });
        name.setText(user.name);
        identity.setText(user.identity.toString());
        passageNumber.setText(String.valueOf(user.passageNumber));
        followNumber.setText(String.valueOf(user.followNumber));
        coinNumber.setText(String.valueOf(user.coin));
        fansNumber.setText(String.valueOf(user.fansNumber));
    }

    public void cancel() {
        if (cancel != null) cancel.condition = true;
    }

    public void setFollowOnClickListener(OnClickListener listener) {
        followNumber.setOnClickListener(listener);
        followLabel.setOnClickListener(listener);
    }
}
