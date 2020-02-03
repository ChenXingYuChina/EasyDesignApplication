package cn.edu.hebut.easydesign.Activity.PassageList.Cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Media.Image.ImageHostLoadTask;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageItemCard;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageListItem;
import cn.edu.hebut.easydesign.Resources.UserMini.LoadUserMiniTask;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class TitleMainCard extends PassageItemCard {
    private TextView title;
    private ImageView listImage;
    private ImageView userImage;
    private TextView userName;
    private TextView like;
    private TextView commentNumber;

    public TitleMainCard(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.title_main_list_card_frame, this);
        title = findViewById(R.id.list_title);
        listImage = findViewById(R.id.list_image);
        like = findViewById(R.id.list_like);
        commentNumber = findViewById(R.id.list_comment_number);
        userName = findViewById(R.id.user_name);
        userImage = findViewById(R.id.list_user_image);
    }

    @Override
    protected void setItem(PassageListItem item, UserMini userMini) {
        this.item = item;
        char[] mid = item.title.toCharArray();
        title.setText(mid, 0, mid.length);
        TaskService.MyBinder binder = ContextHolder.getBinder();
        binder.PutTask(new ImageHostLoadTask(item.listImage, cancel) {
            @Override
            protected long getId() {
                return 0;
            }

            @Override
            protected void setImage(Bitmap bitmap) {
                listImage.setImageBitmap(bitmap);
            }
        });
        mid = (item.like + "").toCharArray();
        like.setText(mid, 0, mid.length);
        mid = (item.commentNumber + "").toCharArray();
        commentNumber.setText(mid, 0, mid.length);
        userName.setText(userMini.name);
        binder.PutTask(new ImageHostLoadTask(userMini.headImage, cancel) {
            @Override
            protected long getId() {
                return 0;
            }

            @Override
            protected void setImage(Bitmap bitmap) {
                userImage.setImageBitmap(bitmap);
            }
        });

    }

    @Override
    protected void reset() {
        userImage.setImageResource(R.drawable.logo);
        listImage.setImageResource(R.drawable.logo);
        userName.setText("");
    }
}
