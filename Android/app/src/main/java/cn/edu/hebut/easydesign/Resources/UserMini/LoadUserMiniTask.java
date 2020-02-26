package cn.edu.hebut.easydesign.Resources.UserMini;

import android.graphics.Bitmap;

import cn.edu.hebut.easydesign.Resources.Media.Image.ImageLoader;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.Task;

public abstract class LoadUserMiniTask extends Task<Bitmap, UserMini> {
    private Condition<Boolean> cancel;
    long id;
    boolean loadImage;

    public LoadUserMiniTask(long id, Condition<Boolean> cancel) {
        this(id, cancel, true);
    }

    public LoadUserMiniTask(long id, Condition<Boolean> cancel, boolean loadImage) {
        this.id = id;
        this.cancel = cancel;
        this.loadImage = loadImage;
    }

    @Override
    protected void doOnMain() {
        if (!cancel.condition) {
            putInformation(data2, data1);
        }
    }

    protected abstract void putInformation(UserMini userMini, Bitmap userHeadImage);

    @Override
    protected boolean doOnService() {
        data2 = UserMiniLoader.getInstance().load(id);
        try {
            if (!cancel.condition && loadImage) {
                data1 = ImageLoader.getInstance().load(data2.headImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
