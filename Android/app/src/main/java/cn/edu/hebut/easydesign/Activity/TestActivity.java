package cn.edu.hebut.easydesign.Activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.Activity.Fragment.ExamplePage.ExampleFragment;
import cn.edu.hebut.easydesign.Activity.PassageList.Config.LastByType;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListView;
import cn.edu.hebut.easydesign.DataManagement.DataManagement;
import cn.edu.hebut.easydesign.DataManagement.DataType;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMiniLoader;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class TestActivity extends HoldContextAppCompatActivity {
    ServiceConnection connection;
    FragmentManager fm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_fragment);
        try {
            DataManagement.getInstance().RegisterLoader(DataType.UserMini, UserMiniLoader.class);
            DataManagement.getInstance().Start(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ContextHolder.setBinder((TaskService.MyBinder) service);
                PassageListView listView = findViewById(R.id.user_description);
//                listView.init(new LastByType((short) 0), null);
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
        ContextHolder.setBinder(null);
    }
}
