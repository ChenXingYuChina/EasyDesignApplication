package cn.edu.hebut.easydesign.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ContextHelp.HoldContextActivity;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageList;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageListItem;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;

public class PassageActivity extends HoldContextActivity {
    private PassageListItem item;
    private UserMini userMini;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            item = (PassageListItem) bundle.get("item");
            userMini = (UserMini) bundle.get("userMini");
            // todo load the passage and show it.
        } else {
            finish();
        }

    }
}
