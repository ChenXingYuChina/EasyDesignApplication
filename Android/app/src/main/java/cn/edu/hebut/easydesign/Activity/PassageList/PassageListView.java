package cn.edu.hebut.easydesign.Activity.PassageList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Tools.AttributeSetTools;

public class PassageListView extends PassageListContainer {
    /* the widgets */
    /*package*/ RecyclerView main;

    PassageListAdapter adapter;

    public PassageListView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflate(context, R.layout.passage_list, this);
        swipe = findViewById(R.id.list_swipe);

        main = findViewById(R.id.list_main);
    }

    public void init(@NonNull PassageListViewConfig viewConfig) {
        init(viewConfig, null, null);
    }

    public void init(@NonNull PassageListViewConfig viewConfig, @Nullable PassageListViewPerformance performance, @Nullable TipResources tips) {
        adapter = makeList(main, viewConfig, performance, tips);
        swipe.setOnRefreshListener(adapter);
        refresh();
    }

    public void toHead() {
        // todo implement it to jump to the position 0 in some day.
    }

    public void refresh() {
        swipe.setRefreshing(true);
        adapter.refresh();
    }

    public void disableRefresh() {
        swipe.setEnabled(false);
    }

}
