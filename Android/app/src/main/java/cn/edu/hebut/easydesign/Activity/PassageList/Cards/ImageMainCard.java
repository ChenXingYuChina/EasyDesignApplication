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

public class ImageMainCard extends PassageItemCard {
    private ImageView listImage;
    private TextView like;
    private TextView comment;
    private TextView title;
    public ImageMainCard(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.image_main_list_card_frame, this);
        listImage = findViewById(R.id.list_image);
        comment = findViewById(R.id.list_comment_number);
        like = findViewById(R.id.list_like);
        title = findViewById(R.id.list_title);
    }

    @Override
    protected void setItem(PassageListItem item, UserMini userMini) {
        this.item = item;
        TaskService.MyBinder binder = ContextHolder.getBinder();
        binder.PutTask(new ImageHostLoadTask(item.listImage, cancel) {
            @Override
            protected long getId() {
                return 0;
            }

            @Override
            protected void setImage(Bitmap bitmap) {
                listImage.setImageBitmap(data2);
            }
        });
        char[] mid = (item.like + "").toCharArray();
        like.setText(mid, 0, mid.length);
        mid = (item.commentNumber + "").toCharArray();
        comment.setText(mid, 0, mid.length);
        mid = (item.title + "").toCharArray();
        title.setText(mid, 0, mid.length);
    }

    @Override
    protected void reset() {
        listImage.setImageResource(R.drawable.logo);
        title.setText("");
    }
}
