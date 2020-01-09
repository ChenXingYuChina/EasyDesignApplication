package cn.edu.hebut.easydesign.DataManagement;

import android.content.Context;
import android.os.IBinder;

import java.io.InputStream;

public interface DataLoader {
    Data LoadFromNet(Context ctx, IBinder binder, InputStream stream, long id) throws Exception;
    Data LoadFromCache(Context ctx, IBinder binder, InputStream stream, long id) throws Exception;
}
