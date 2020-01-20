package cn.edu.hebut.easydesign.Resources.Media.Image;

import android.net.Uri;

import cn.edu.hebut.easydesign.DataManagement.DataManagement;
import cn.edu.hebut.easydesign.DataManagement.DataType;
import cn.edu.hebut.easydesign.TaskWorker.Task;

public abstract class ImageHostLoadTask extends Task<Long, Uri> {
    public ImageHostLoadTask(long id) {
        this.data1 = id;
    }
    public ImageHostLoadTask() {
    }

    @Override
    protected boolean doOnService() {
        if (data1 == null) {
            data1 = getId();
            if (data1 == 0) {
                return false;
            }
        }
        data2 = DataManagement.getInstance().Cache(DataType.Image, data1);
        return data2 == null;
    }

    /*
    if give a id so this method can has an empty implement.
     */
    protected abstract long getId();
}
