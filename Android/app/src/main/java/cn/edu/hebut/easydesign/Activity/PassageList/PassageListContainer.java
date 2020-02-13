package cn.edu.hebut.easydesign.Activity.PassageList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public abstract class PassageListContainer extends FrameLayout {

    protected SwipeRefreshLayout swipe;
    protected PassageListViewPerformance defaultPerformance;

    protected TipResources defaultTips;


    protected Context context;
    public PassageListContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        defaultPerformance = PassageListViewPerformance.getFromAttributeSet(attrs);
        defaultTips = new TipResources(attrs);
    }

    public PassageListAdapter makeList(RecyclerView view, PassageListViewConfig config, PassageListViewPerformance performance, TipResources tips) {
        performance = performance != null ? performance : defaultPerformance;
        tips = tips != null ? tips : defaultTips;
        PassageListAdapter adapter = new PassageListAdapter(performance, this, config, tips);
        switch (performance.layout) {
            case 0:
                view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                break;
            case 1:
                GridLayoutManager manager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
                manager.setSpanSizeLookup(new SpanLookUp(adapter));
                view.setLayoutManager(manager);
        }
        view.setAdapter(adapter);
        return adapter;
    }

}
