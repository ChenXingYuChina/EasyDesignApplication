package cn.edu.hebut.easydesign.Activity.UserInformation.InformationEditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.commonComponents.AlbumImage.UseAlbumImageByPath;
import cn.edu.hebut.easydesign.Activity.commonComponents.AlbumImage.UseAlumImageByImage;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.ImageField;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Tools.AttributeSetTools;

public class ImageSelect extends FrameLayout implements InformationEditor, UseAlumImageByImage, UseAlbumImageByPath, View.OnClickListener {
    private View select;
    private ImageView imagePreShow;
    private String path;
    private String api;

    public ImageSelect(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public ImageSelect(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        api = AttributeSetTools.getStringFromAttrs(context, attrs, "use_api", R.string.set_back_image);
        Log.i("image select", "ImageSelect: " + api);
    }

    private void initView(Context context) {
        inflate(context, R.layout.user_image_select, this);
        select = findViewById(R.id.image_select);
        imagePreShow = findViewById(R.id.image_pre_show);
        select.setOnClickListener(this);
    }

    @Override
    public String getApiUrl() {
        return api;
    }

    @Override
    public void setImage(Bitmap image) {
        imagePreShow.setImageBitmap(image);
    }

    @Override
    public void setImage(String path) {
        this.path = path;
    }

    @Override
    public void onClick(View v) {
        ContextHolder.bindUseAlbumImage(this);
    }

    @Override
    public void collectData(List<FormField> goal) throws Exception {
        if (path != null) {
            goal.add(new ImageField("userImage", path));
        } else {
            Toast.makeText(ContextHolder.getContext(), "未选择图片", Toast.LENGTH_LONG).show();
            throw new Exception();
        }
    }
}
