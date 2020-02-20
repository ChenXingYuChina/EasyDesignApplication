package cn.edu.hebut.easydesign.Resources.Media.Image;

import android.graphics.Bitmap;

import java.io.FileOutputStream;

import cn.edu.hebut.easydesign.DataManager.Data;
import cn.edu.hebut.easydesign.DataManager.DataType;

class Image implements Data {
    long id;
    Bitmap image;
    byte[] data;

    @Override
    public long GetId() {
        return id;
    }

    @Override
    public DataType GetType() {
        return DataType.Image;
    }

    @Override
    public void cache(FileOutputStream stream) throws Exception {
        stream.write(data);
    }
}
