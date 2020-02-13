package cn.edu.hebut.easydesign.Activity.PassageList.Page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListContainer;

public class SamplePage implements Page {
    private @LayoutRes int layout;
    private View view;
    public SamplePage(@LayoutRes int layout) {
        this.layout = layout;
    }

    @Override
    public boolean canRefresh() {
        return false;
    }

    @Override
    public void bind(@NonNull PassageListContainer father) {
        view = LayoutInflater.from(ContextHolder.getContext()).inflate(layout, null);
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onRefresh() {

    }
}
