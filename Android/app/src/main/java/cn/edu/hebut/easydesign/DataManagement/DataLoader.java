package cn.edu.hebut.easydesign.DataManagement;

import android.content.Context;

import java.io.InputStream;

public interface DataLoader {
    Data LoadFromNet(Context ctx, InputStream stream, long id) throws Exception;
    Data LoadFromCache(Context ctx, InputStream stream, long id) throws Exception;
}
