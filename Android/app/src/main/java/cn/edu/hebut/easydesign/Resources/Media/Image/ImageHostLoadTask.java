package cn.edu.hebut.easydesign.Resources.Media.Image;

import android.graphics.Bitmap;
import android.util.Log;

import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.Task;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public abstract class ImageHostLoadTask extends Task<Long, Bitmap> {
    protected Condition<Boolean> cancel;
    protected TaskService.MyBinder binder;
    public ImageHostLoadTask(long id, Condition<Boolean> cancel) {
        this.data1 = id;
        this.async = true;
        this.cancel = cancel;
    }

    @Override
    protected void doOnMain() {
        if (!cancel.condition) {
            setImage(data2);
        }
    }

    public ImageHostLoadTask(Condition<Boolean> cancel) {
        this.cancel = cancel;
        this.async = true;
    }

    @Override
    protected boolean doOnService() {
        if (data1 == null) {
            data1 = getId();
            Log.i("EDImage", "doOnService: imageId" + data1);
            if (data1 == 0) {
                return false;
            }
        }
//        data2 = DataManagement.getInstance().Cache(DataType.Image, data1);
        try {
            if (!cancel.condition) {
                data2 = ImageLoader.getInstance().load(data1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data2 != null;
    }

    /*
            if give a id so this method can has an empty implement.
             */
    protected abstract long getId();
    protected abstract void setImage(Bitmap bitmap);
}
