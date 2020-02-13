package cn.edu.hebut.easydesign.Activity.commonComponents;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.R;

public class ImageWithTextView extends FrameLayout {
    private ImageView imageView;
    private TextView textView;
    public ImageWithTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.home_page_card, this);
        int image = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "image", R.drawable.logo);
        imageView = findViewById(R.id.home_page_card_image);
        imageView.setImageResource(image);
//        Log.i("TEST", "ImageWithTextView: " + attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "text", R.string.home_page_forth));
        int text = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "text", R.string.home_page_forth);
        textView = findViewById(R.id.home_page_card_text);
//        Log.i("TEST", "ImageWithTextView: " + );
        textView.setText(context.getResources().getString(text));
    }

    public void setImage(int res) {
        imageView.setImageResource(res);
    }

    public void setImage(Bitmap image) {
        imageView.setImageBitmap(image);
    }

    public void setText(int res) {
        textView.setText(res);
    }
    public void setText(String string) {
        textView.setText(string);
    }
    public void setChecked(boolean check) {
        if (check) {
            // TODO: 2020/2/12 make it use the checked color mode
        } else {
            // TODO: 2020/2/12 make it use the unchecked color mode
        }
    }
}
