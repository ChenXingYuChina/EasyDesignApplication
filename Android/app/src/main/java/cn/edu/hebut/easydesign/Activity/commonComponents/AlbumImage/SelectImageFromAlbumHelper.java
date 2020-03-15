package cn.edu.hebut.easydesign.Activity.commonComponents.AlbumImage;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.File;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Tools.ResourcesTools;

public class SelectImageFromAlbumHelper implements SelectImageFromAlbum {
    private Activity activity;
    private UseAlbumImage use;
    private static int getImageRequestCode = 12345;

    @Override
    public void openAlbum() {
        Intent startFileSystem = new Intent();
        startFileSystem.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startFileSystem.setType("image/*");
        startFileSystem.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(startFileSystem, getImageRequestCode);
    }

    @Override
    public void setInteractionObject(UseAlbumImage t) {
        this.use = t;
    }

    public void handleResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == getImageRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("SelectImage", "handleResult: ok");
                if (data == null) {
                    Log.i("SelectImage", "handleResult: data = null");
                    return;
                }
                Uri uri = data.getData();
                if (uri == null) {
                    Log.i("SelectImage", "handleResult: uri = null");
                    return;
                }
                String path = null;
                if (use instanceof UseAlbumImageByPath) {
                    path = ResourcesTools.uriToFile(activity, uri);
                    ((UseAlbumImageByPath) use).setImage(path);
                }
                if (use instanceof UseAlumImageByImage) {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    if (bitmap != null) {
                        ((UseAlumImageByImage) use).setImage(bitmap);
                    } else {
                        if (path == null) {
                            path = ResourcesTools.uriToFile(activity, uri);
                        }
                        ((UseAlumImageByImage) use).setImage(BitmapFactory.decodeFile(path));
                    }
                }
                if (use instanceof UseAlbumImageByUri) {
                    ((UseAlbumImageByUri) use).setImage(uri);
                }
            }
        }

    }

    public SelectImageFromAlbumHelper(Activity activity) {
        this.activity = activity;
        ContextHolder.setSelectImageFromAlbum(this);
    }
}
