package cn.edu.hebut.easydesign.Resources.Media.Image;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.CrossSiteHttpTask;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class ImageCrossSiteLoadTask extends CrossSiteHttpTask<Bitmap> {
    public ImageCrossSiteLoadTask(URL url) {
        super(url);
    }

    @Override
    protected int processResponse(Response r) {
        ResponseBody responseBody = r.body();
        if (responseBody != null) {
            data2 = BitmapFactory.decodeStream(responseBody.byteStream());
        }
        return data2 != null?0:1;
    }

    @Override
    protected int beforeRequest(Request.Builder r) {
        return 0;
    }

    @Override
    protected int beforeProcess() {
        return 0;
    }

    @Override
    protected int onError(Exception e) {
        Log.i("ED", "CrossSiteImageOnError: " + e);
        return 0;
    }
}
