package cn.edu.hebut.easydesign.Activity.PassageList.Page;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListAdapter;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListContainer;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListViewConfig;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListViewPerformance;
import cn.edu.hebut.easydesign.Activity.PassageList.TipResources;

public class PassageListPage implements Page {
    private PassageListAdapter adapter;
    private RecyclerView recyclerView;
    private PassageListViewConfig config;
    private PassageListViewPerformance performance;
    private TipResources tips;

    public PassageListPage(PassageListViewConfig config) {
        this(null, config, null);
    }

    public PassageListPage(PassageListViewPerformance performance, PassageListViewConfig config, TipResources tips) {
        this.tips = tips;
        this.config = config;
        this.performance = performance;
    }

    public void bind(@NonNull PassageListContainer father) {
        recyclerView = new RecyclerView(ContextHolder.getContext());
        adapter = father.makeList(recyclerView, config, performance, tips);
    }

    @Override
    public View getView() {
        return recyclerView;
    }

    @Override
    public boolean canRefresh() {
        return true;
    }

    @Override
    public void onRefresh() {
        Log.i("refresh", "" + config);
        adapter.onRefresh();
    }
}
