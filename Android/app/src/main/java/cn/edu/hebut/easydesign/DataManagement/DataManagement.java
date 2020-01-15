package cn.edu.hebut.easydesign.DataManagement;

import android.content.Context;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;

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
        if (data.onCache()) {
            local.cacheData(data);
        }
    }

    public Uri Cache(DataType type, long id) {
        Uri goal = local.UriOf(type, id);
        if (goal != null) {
            return goal;
        }
        String path = net.makePath(type, id);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(path).openConnection();
            InputStream inputStream = connection.getInputStream();
            path = local.makePath(type, id);
            OutputStream outputStream = new FileOutputStream(new File(path));
            int c = 0;
            byte[] buffer = new byte[3 * 1024 * 1024];
            do {
                c = inputStream.read(buffer);
                outputStream.write(buffer);
            } while (c != 3 * 1024 * 1024);
            outputStream.flush();
            outputStream.close();
            return Uri.parse("file://" + path);
        } catch (Exception ignored) {
            return null;
        }

    }

    public void Start(Context ctx) {
        if (local != null) return;
        synchronized (this) {
            if (local == null) {
                local = new LocalSource(ctx.getFilesDir().getAbsolutePath());
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
    public Data LoadData(Context ctx, IBinder binder, DataType type, long id) {
        DataLoader loader = this.loader[type.ordinal()];
        if (loader == null) {
            Log.d("easyDesign_Bug", "LoadData: do not register DataLoader for type: " + type.path);
            return null;
        }
        InputStream stream = local.Load(type, id);
        if (stream != null) {
            try {
                return loader.LoadFromCache(ctx, binder, stream, id);
            } catch (Exception ignored) {
            }
        }
        stream = net.Load(type, id);
        if (stream != null) {
            try {
                Data goal = loader.LoadFromNet(ctx, binder, stream, id);
                local.cacheData(goal);
                return goal;
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /*
    force load data from net and update cache
     */
    public Data LoadDataFromNet(Context ctx, IBinder binder, DataType type, long id) {
        DataLoader loader = this.loader[type.ordinal()];
        InputStream stream = net.Load(type, id);
        if (stream != null) {
            try {
                Data goal = loader.LoadFromNet(ctx, binder, stream, id);
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
