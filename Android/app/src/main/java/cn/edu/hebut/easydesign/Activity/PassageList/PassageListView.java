package cn.edu.hebut.easydesign.Activity.PassageList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.edu.hebut.easydesign.R;

public class PassageListView extends FrameLayout {
    /* the widgets */
    /*package*/ SwipeRefreshLayout swipe;
    /*package*/ RecyclerView main;

    /* the data set now*/
    /*package*/ PassageListViewPerformance defaultPerformance;
    /*package*/ PassageListViewPerformance performance;
    /*package*/ PassageListAdapter adapter;
    /*package*/ PassageListData data;
    /*package*/ PassageListViewConfig config;

    private Map<PassageListViewConfig, PassageListDataSet> cache = new HashMap<>();
    private Context context;

    public PassageListView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflate(context, R.layout.passage_list, this);
        swipe = findViewById(R.id.list_swipe);
        main = findViewById(R.id.list_main);
        defaultPerformance = PassageListViewPerformance.getFromAttributeSet(attrs);
    }

    private void makeLayout() {
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
        this.performance = performance != null?performance:defaultPerformance;
        config = viewConfig;
        makeLayout();
        data = new PassageListData(viewConfig);
        adapter = new PassageListAdapter(this.performance, this, data.passageList, data.userMinis);
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

    public void toHead() {
        // todo implement it to jump to the position 0 in some day.
    }

    public void changeDataSet(@NonNull PassageListViewConfig newConfig, PassageListViewPerformance performance) {
        if (config.equals(newConfig)) {
            return;
        } else {
            PassageListDataSet set = cache.get(newConfig);
            if (set != null) {
                this.data = set.data;
                this.performance = set.performance;
                this.config = set.config;
                this.adapter = set.adapter;
                makeLayout();
                main.setAdapter(adapter);
                swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        data.refresh(PassageListView.this);
                    }
                });
                swipe.setRefreshing(true);
                data.refresh(PassageListView.this);
            } else {
                cache.put(config, new PassageListDataSet(this.performance, this.adapter, this.data, this.config));
                this.performance = performance;
                init(newConfig, performance);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void refresh() {
        swipe.setRefreshing(true);
        data.refresh(this);

    }

    private class PassageListDataSet {
        PassageListViewPerformance performance;
        PassageListAdapter adapter;
        PassageListData data;
        PassageListViewConfig config;

        PassageListDataSet(PassageListViewPerformance performance, PassageListAdapter adapter, PassageListData data, PassageListViewConfig config) {
            this.adapter = adapter;
            this.config = config;
            this.data = data;
            this.performance = performance;
        }
    }

    public void disableRefresh() {
        swipe.setEnabled(false);
    }

}
