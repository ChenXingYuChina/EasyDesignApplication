package cn.edu.hebut.easydesign.Tools;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.AnyRes;
import androidx.annotation.Px;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;

public class ResourcesTools {


    public static final String RES_ID = "id";
    public static final String RES_STRING = "string";
    public static final String RES_DRAWABLE = "drawable";
    public static final String RES_LAYOUT = "layout";
    public static final String RES_STYLE = "style";
    public static final String RES_COLOR = "color";
    public static final String RES_DIMEN = "dimen";
    public static final String RES_ANIM = "anim";
    public static final String RES_MENU = "menu";

    @AnyRes
    public static int getResFromString(String name, String type) {
        Context context = ContextHolder.getContext();
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }

    public static int px2dp(@Px int value) {
        final float scale = ContextHolder.getContext().getResources().getDisplayMetrics().density;
        return (int) (value / scale + 0.5f);
    }

    public static int dp2px(float value) {
        final float scale = ContextHolder.getContext().getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    private static String[] filePathColumn = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};

    public static String uriToFile(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        File file = null;
        String filePath = "";
        String fileName;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            fileName = cursor.getString(cursor.getColumnIndex(filePathColumn[1]));
            cursor.close();
            if (!TextUtils.isEmpty(filePath)) {
                file = new File(filePath);
            }
            if (file == null || !file.exists() || file.length() <= 0 || TextUtils.isEmpty(filePath)) {
                filePath = getPathFromInputStreamUri(context, uri);
            }
        }
        return filePath;
    }

    public static String getPathFromInputStreamUri(Context context, Uri uri) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                File file = createTemporalFileFrom(context, inputStream);
                filePath = file.getPath();

            } catch (Exception e) {
                Log.e("load", "copy: ", e);
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    Log.e("load", "close: ", e);
                }
            }
        }

        return filePath;
    }

    private static File createTemporalFileFrom(Context context, InputStream inputStream)
            throws IOException {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];
            targetFile = new File(context.getCacheDir()+"mid");
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            } else {
                targetFile.delete();
                targetFile.createNewFile();
            }
            OutputStream outputStream = new FileOutputStream(targetFile);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return targetFile;
    }

}
