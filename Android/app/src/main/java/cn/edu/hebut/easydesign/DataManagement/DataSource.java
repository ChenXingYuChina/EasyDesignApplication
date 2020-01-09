package cn.edu.hebut.easydesign.DataManagement;

import android.net.Uri;

import java.io.InputStream;

interface DataSource {
    Uri UriOf(DataType type, long id);
    InputStream Load(DataType type, long id);
    String makePath(DataType type, long id);
}
