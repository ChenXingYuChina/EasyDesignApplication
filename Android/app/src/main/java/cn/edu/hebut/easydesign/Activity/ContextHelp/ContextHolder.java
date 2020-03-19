package cn.edu.hebut.easydesign.Activity.ContextHelp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import cn.edu.hebut.easydesign.Activity.commonComponents.AlbumImage.SelectImageFromAlbum;
import cn.edu.hebut.easydesign.Activity.commonComponents.AlbumImage.SelectImageFromAlbumHelper;
import cn.edu.hebut.easydesign.Activity.commonComponents.AlbumImage.UseAlbumImage;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class ContextHolder {
    private static final ContextHolder holder = new ContextHolder();
    private Context context;
    private TaskService.MyBinder binder;
    private SelectImageFromAlbum selectImageFromAlbum;

    public static void setContext(Context context) {
        holder.context = context;
    }

    public static Context getContext() {
        return holder.context;
    }

    public static TaskService.MyBinder getBinder() {
        return holder.binder;
    }

    public static void setBinder(TaskService.MyBinder binder) {
        holder.binder = binder;
    }
    public static void setSelectImageFromAlbum(SelectImageFromAlbum selectImageFromAlbum) {
        holder.selectImageFromAlbum = selectImageFromAlbum;
    }

    public static void selectAlbumImage(UseAlbumImage use) {
        holder.selectImageFromAlbum.setInteractionObject(use);
        holder.selectImageFromAlbum.openAlbum();
    }
}
