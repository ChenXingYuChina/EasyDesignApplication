package cn.edu.hebut.easydesign.Activity.PassageList.Cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Media.Image.ImageHostLoadTask;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageItemCard;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageListItem;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class IdentityMainCard extends PassageItemCard {
    private TextView userName;
    private TextView identity;
    private ImageView userImage;

    public IdentityMainCard(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.identity_main_list_card_frame, this);
        userImage = findViewById(R.id.list_user_image);
        identity = findViewById(R.id.user_identity);
        userName = findViewById(R.id.user_name);
    }

    @Override
    protected void setItem(PassageListItem item, UserMini userMini) {
        this.userMini = userMini;
        char[] mid = (userMini.name + "").toCharArray();
        userName.setText(mid,0,mid.length);
        switch (userMini.identity){
            case 0:
                mid = ("学生"+"").toCharArray();
                break;
            case 1:
                mid = ("设计师"+"").toCharArray();
                break;
            case 2:
                mid = ("公众"+"").toCharArray();
                break;
        }
        identity.setText(mid, 0, mid.length);
        TaskService.MyBinder binder = ContextHolder.getBinder();
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
        userName.setText("");
    }
}
