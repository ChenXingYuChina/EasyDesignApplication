package cn.edu.hebut.easydesign.DataManagement;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class DataManagement {
    private static DataManagement instance = new DataManagement();
    private LocalSource local;
    private HttpSource net;
    private DataLoader[] loader;

    public static final String netAddress = "192.168.31.216";

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
                local = new LocalSource(ctx.getExternalFilesDir(null).getAbsolutePath());
                net = new HttpSource(netAddress);
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
    public <D extends Data> D LoadData(DataType type, long id, Object... extraArgs) {
        DataLoader loader = this.loader[type.ordinal()];
        if (loader == null) {
            Log.d("easyDesign_Bug", "LoadData: do not register DataLoader for type: " + type.path);
            return null;
        }
        InputStream stream = local.Load(type, id);
        if (stream != null) {
            try {
                return loader.LoadFromCache(stream, id);
            } catch (Exception ignored) {
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("DATA", "no in cache");
        String r = net.Load(type, id);
        if (r != null) {
            try {
                D goal = loader.LoadFromNet(r, id, extraArgs);
                Log.i("DATA", "goal: " + goal);
                local.cacheData(goal);
                return goal;
            } catch (Exception e) {
                Log.i("DATA", "error: " + e);
            }
        }

        return null;
    }

    public Data LoadFromLocal(DataType type, long id) {
        DataLoader loader = this.loader[type.ordinal()];
        if (loader == null) {
            Log.d("easyDesign_Bug", "LoadData: do not register DataLoader for type: " + type.path);
            return null;
        }
        InputStream stream = local.Load(type, id);
        if (stream != null) {
            try {
                return loader.LoadFromCache(stream, id);
            } catch (Exception ignored) {
            }
        }
        return null;
    }
    /*
    force load data from net and update cache
     */
    public Data LoadDataFromNet(DataType type, long id) {
        DataLoader loader = this.loader[type.ordinal()];
        String r = net.Load(type, id);
        if (r != null) {
            try {
                Data goal = loader.LoadFromNet(r, id);
                local.cacheData(goal);
                return goal;
            } catch (Exception ignored) {
            }
        }
        return null;
    }


    public boolean inCache(DataType type, long id) {
        return local.inCache(type, id);
    }
}
