package cn.edu.hebut.easydesign.DataManagement;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;

public class DataManagement {
    private static DataManagement instance = new DataManagement();
    private LocalSource local;
    private NetSource net;
    private DataLoader[] loader;

    private static final String netAddress = "http://localhost:80";

    private DataManagement() {
        loader = new DataLoader[DataType.values().length];
    }

    public static DataManagement getInstance() {
        return instance;
    }

    public Uri GetUriOf(DataType type, long id) {
        Uri goal = local.UriOf(type, id);
        if (goal != null) {
            return goal;
        }
        return net.UriOf(type, id);
    }

    public void Cache(Data data) {
        local.cacheData(data);
    }

    public void Start(Context ctx) {
        if (local != null) return;
        synchronized (this) {
            if (local == null) {
                local = new LocalSource(ctx.getFilesDir().getAbsolutePath());
                net = new NetSource(netAddress);
            }
        }
    }

    public void Clear() {
        for (DataType type : DataType.values()) {
            local.clear(type);
        }
    }

    public synchronized void RegisterLoader(DataType type, Class<? extends DataLoader> c) throws Exception {
        if (loader[type.ordinal()] == null) {
            loader[type.ordinal()] = c.newInstance();
        }
    }

    /*
    if you can not get Data form here, and a message can see in the logcat
    please register the loader first.
     */
    public Data LoadData(Context ctx, DataType type, long id) {
        DataLoader loader = this.loader[type.ordinal()];
        if (loader == null) {
            Log.d("easyDesign_Bug", "LoadData: do not register DataLoader for type: " + type.path);
            return null;
        }
        InputStream stream = local.Load(type, id);
        if (stream != null) {
            try {
                return loader.LoadFromCache(ctx, stream, id);
            } catch (Exception ignored) {
            }
        }
        stream = net.Load(type, id);
        if (stream != null) {
            try {
                Data goal = loader.LoadFromNet(ctx, stream, id);
                local.cacheData(goal);
                return goal;
            } catch (Exception ignored) {
            }
        }
        return null;
    }

}
