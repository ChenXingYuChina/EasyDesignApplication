package cn.edu.hebut.easydesign.Activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.Activity.Fragment.ExamplePage.ExampleFragment;
import cn.edu.hebut.easydesign.Activity.Fragment.HomePage.HomeFragment;
import cn.edu.hebut.easydesign.Activity.Fragment.ThinkingFragment;
import cn.edu.hebut.easydesign.DataManagement.DataManagement;
import cn.edu.hebut.easydesign.DataManagement.DataType;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Passage.Passage;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMiniLoader;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class MainActivity extends HoldContextAppCompatActivity implements View.OnClickListener {
    private ServiceConnection connection;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private Fragment homePage = null;
    private Fragment thinkingPage = null;
    private Fragment examplePage = null;
    private Fragment userPage = null;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        fm = getSupportFragmentManager();
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
                if (savedInstanceState == null) {
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.add(R.id.main_content, HomeFragment.getInstance());
                    transaction.commit();
                }
                initBottomNav();
//                home.listView.init(new HotByType((short) 0), null);
//                Log.i("main", "onCreate: " + listView);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                ContextHolder.setBinder(null);
            }
        };
        bindService(new Intent(this, TaskService.class), connection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    private void backFragment(Fragment fragment) {
        for (Fragment f : fm.getFragments()) {
            if (f != null && !f.isHidden() && f != fragment) {
                transaction.hide(f);
            }
        }
        if (fragment != null) {
            if (fragment.isHidden()) {
                transaction.show(fragment);
            } else if (fragment.isDetached()) {
                transaction.attach(fragment);
            }
        }
    }

    private void initBottomNav() {
        findViewById(R.id.home_page).setOnClickListener(this);
        findViewById(R.id.thinking_page).setOnClickListener(this);
        findViewById(R.id.publish_page).setOnClickListener(this);
        findViewById(R.id.example_page).setOnClickListener(this);
        findViewById(R.id.user_page).setOnClickListener(this);
    }

    int nowPage = R.id.home_page;

    @Override
    public void onClick(View v) {
        int toPage = v.getId();
        if (nowPage == toPage) {
            return;
        } else if (toPage == R.id.publish_page) {
            // TODO: 2020/2/13 start the publish activity
        }
        transaction = fm.beginTransaction();
        switch (toPage) {
            case R.id.home_page:
                nowPage = R.id.home_page;
                if (homePage == null) {
                    backFragment(null);
                    homePage = HomeFragment.getInstance();
                    transaction.add(R.id.main_content, homePage);
                } else {
                    backFragment(homePage);
                }
                break;
            case R.id.thinking_page:
                if (thinkingPage == null) {
                    backFragment(null);
                    thinkingPage = ThinkingFragment.getInstance();
                    transaction.add(R.id.main_content, thinkingPage);
                } else {
                    backFragment(thinkingPage);
                }
                nowPage = R.id.thinking_page;
                break;
            case R.id.example_page:
                if (examplePage == null) {
                    backFragment(null);
                    examplePage = ExampleFragment.getInstance();
                    transaction.add(R.id.main_content, examplePage);
                } else {
                    backFragment(examplePage);
                }
                nowPage = R.id.example_page;
                break;
            case R.id.user_page:
//                nowPage = R.id.user_page;
                break;
        }
        transaction.commit();
    }
}



