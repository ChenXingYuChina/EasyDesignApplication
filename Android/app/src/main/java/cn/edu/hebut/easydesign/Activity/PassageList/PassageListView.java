package cn.edu.hebut.easydesign.Activity.PassageList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.edu.hebut.easydesign.R;

public class PassageListView extends FrameLayout {
    /*package*/ PassageListAdapter adapter;
    /*package*/ SwipeRefreshLayout swipe;
    /*package*/ RecyclerView main;
    PassageListViewPerformance performance;
    PassageListData data;
    PassageListViewConfig config;
    private Context context;

    public PassageListView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflate(context, R.layout.passage_list, this);
        swipe = findViewById(R.id.list_swipe);
        main = findViewById(R.id.list_main);
        performance = PassageListViewPerformance.getFromAttributeSet(attrs);
    }

    private void makeList() {
        switch (performance.layout) {
            case 0:
                main.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                break;
            case 1:
                GridLayoutManager manager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
                manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (position == data.size() + 1 || position == 0) {
                            return 2;
                        }
                        return 1;
                    }
                });
                main.setLayoutManager(manager);
        }
    }

    public void init(PassageListViewConfig viewConfig, PassageListViewPerformance performance) {
        performance = performance != null?performance:this.performance;
        config = viewConfig;
        makeList();
        data = new PassageListData(viewConfig);
        adapter = new PassageListAdapter(performance.card, performance.head, this, data.passageList, data.userMinis);
        main.setAdapter(adapter);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.refresh(PassageListView.this);
            }
        });
        swipe.setRefreshing(true);
        data.refresh(PassageListView.this);
    }

    public void changeDataSet(PassageListViewConfig viewConfig, PassageListViewPerformance performance) {
        this.performance = performance!=null?performance:this.performance;
        if (config == null) {
            throw new IllegalArgumentException();
        }
        if (config.equals(viewConfig)) {
            return;
        }
        init(viewConfig, null);
    }

    public void toHead() {
        main.scrollToPosition(0);
    }

}
