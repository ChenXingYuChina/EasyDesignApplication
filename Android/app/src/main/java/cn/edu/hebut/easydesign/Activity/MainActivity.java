package cn.edu.hebut.easydesign.Activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.Fragment.HomeFragment;
import cn.edu.hebut.easydesign.Activity.PassageList.Config.HotByType;
import cn.edu.hebut.easydesign.DataManagement.DataManagement;
import cn.edu.hebut.easydesign.DataManagement.DataType;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMiniLoader;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class MainActivity extends AppCompatActivity {
    ServiceConnection connection;
    FragmentManager fm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
                final HomeFragment home = (HomeFragment) fm.findFragmentById(R.id.main_content);
                home.listView.init(new HotByType((short) 0), null);
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

    private void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();//开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.fragment , fragment);
        transaction.commit();
    }


}



