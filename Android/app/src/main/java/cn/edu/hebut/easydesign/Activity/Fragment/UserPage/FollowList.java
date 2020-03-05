package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.TaskWorker.Condition;

public class FollowList extends FrameLayout {
    long cachedId;
    RecyclerView recyclerView;
    FollowListAdapter adapter;
    Condition<Boolean> cancel;
    public FollowList(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.follow_list_layout, this);
        recyclerView = findViewById(R.id.follow_list);
    }
    public void setWho(long who, Condition<Boolean> cancel) {
        if (cachedId == who) {
            return;
        }
        cachedId = who;
        if (this.cancel != null) {
            this.cancel.condition = true;
        }
        this.cancel = cancel;
        ContextHolder.getBinder().PutTask(new LoadFollowListTask(who, cancel) {
            @Override
            protected void handleResult(List<UserMini> userMinis) {
                if (adapter == null) {
                    Log.i("Follow", "handleResult: ");
                    adapter = new FollowListAdapter(userMinis);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.changeData(userMinis);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(ContextHolder.getContext()));
    }
}
