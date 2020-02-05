package cn.edu.hebut.easydesign.Activity.PassageList;

import android.widget.TextView;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;

public abstract class PassageListViewConfig {
    protected TextField[] fields;
    protected TextField[] refreshFields;
    protected byte length = 10;
    protected abstract FormField[] getFields(int begin);
    protected abstract FormField[] getRefreshFields(long lastTime);

    @Override
    public abstract boolean equals(@Nullable Object obj);

    @Override
    public abstract int hashCode();
}
