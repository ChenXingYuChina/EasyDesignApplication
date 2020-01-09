package cn.edu.hebut.easydesign.Resources.Media.Image;

import android.net.Uri;

import cn.edu.hebut.easydesign.DataManagement.DataManagement;
import cn.edu.hebut.easydesign.DataManagement.DataType;
import cn.edu.hebut.easydesign.TaskWorker.Task;

public class ImageLoadTask extends Task<Long, ImageLoadCallback> {
    private Uri uri;
    public ImageLoadTask(long id, ImageLoadCallback callback) {
        this.data2 = callback;
        this.data1 = id;
    }
    @Override
    protected void doOnMain() {
        data2.callback(uri);
    }

    @Override
    protected boolean doOnService() {
        uri = DataManagement.getInstance().Cache(DataType.Image, data1);
        if (uri != null) {
            return true;
        }
        return true;
    }
}
