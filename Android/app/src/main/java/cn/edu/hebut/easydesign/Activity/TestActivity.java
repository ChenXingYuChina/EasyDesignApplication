package cn.edu.hebut.easydesign.Activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.Activity.Fragment.UserPage.UserDescriptionPage;
import cn.edu.hebut.easydesign.Activity.Fragment.UserPage.UserFragment;
import cn.edu.hebut.easydesign.ComplexString.ComplexStringLoader;
import cn.edu.hebut.easydesign.DataManagement.DataManagement;
import cn.edu.hebut.easydesign.DataManagement.DataType;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMiniLoader;
import cn.edu.hebut.easydesign.Session.Session;
import cn.edu.hebut.easydesign.Session.User.User;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class TestActivity extends HoldContextAppCompatActivity {
    ServiceConnection connection;
    FragmentManager fm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        try {
            DataManagement.getInstance().RegisterLoader(DataType.UserMini, UserMiniLoader.class);
            DataManagement.getInstance().Start(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                TaskService.MyBinder binder = (TaskService.MyBinder) service;
                ContextHolder.setBinder(binder);
                try {
                    JSONObject user = new JSONObject("{\"id\":50,\"name\":\"你的名字。\",\"Password\":\"\",\"phone\":\"\",\"email\":\"2@designer.com\",\"coin\":0,\"fans_number\":0,\"follow_number\":0,\"passage_number\":0,\"head_image\":0,\"back_image\":0,\"identity\":{\"works\":[{\"Id\":13,\"start\":\"2019-01-01T00:00:00Z\",\"end\":\"0001-01-01T00:00:00Z\",\"company\":\"company\",\"industry\":\"it\",\"position\":\"tester\"}]}}");
                    JSONObject description = new JSONObject("{\"content\":\"abcdefghijklmnopqrstuvwxyz\",\"position\":[0,1,2,3,4,5,6,7,20],\"width\":[1,1,1,1,1,1,1,10,2],\"resources\":[{\"id\":0},{\"id\":1},{\"id\":2},{\"id\":3},{\"id\":4},{\"id\":7},{\"id\":12},{\"id\":17,\"url\":\"http://www.baidu.com\"},{\"id\":19}]}");
                    Session session = Session.getSession();
                    session.longDescription = ComplexStringLoader.getInstance().LoadFromNet(description);
                    session.user = new User(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                UserDescriptionPage page = new UserDescriptionPage();
                    UserFragment p = UserFragment.getInstance(UserFragment.NOW_USER);
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.add(R.id.test_content, p);
                    transaction.commit();
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

    private void init() {
//        ImageView back = findViewById(R.id.user_back);
//        ImageView head = findViewById(R.id.user_head);
        TextView name = findViewById(R.id.user_name);
        name.setText(Session.getSession().user.name);
    }
}
