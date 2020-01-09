package cn.edu.hebut.easydesign.Resources.Media.Image;

import android.net.Uri;

public interface ImageLoadCallback {
    /*
    this will run on the Main Thread.
     */
    public void callback(Uri localUri);
}
