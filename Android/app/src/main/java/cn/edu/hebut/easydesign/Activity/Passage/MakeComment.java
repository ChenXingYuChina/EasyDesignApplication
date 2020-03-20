package cn.edu.hebut.easydesign.Activity.Passage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.commonComponents.TextButtonRichTextEditor;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageListItem;

public class MakeComment extends FrameLayout implements View.OnClickListener {
    private TextButtonRichTextEditor editor;
    private View submit;
    private PassageListItem item;

    public MakeComment(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.make_comment, this);
        editor = findViewById(R.id.comment_editor);
        submit = findViewById(R.id.submit_comment);
        submit.setOnClickListener(this);
    }

    public void setData(PassageListItem item) {
        this.item = item;
    }

    @Override
    public void onClick(View v) {
        if (item != null) {
            Log.i("makeComment", "onClick: ");
            submit.setClickable(false);
            ContextHolder.getBinder().PutTask(item.new commentTo(editor.getGoal()) {
                @Override
                protected void onSuccess() {
                    super.onSuccess();
                    editor.clear();
                    submit.setClickable(true);
                    Toast.makeText(ContextHolder.getContext(), "评论成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                protected void onError(int errCode) {
                    Toast.makeText(getContext(), "评论失败错误代码："+errCode, Toast.LENGTH_SHORT).show();
                    submit.setClickable(true);
                }
            });
        }
    }
}
