package cn.edu.hebut.easydesign.Resources.Media.Image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.DataManagement.DataManagement;
import cn.edu.hebut.easydesign.DataManagement.DataType;
import cn.edu.hebut.easydesign.HttpClient.Client;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ImageManager {
    String imageDir;
    private static ImageManager instance = null;
    private ImageManager() {
        Context ctx = ContextHolder.getContext();
        File dir = ctx.getExternalFilesDir("image");
        dir.mkdir();
        imageDir = dir.getAbsolutePath() + File.separator;
    }

    public static ImageManager getInstance() {
        if (instance == null) {
            synchronized (ImageManager.class) {
                if (instance == null)
                    instance = new ImageManager();
            }
        }
        return instance;
    }

    public Bitmap LoadImage(long id) throws Exception {
        if (DataManagement.getInstance().inCache(DataType.Image, id)) {
            Uri uri = DataManagement.getInstance().GetUriOf(DataType.Image, id);
            File f  = new File(new URI(uri.toString()));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(f));
            byte[] data = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(data);
            Bitmap goal = BitmapFactory.decodeByteArray(data, 0, data.length);
            bufferedInputStream.close();
            return goal;
        }
        Response r = Client.getInstance().GetFromHost("image?id=" + id);
        if (r.body() != null) {
            ResponseBody body = r.body();
//            Log.i("LI", "LoadImage: "+body.contentLength());
            byte[] buffer = body.bytes();
            File file = new File(imageDir + id);
            FileOutputStream output = new FileOutputStream(file);
            output.write(buffer);
            output.flush();
            output.close();
            return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
        }
        return null;
    }
}
