package cn.edu.hebut.easydesign.Resources.PassageList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.PassageActivity;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.TaskWorker.Condition;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public abstract class PassageItemCard extends FrameLayout implements FrameLayout.OnClickListener {
    protected PassageListItem item = null;
    protected UserMini userMini = null;
    protected Condition<Boolean> cancel = new Condition<>();
    public PassageItemCard(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent jump = new Intent(ContextHolder.getContext(), PassageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);

        bundle.putSerializable("userMini", userMini);
        jump.putExtras(bundle);
        getContext().startActivity(jump);
//        getContext().startActivity(jump);
    }
    public void putItem(PassageListItem item, UserMini userMini) {
        cancel.condition = false;
        setItem(item, userMini);
    }

    public void cancelLoad() {
        cancel.condition = true;
    }

    protected abstract void setItem(PassageListItem item, UserMini userMini);
    protected abstract void reset();
}
