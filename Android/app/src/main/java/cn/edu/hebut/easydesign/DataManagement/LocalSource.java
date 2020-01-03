package cn.edu.hebut.easydesign.DataManagement;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

class LocalSource implements DataSource {
    private String filePath;
    private static final long keepTime = 1000 * 24 * 3600;

    LocalSource(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Uri UriOf(DataType type, long id) {
        if (type.keepTime != 0) {
            String path = makePath(type, id);
            File f = new File(path);
            if (!f.exists()) {
                return null;
            }
            if ((new Date()).getTime() - f.lastModified() > keepTime) {
                if (!f.delete()) {
                    f.deleteOnExit();
                }
                return null;
            }
            return Uri.parse("file://" + path);
        } else {
            String path = makePath(type, id);
            File f = new File(path);
            if (!f.exists()) {
                return null;
            }
            return Uri.parse("file://" + path);
        }
    }

    @Override
    public InputStream Load(DataType type, long id) {
        if (type.keepTime != 0) {
            File f = new File(makePath(type, id));
            if (!f.exists()) {
                return null;
            }
            if ((new Date()).getTime() - f.lastModified() > keepTime) {
                if (!f.delete()) {
                    f.deleteOnExit();
                }
                return null;
            }
            try {
                return new FileInputStream(f);
            } catch (FileNotFoundException e) {
                return null;
            }
        } else {
            File f = new File(makePath(type, id));
            if (!f.exists()) {
                return null;
            }
            try {
                return new FileInputStream(f);
            } catch (FileNotFoundException e) {
                return null;
            }
        }
    }

    void clear(DataType type) {
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
        try {
            f = new File(makePath(data.GetType(), data.GetId()));
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(f));
            stream.writeObject(data);
            stream.close();
        } catch (Exception e) {
            if (f != null) {
                if (f.exists()) {
                    f.delete();
                }
            }
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
