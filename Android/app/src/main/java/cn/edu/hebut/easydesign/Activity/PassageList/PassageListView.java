package cn.edu.hebut.easydesign.Activity.PassageList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
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
    protected String[] texts;
    protected static final int text_foot_onError = 0;
    protected static final int text_foot_onNoNew = 1;
    protected static final int text_foot_onLoading = 2;
    protected static final int text_foot_toLoad = 3;
    protected static final int text_refresh_onError = 4;
    protected static final int text_refresh_onNoNew = 5;


    private Map<PassageListViewConfig, PassageListDataSet> cache = new HashMap<>();
    private Context context;

    public PassageListView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflate(context, R.layout.passage_list, this);
        swipe = findViewById(R.id.list_swipe);
        main = findViewById(R.id.list_main);
        defaultPerformance = PassageListViewPerformance.getFromAttributeSet(attrs);
        texts = new String[6];
        texts[text_foot_onError] = getStringFromAttrs(attrs, "text_foot_onError", R.string.error);
        texts[text_foot_onNoNew] = getStringFromAttrs(attrs, "text_foot_onNoNew", R.string.finish_list);
        texts[text_foot_onLoading] = getStringFromAttrs(attrs, "text_foot_onLoading", R.string.loading_list);
        texts[text_foot_toLoad] = getStringFromAttrs(attrs, "text_foot_toLoad", R.string.load);
        texts[text_refresh_onError] = getStringFromAttrs(attrs, "text_refresh_onError", R.string.error);
        texts[text_refresh_onNoNew] = getStringFromAttrs(attrs, "text_refresh_onNoNew", R.string.no_news);
    }

    private String getStringFromAttrs(AttributeSet attrs, String key, @StringRes int defaultValue) {
        String attr = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", key);
        if (attr == null || attr.matches("@[a-zA-Z]+/")) {
            attr = context.getResources().getString(defaultValue);
        } else if (attr.startsWith("@string/")) {
            attr = context.getResources().getString(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", key, defaultValue));
        }
        return attr;
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

    public void init(@NonNull PassageListViewConfig viewConfig, @Nullable PassageListViewPerformance performance) {
        this.performance = performance != null ? performance : defaultPerformance;
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
