package cn.edu.hebut.easydesign.DataManagement;

import android.content.Context;
import android.os.IBinder;

import java.io.InputStream;

public interface DataLoader {
    <D extends Data> D LoadFromNet(String r, long id) throws Exception;
    <D extends Data> D LoadFromCache(InputStream stream, long id) throws Exception;
}
