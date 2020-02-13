package cn.edu.hebut.easydesign.Activity.PassageList;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.Activity.PassageList.Config.LastByType;
import cn.edu.hebut.easydesign.Activity.PassageList.Page.Page;
import cn.edu.hebut.easydesign.Activity.PassageList.Page.PassageListPage;
import cn.edu.hebut.easydesign.DataManagement.DataManagement;
import cn.edu.hebut.easydesign.DataManagement.DataType;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMiniLoader;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class TestActivity extends HoldContextAppCompatActivity {
    PassageMultiListView page;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        page = findViewById(R.id.test_list);
        try {
            DataManagement.getInstance().RegisterLoader(DataType.UserMini, UserMiniLoader.class);
            DataManagement.getInstance().Start(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ContextHolder.setBinder((TaskService.MyBinder) service);
                List<Page> pages = new LinkedList<>();
                pages.add(new PassageListPage(new LastByType((short) 0)));
//                page.init(pages, new TestGroup(TestActivity.this));
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                ContextHolder.setBinder(null);
            }
        };
        bindService(new Intent(this, TaskService.class), connection, Service.BIND_AUTO_CREATE);
    }

//    public static class TestGroup extends FixedPart {
//        RadioGroup group;
//        public TestGroup(@NonNull Context context) {
//            super(context, null);
//            inflate(context, R.layout.example_radio_group, this);
//            group = findViewById(R.id.radio_group);
//            RadioButton button = new RadioButton(context);
//            group.addView(button);
//            button.setId(0);
//            button.setText("点击加载");
//
//            Log.i("TG", "" + group.getChildCount());
//        }
//
//        @Override
//        public RadioGroup getGroup() {
//            return group;
//        }
//    }
}
