package cn.edu.hebut.easydesign.Activity.Passage;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.Activity.MainActivity;
import cn.edu.hebut.easydesign.Activity.Passage.PassageTask.FollowUser;
import cn.edu.hebut.easydesign.Activity.Passage.PassageTask.LoadCommentTask;
import cn.edu.hebut.easydesign.Activity.Passage.PassageTask.StarPassageTask;
import cn.edu.hebut.easydesign.Activity.commonComponents.HalfAboveDialog;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.PassageMetaHelper;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.UserMiniHelper;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.Media.Image.ImageHostLoadTask;
import cn.edu.hebut.easydesign.Resources.Passage.Comment;
import cn.edu.hebut.easydesign.Resources.Passage.LoadPassageTask;
import cn.edu.hebut.easydesign.Resources.Passage.Passage;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageListItem;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class PassageActivity extends HoldContextAppCompatActivity implements CommentListAdapter.getDialog {

    private TextView content, doComment, starPassage, retry, userNameCopy, followWriter;
    private ViewGroup contentPart;
    private RecyclerView commentList;
    private HalfAboveDialog dialog;
    private FullSubComment fullSubComment;
    private UserMiniHelper userMiniHelper;
    private PassageMetaHelper passageMetaHelper;
    private ImageView userHeadCopy;
    private PassageFixedPart typeSelector;

    private Bundle startBundle;

    private UserMini userMini;
    private PassageListItem item;

    private ServiceConnection connection;
    private Condition<Boolean> cancel = new Condition<>(false);
    private Passage passage = null;
    private CommentListAdapter hotAdapter;
    private CommentListAdapter lastAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passage_activity_layout);
        content = findViewById(R.id.passage_content);
        commentList = findViewById(R.id.passage_comments);
        doComment = findViewById(R.id.do_comment);
        starPassage = findViewById(R.id.star_passage);
        typeSelector = findViewById(R.id.comments_type);
        retry = findViewById(R.id.retry);
        dialog = findViewById(R.id.all_sub_dialog);
        userHeadCopy = findViewById(R.id.user_head_copy);
        userNameCopy = findViewById(R.id.user_name_copy);
        contentPart = findViewById(R.id.content_part);
        followWriter = findViewById(R.id.follow);

        fullSubComment = new FullSubComment(ContextHolder.getContext());

        Intent intent = getIntent();
        startBundle = intent.getExtras();
        if (startBundle != null) {
            item = (PassageListItem) startBundle.getSerializable("item");
            userMini = (UserMini) startBundle.getSerializable("userMini");
            if (item == null || userMini == null) {
                Toast.makeText(this, "内部错误（空）", Toast.LENGTH_LONG).show();
                finish();
            }
            // for test
//            item.id = 100;
        } else {
            if (savedInstanceState != null) {
                startBundle = savedInstanceState.getBundle("last");
            }
            if (startBundle == null) {
                Toast.makeText(this, "内部错误", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
            }
        }


        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                TaskService.MyBinder binder = (TaskService.MyBinder) service;
                PassageActivity.this.binder = binder;
                ContextHolder.setBinder(binder);
                userNameCopy.setText(userMini.name);
                passageMetaHelper = new PassageMetaHelper(contentPart, item, cancel, true);
                loadUserHead();
                initPage(binder);
                setupActions();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                ContextHolder.setBinder(null);
            }
        };
        bindService(new Intent(this, TaskService.class), connection, Service.BIND_AUTO_CREATE);
    }

    public void loadUserHead() {
        binder.PutTask(new ImageHostLoadTask(userMini.headImage, cancel) {
            @Override
            protected void setImage(Bitmap bitmap) {
                userMiniHelper = new UserMiniHelper(contentPart, userMini, bitmap, cancel);
                userHeadCopy.setImageBitmap(bitmap);
            }
        });
    }

    private void initPage(final TaskService.MyBinder binder) {
        commentList.setLayoutManager(new LinearLayoutManager(this));

        binder.PutTask(new LoadPassageTask(item.id, item.type, cancel) {
            @Override
            protected void onError() {
                Toast.makeText(PassageActivity.this, "加载文章出错了", Toast.LENGTH_LONG).show();
                retry();
            }

            @Override
            protected void onSuccess(Passage passage) {
                PassageActivity.this.passage = passage;
                passage.content.SetToTextView(content);
//                Log.i("PA", "onSuccess: comments" + passage.comments);
                if (passage.comments == null) {
                    binder.PutTask(new LoadCommentTask(item.id, cancel) {
                        @Override
                        protected void onSuccess(List<Comment> comments) {
                            PassageActivity.this.passage.comments = comments;
                            setupCommentList(comments);
                        }

                        @Override
                        protected void onError(int errCode) {
                            Toast.makeText(PassageActivity.this, "加载评论失败", Toast.LENGTH_LONG).show();

                        }
                    });
                } else {
                    setupCommentList(passage.comments);
                }
            }
        });
    }

    private void retry() {
        retry.setVisibility(View.VISIBLE);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPage(ContextHolder.getBinder());
                retry.setVisibility(View.GONE);
            }
        });
    }

    private boolean switchToLastAuto = false;

    private void setupCommentList(List<Comment> comments) {
        Log.i("PA", "setupCommentList: " + comments.size());
        hotAdapter = CommentListAdapter.getInstance(comments, item.id, false, fullSubComment);
        hotAdapter.setGetDialog(this);
        commentList.setAdapter(hotAdapter);
//        if (true) {
        if (item.commentNumber > comments.size()) {
            typeSelector.showLast();
            TaskService.MyBinder binder = ContextHolder.getBinder();
            binder.PutTask(new LoadCommentTask(item.id, 0, cancel) {
                @Override
                protected void onSuccess(List<Comment> comments) {
                    lastAdapter = CommentListAdapter.getInstance(comments, item.id, true, fullSubComment);
                    lastAdapter.setGetDialog(PassageActivity.this);
                    if (switchToLastAuto) {
                        commentList.swapAdapter(lastAdapter, true);
                    }
                }

                @Override
                protected void onError(int errCode) {

                }
            });
            typeSelector.setOnChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Log.i("PA", "full: " + typeSelector.full(checkedId));
                    if (typeSelector.full(checkedId)) {
                        if (lastAdapter != null) {
                            commentList.swapAdapter(lastAdapter, true);
                        } else {
                            switchToLastAuto = true;
                            Toast.makeText(PassageActivity.this, "正在加载请稍等", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        switchToLastAuto = false;
                        commentList.swapAdapter(hotAdapter, true);
                    }
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("last", startBundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        cancel.condition = true;
        if (passage != null) {
            passage.content.cancel();
        }
    }

    boolean stared = false;
    boolean followed = false;

    public void setupActions() {
        starPassage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stared) {
                    ContextHolder.getBinder().PutTask(new StarPassageTask(item.id));
                    // TODO: 2020/2/28 改图标
                    stared = true;
                }
            }
        });
        followWriter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!followed) {
                    ContextHolder.getBinder().PutTask(new FollowUser(userMini.id));
                    // TODO: 2020/2/28 改图标
                    followed = true;
                }
            }
        });
    }

    @Override
    public HalfAboveDialog dialog() {
        return dialog;
    }
}
