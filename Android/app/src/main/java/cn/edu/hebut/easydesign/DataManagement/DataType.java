package cn.edu.hebut.easydesign.DataManagement;

import androidx.annotation.NonNull;

/*
add the other resources which need to cache to here.
 */
public enum DataType {
    Image("image", 0), Passage("passage", 5 * 3600000), UserMini("userMini", 24 * 3600000);

    /*
     the dir and the net api path of this type
     Example: if image will GET with url (hostname):(port)/[the path to handle resources]/image?id=*,
     the path will be the "image" and the cache of this type of file will in the ..../image/*.*
     */
    public String path;

    /*
    if the keepTime is 0 means if will be cache for ever until user clear the cache. If necessary
    we can clear it if user don't use it for a long time.
     */
    public long keepTime;

    DataType(String path, long keepTime) {
        this.path = path;
        this.keepTime = keepTime;
    }

    @NonNull
    @Override
    public String toString() {
        return path;
    }
}
