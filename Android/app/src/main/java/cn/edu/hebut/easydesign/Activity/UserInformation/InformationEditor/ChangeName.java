package cn.edu.hebut.easydesign.Activity.UserInformation.InformationEditor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.FormFieldView;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.R;

public class ChangeName extends FrameLayout implements InformationEditor {
    private EditText newName;
    public ChangeName(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.user_name_editor, this);
        newName = findViewById(R.id.user_name);
    }

    public ChangeName(@NonNull Context context) {
        super(context, null);
    }

    @Override
    public String getApiUrl() {
        return "changeUserName";
    }

    @Override
    public void collectData(List<FormField> goal) throws Exception {
        goal.add(new TextField("name", newName.getText().toString()));
    }
}
