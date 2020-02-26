package cn.edu.hebut.easydesign.Activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.Activity.PassageList.Config.Search;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListView;
import cn.edu.hebut.easydesign.Activity.commonComponents.SearchBar;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class SearchResultActivity extends HoldContextAppCompatActivity {
    PassageListView listView;
    ServiceConnection connection;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);
        final Intent search = getIntent();
        final String keyword;
        if (search.hasExtra("keyword")) {
            keyword = search.getStringExtra("keyword");
        } else {
            Log.i("search", "no keyword");
            finish();
            return;
        }
        listView = findViewById(R.id.search_result);

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ContextHolder.setBinder((TaskService.MyBinder) service);
                SearchResultActivity.this.binder = (TaskService.MyBinder) service;
                listView.init(new Search(keyword));
                listView.disableRefresh();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                ContextHolder.setBinder(null);
            }
        };
        bindService(new Intent(this, TaskService.class), connection, Service.BIND_AUTO_CREATE);
        SearchBar searchView = findViewById(R.id.search);
        searchView.setHint(keyword + "  的搜索结果");
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        ContextHolder.setBinder(null);
    }
}
