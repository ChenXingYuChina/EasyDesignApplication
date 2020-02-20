package cn.edu.hebut.easydesign.Resources.UserMini;

import android.graphics.Bitmap;

import cn.edu.hebut.easydesign.Resources.Media.Image.ImageManager;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.Task;

public abstract class LoadUserMiniTask extends Task<Bitmap, UserMini> {
    private Condition<Boolean> cancel;
    long id;

    public LoadUserMiniTask(long id, Condition<Boolean> cancel) {
        this.id = id;
        this.cancel = cancel;
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
            if (!cancel.condition) {
                data1 = ImageManager.getInstance().LoadImage(data2.headImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
