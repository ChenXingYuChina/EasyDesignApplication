package cn.edu.hebut.easydesign.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ActivityTask.DelayJumpTask;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.Activity.commonComponents.AlbumImage.SelectImageFromAlbumHelper;
import cn.edu.hebut.easydesign.Activity.commonComponents.AlbumImage.UseAlbumImageByPath;
import cn.edu.hebut.easydesign.Activity.commonComponents.AlbumImage.UseAlumImageByImage;
import cn.edu.hebut.easydesign.Activity.commonComponents.TextButtonRichTextEditor;
import cn.edu.hebut.easydesign.ComplexString.ComplexString;
import cn.edu.hebut.easydesign.ComplexString.ComplexStringField;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.ImageField;
import cn.edu.hebut.easydesign.HttpClient.Form.IntField;
import cn.edu.hebut.easydesign.HttpClient.Form.LongField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.NeedSessionTask.NoReplySessionHostPostTask;
import cn.edu.hebut.easydesign.Session.Session;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class PublicPassageActivity extends HoldContextAppCompatActivity implements View.OnClickListener, UseAlumImageByImage, UseAlbumImageByPath {
    private ImageView listImagePreShow;
    private View selectImage, submit;
    private TextButtonRichTextEditor editor;
    private Spinner typeSelect;
    private EditText titleInput;
    private ServiceConnection connection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_passage);
        listImagePreShow = findViewById(R.id.pre_show_list_image);
        selectImage = findViewById(R.id.select_list_image);
        submit = findViewById(R.id.submit_passage);
        editor = findViewById(R.id.passage_content_editor);
        typeSelect = findViewById(R.id.passage_type_selector);
        titleInput = findViewById(R.id.set_passage_title);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (TaskService.MyBinder) service;
                ContextHolder.setBinder(binder);
                submit.setClickable(true);
                selectImage.setClickable(true);
                submit.setOnClickListener(PublicPassageActivity.this);
                selectImage.setOnClickListener(PublicPassageActivity.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(new Intent(this, TaskService.class), connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    private SelectImageFromAlbumHelper helper = new SelectImageFromAlbumHelper(this);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        helper.handleResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_passage:
                ContextHolder.getBinder().PutTask(new newPassageTask());
                break;
            case R.id.select_list_image:
                ContextHolder.selectAlbumImage(this);
                break;
        }
    }

    private String listImagePath;

    @Override
    public void setImage(String path) {
        listImagePath = path;
    }

    @Override
    public void setImage(Bitmap image) {
        listImagePreShow.setImageBitmap(image);
    }

    private class newPassageTask extends NoReplySessionHostPostTask {
        private ComplexString string;
        private int type;
        private String title;

        public newPassageTask() {
            super("publicPassage");
            string = editor.getGoal();
            type = typeSelect.getSelectedItemPosition();
            title = titleInput.getText().toString();
        }

        @Override
        protected int makeForm(Form form, Session session) {
            form.addFields(new ComplexStringField(string))
                    .addFields(new IntField("type", type))
                    .addFields(new TextField("title", title))
                    .addFields(new ImageField("listImage", listImagePath));
            return 0;
        }

        @Override
        protected void onSuccess() {
            super.onSuccess();
            Toast.makeText(PublicPassageActivity.this, "发布成功1秒后跳转到主页面", Toast.LENGTH_LONG).show();
            ContextHolder.getBinder().PutTask(new DelayJumpTask(1000 , null) {
                @Override
                protected void doOnMain() {
                    finish();
                }
            });
        }

        @Override
        protected void onError(int errCode) {
            super.onError(errCode);
            Toast.makeText(PublicPassageActivity.this, "发布失败错误代码：" + errCode, Toast.LENGTH_SHORT).show();
        }
    }

}
