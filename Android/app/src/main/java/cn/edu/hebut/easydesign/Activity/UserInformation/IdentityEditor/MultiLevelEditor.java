package cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Session.User.Identity;

public abstract class MultiLevelEditor<T extends Identity, Editor extends SubEditor> extends IdentityEditor<T> {
    protected ViewGroup viewGroup;
    protected View add;
    public MultiLevelEditor(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public MultiLevelEditor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(final Context context) {
        inflate(context, R.layout.user_mulit_level_editor_layout, this);
        viewGroup = findViewById(R.id.sub_editor_list);
        add = findViewById(R.id.new_sub_editor);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addVoidSubEditor(context);
            }
        });
    }

    protected Editor addVoidSubEditor(Context context) {
        final Editor editor = createEditor(context);
        editor.getDelete().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewGroup.removeView(editor);
            }
        });
        viewGroup.addView(editor);
        return editor;
    }

    protected abstract Editor createEditor(Context context);
}
