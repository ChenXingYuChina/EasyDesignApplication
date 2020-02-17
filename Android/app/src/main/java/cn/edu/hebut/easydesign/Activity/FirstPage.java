package cn.edu.hebut.easydesign.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ActivityTask.DelayJumpTask;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextActivity;
import cn.edu.hebut.easydesign.DataManagement.DataManagement;
import cn.edu.hebut.easydesign.HttpClient.Client;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Media.Image.ImageHostLoadTask;
import cn.edu.hebut.easydesign.Session.LoginTask;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FirstPage extends HoldContextActivity {
    private ServiceConnection connection;
    private Condition<Boolean> cancel = new Condition<>(false);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        final ImageView view = findViewById(R.id.helloImage);
        view.setImageResource(R.drawable.yindao1);
        DataManagement.getInstance().Start(this);
//        DataManagement.getInstance().Clear();
        startService(new Intent(this, TaskService.class));
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, final IBinder service) {
                final Condition<Integer> c = new Condition<>();
                ContextHolder.setBinder((TaskService.MyBinder) service);

//                c.condition = checkVersion()?2:1;
                c.condition = 2;

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
                        finish();
                    }
                });
                ((TaskService.MyBinder) service).PutTask(new ImageHostLoadTask(cancel) {
                    @Override
                    protected long getId() {
                        this.binder = (TaskService.MyBinder) service;
                        Response r = null;
                        try {
                            r = Client.getInstance().GetFromHost("firstPageImage");
                            if (Client.GetStatusCode(r) == 200) {
                                ResponseBody body = r.body();
                                if (body != null) {
                                    Log.i("ED", "getId: success");
                                    return Long.valueOf(body.string());
                                }
                            }
                        } catch (Exception e) {
                            return 0;
                        } finally {
                            if (r != null) {
                                r.close();
                            }
                        }
                        return 0;
                    }

                    @Override
                    protected void setImage(Bitmap bitmap) {
                        view.setImageBitmap(data2);
                    }
                });
                SharedPreferences read = getSharedPreferences("loginInformation", MODE_PRIVATE);
                int id = read.getInt("id", 50);
                String pw = read.getString("pw", "hello world");
                Log.i("ED", "loginInformation " + id +" " + pw);
                try {
                    ((TaskService.MyBinder) service).PutTask(new LoginTask(id, pw) {
                        @Override
                        protected void doOnMain() {
                            if (c.condition != 1) {
                                c.condition = this.condition.condition;
                            }
                            Log.i("firstPage", c.condition + "");
                        }
                    });
                } catch (Exception e) {
                    Log.e("ED", "onServiceConnected: "+e);
                }
            }


            @Override
            public void onServiceDisconnected(ComponentName name) {
                ContextHolder.setBinder(null);
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

        cancel.condition = false;
    }
}
