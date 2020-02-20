package cn.edu.hebut.easydesign.DataManager;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import cn.edu.hebut.easydesign.HttpClient.Client;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import okhttp3.Response;

/*
    if you use this loader, extend it and provide a public method as load function for pass arguments and get data
    如果你需要使用本加载器框架，则需要继承此类，并提供一个公开的方法作为加载函数，来传递参数和获取结果。
    like:
    如此：
    public goal loadData(long id, ...){}
 */
public abstract class DataLoader<T extends Data> {
    private DataManager dataManager;

    protected DataLoader() {
        dataManager = DataManager.getInstance();
    }

    /*
        if you use the extraArgs, must make an overload loadData for loading data which uses TYPED arguments.
        如果你使用额外的参数，请必须重载函数来获得一个具有确定类型参数的函数
    */
    protected T loadData(DataType type, long id, Object... extraArgs) {
        InputStream stream = dataManager.load(type, id);
        if (stream != null) {
            T goal = rebuildFromCache(stream, id);
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return goal;
        }
        try {
            Response response = Client.getInstance().PostToHost(type.path, makeForm(type, id, extraArgs));
            if (response.body() == null) {

                Log.i("DL", "the response: null");
                return null;
            }
            Log.i("DL", "the response:");
            T goal = buildDataFromNet(response.body().bytes(), id);
            if (goal != null) {
                dataManager.cacheData(goal);
            }
            return goal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
        if you use the extraArgs, must make an overload loadData for loading data which uses TYPED arguments.
        如果你使用额外的参数，请必须重载函数来获得一个具有确定类型参数的函数
        the data of arguments are the data passed from loadData.
        该函数的参数直接传递自loadData函数
    */
    protected abstract Form makeForm(DataType type, long id, Object... extraArgs);

    protected abstract T buildDataFromNet(byte[] data, long id);

    protected abstract T rebuildFromCache(InputStream stream, long id);
}
