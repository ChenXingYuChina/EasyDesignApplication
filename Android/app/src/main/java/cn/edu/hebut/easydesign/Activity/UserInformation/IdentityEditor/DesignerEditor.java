package cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.UserInformation.UserInformation;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.JsonField;
import cn.edu.hebut.easydesign.Session.User.Designer;
import cn.edu.hebut.easydesign.Session.User.Work;
import okhttp3.FormBody;
import okhttp3.MultipartBody;

public class DesignerEditor extends MultiLevelEditor<Designer, WorkEditor> {
    public DesignerEditor(@NonNull Context context) {
        super(context);
        if (add instanceof TextView) {
            ((TextView) add).setText("新建工作经历");
        }
    }

    public DesignerEditor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void collectData(List<FormField> goal) throws Exception {
        Set<Work> mid = new HashSet<>();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            mid.add(((WorkEditor) viewGroup.getChildAt(i)).getWork());
        }
        Designer designer = new Designer();
        designer.works = new ArrayList<>(mid);
        Collections.sort(designer.works);
        goal.add(new IdentityField(fieldName, designer));
    }

    @Override
    public void setDate(Designer data) {
        for (Work work : data.works) {
            WorkEditor editor = addVoidSubEditor(getContext());
            editor.setData(work);
        }
    }

    @Override
    protected WorkEditor createEditor(Context context) {
        return new WorkEditor(context);
    }
}
