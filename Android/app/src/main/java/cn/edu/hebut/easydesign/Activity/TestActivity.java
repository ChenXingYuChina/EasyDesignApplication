package cn.edu.hebut.easydesign.Activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextAppCompatActivity;
import cn.edu.hebut.easydesign.Activity.Fragment.UserPage.UserDescriptionPage;
import cn.edu.hebut.easydesign.Activity.Fragment.UserPage.UserFragment;
import cn.edu.hebut.easydesign.Activity.commonComponents.AlbumImage.SelectImageFromAlbumHelper;
import cn.edu.hebut.easydesign.ComplexString.ComplexStringLoader;
import cn.edu.hebut.easydesign.ComplexString.RichTextEditor.RichTextEditorHelper;
import cn.edu.hebut.easydesign.R;
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
    }

    SelectImageFromAlbumHelper helper = new SelectImageFromAlbumHelper(this);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        helper.handleResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        ContextHolder.setBinder(null);
    }

}
