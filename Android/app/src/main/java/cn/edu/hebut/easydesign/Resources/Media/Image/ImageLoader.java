package cn.edu.hebut.easydesign.Resources.Media.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

import cn.edu.hebut.easydesign.DataManager.DataLoader;
import cn.edu.hebut.easydesign.DataManager.DataType;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.LongField;

public class ImageLoader extends DataLoader<Image> {
    private ImageLoader() {
        super();
    }

    private static final ImageLoader instance = new ImageLoader();

    public static ImageLoader getInstance() {
        return instance;
    }

    public Bitmap load(long id) {
        return loadData(DataType.Image, id).image;
    }

    @Override
    protected Form makeForm(DataType type, long id, Object... extraArgs) {
        return (new Form()).addFields(new LongField("id", id));
    }

    @Override
    protected Image buildDataFromNet(byte[] data, long id) {
        Image goal = new Image();
        goal.data = data;
        goal.id = id;
        goal.image = BitmapFactory.decodeByteArray(data, 0, data.length);
        return goal;
    }

    @Override
    protected Image rebuildFromCache(InputStream stream, long id) {
        Image goal = new Image();
        goal.image = BitmapFactory.decodeStream(stream);
        goal.id = id;
        return goal;
    }
}
