package cn.edu.hebut.easydesign.DataManager;


import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;

public class DataManager {
    private static DataManager instance = new DataManager();
    private String filePath;

    private DataManager() {
        filePath = ContextHolder.getContext().getExternalFilesDir(null).getAbsolutePath();
        Log.i("DM", filePath);
    }

    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();

                }
            }
        }
        return instance;
    }

    /*
    clean all the cache
     */
    public void clean() {
        for (DataType type : DataType.values()) {
            clean(type);
        }
    }


    InputStream load(DataType type, long id) {
        File f = new File(makePath(type, id));
        Log.i("dm", "load: " + f.exists());
        if (!f.exists()) {
            return null;
        }
        if (type.keepTime != 0) {
            if ((new Date()).getTime() - f.lastModified() > type.keepTime) {
                return null;
            }
        }
        try {
            return new FileInputStream(f);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public void clean(DataType type) {
        File f = new File(makePath(type));
        delDir(f);
    }

    private void delDir(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                delDir(f);
            }
        }
        if (!file.delete()) {
            file.deleteOnExit();
        }
    }

    private String makePath(DataType type, long id) {
        return filePath + File.separator +
                type.path +
                File.separator +
                id;
    }

    private String makePath(DataType type) {
        return filePath + File.separator +
                type.path +
                File.separator;
    }

    boolean cacheData(Data data) {
        File f = null;
        FileOutputStream stream = null;
        try {
            f = new File(makePath(data.GetType(), data.GetId()));
            Log.i("ED", "cacheData: " + f.getParent());
            f.getParentFile().mkdir();
            f.createNewFile();
            stream = new FileOutputStream(f);
            data.cache(stream);
        } catch (Exception e) {
            if (f != null) {
                if (f.exists()) {
                    f.delete();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ignored) {

            }
        }
        return true;
    }

}
