package cn.edu.hebut.easydesign.Activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ActivityTask.DelayJumpTask;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.LoginTask;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class FirstPage extends Activity {
    private ServiceConnection connection;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        ImageView view = findViewById(R.id.helloImage);
        view.setImageResource(R.drawable.yindao1);
//        view.setImageURI(Uri.parse("http://192.168.31.216/testhost"));
        startService(new Intent(this, TaskService.class));
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                final Condition<Integer> c = new Condition<>();
//                c.condition = checkVersion()?2:1;
                c.condition = 2;
                SharedPreferences read = getSharedPreferences("loginInformation", MODE_PRIVATE);
                int id = read.getInt("id", 36);
                String pw = read.getString("pw", "hello world");
                Log.i("ED", "loginInformation " + id +" " + pw);
                try {
                    ((TaskService.MyBinder) service).PutTask(new LoginTask(id, pw) {
                        @Override
                        protected void doOnMain() {
                            if (c.condition != 1)
                                c.condition = this.condition.condition;
                        }
                    });
                } catch (Exception e) {
                    Log.e("ED", "onServiceConnected: "+e);
                }
                Log.i("ED", "condition" + c.condition);
                ((TaskService.MyBinder)service).PutTask(new DelayJumpTask(3 * 1000, c) {
                    @Override
                    protected void doOnMain() {
                        Log.i("ED", "" + this.data1.condition);
                        Intent intent;
                        switch (data1.condition) {
                            case 0:
                                intent = new Intent(FirstPage.this, MainActivity.class);
                                break;
                            case 1:
                                intent = new Intent(FirstPage.this, Splash.class);
                                break;
                            default:
                                intent = new Intent(FirstPage.this, login2.class);
                        }
                        startActivity(intent);
                    }
                });

            }


            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(new Intent(this, TaskService.class), connection, Context.BIND_AUTO_CREATE);
    }

    public static int getVersion(Context ctx) throws Exception {
        return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
    }

    // if not match return false, else return true
    boolean checkVersion() {
        SharedPreferences read = getSharedPreferences("last_version", MODE_PRIVATE);
        int lastVersion = read.getInt("version", 0);
        int version = 0;
        try {
            version = getVersion(this);
        } catch (Exception e) {
            Log.e("firstPage", "checkVersion: ", e);
        }
        return version == lastVersion;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(connection);
    }
}
