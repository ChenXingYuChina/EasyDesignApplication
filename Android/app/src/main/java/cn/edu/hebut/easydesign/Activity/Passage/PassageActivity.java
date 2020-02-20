package cn.edu.hebut.easydesign.Activity.Passage;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Passage.LoadPassageTask;
import cn.edu.hebut.easydesign.Resources.Passage.Passage;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageListItem;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class PassageActivity extends HoldContextAppCompatActivity {
    private TextView content, doComment, showAllComments;
    private ExpandableListView comments;
    private UserMini userMini;
    private PassageListItem item;
    private ServiceConnection connection;
    private Condition<Boolean> cancel = new Condition<>(false);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passage_activity_layout);
        content = findViewById(R.id.passage_content);
//        comments = findViewById(R.id.passage_comments);
        doComment = findViewById(R.id.do_comment);
        showAllComments = findViewById(R.id.show_all_comments);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            item = (PassageListItem) bundle.getSerializable("item");
            userMini = (UserMini) bundle.getSerializable("userMini");
            Log.i("PA", "onCreate: " + item);
        } else {
            Toast.makeText(this, "内部错误", Toast.LENGTH_LONG).show();
            finish();
        }
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                TaskService.MyBinder binder = (TaskService.MyBinder) service;
                ContextHolder.setBinder(binder);
                binder.PutTask(new LoadPassageTask(item.id, item.type, cancel) {
                    @Override
                    protected void onError() {
                        Toast.makeText(PassageActivity.this, "出错了", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected void onSuccess(Passage passage) {
                        content.setText(passage.content.GetSpannableString());
                    }
                });
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
}
