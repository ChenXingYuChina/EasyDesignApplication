package cn.edu.hebut.easydesign.Activity.UserInformation.InformationEditor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.commonComponents.TextButtonRichTextEditor;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;

public class UserLongDescriptionEditor extends TextButtonRichTextEditor implements InformationEditor {
    public UserLongDescriptionEditor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public String getApiUrl() {
        return "changeLongDescription";
    }

    @Override
    public void collectData(List<FormField> goal) throws Exception {
        getGoal();
    }
}
