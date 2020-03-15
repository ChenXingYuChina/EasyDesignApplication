package cn.edu.hebut.easydesign.Activity.Fragment.UserPage.ChangeInformation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.LoginPage;
import cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor.IdentityEditor;
import cn.edu.hebut.easydesign.Activity.UserInformation.IdentityEditor.IdentityEditorFactory;
import cn.edu.hebut.easydesign.Activity.UserInformation.InformationEditor.ImageSelect;
import cn.edu.hebut.easydesign.Activity.UserInformation.InformationEditor.InformationEditor;
import cn.edu.hebut.easydesign.Activity.UserInformation.InformationEditor.SpecialErrorHandler;
import cn.edu.hebut.easydesign.Activity.commonComponents.FinishActivity;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.FormFieldView;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class EditSelections extends FrameLayout implements View.OnClickListener {
    private View confirm, cancel, logout, window;
    private TextView messageLabel;
    private int[] labelsId = new int[]{
            R.id.edit_head_image,
            R.id.editor_back_image,
            R.id.edit_long_description,
            R.id.edit_identity,
            R.id.edit_name,
            R.id.edit_password
    };
    private editorBinder nowShow;
    private FinishActivity finishActivity;

    public EditSelections(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.edit_selection_list, this);
        ViewGroup editorGroup = findViewById(R.id.editor_detail);
        int editorCount = editorGroup.getChildCount();
        messageLabel = (TextView) editorGroup.getChildAt(0);
        for (int i = 0; i < editorCount - 1; i++) {
            View editor = editorGroup.getChildAt(i + 1);
            if (i == 3) {
                ViewGroup group = (ViewGroup) editor;
                editor = IdentityEditorFactory.getInstance(context);
                editor.setVisibility(GONE);
                group.addView(editor);
            }
            bindEditor(labelsId[i], editor);
        }
        window = findViewById(R.id.editor_detail_window);
        cancel = findViewById(R.id.cancel_edit);
        logout = findViewById(R.id.logout);
        confirm = findViewById(R.id.confirm);
        cancel.setOnClickListener(this);
        logout.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    private void bindEditor(@IdRes int id, View view) {
        if (view instanceof FormFieldView) {
            // 这个会在构造函数中被label持有所以就不用赋值给别人了。
            new editorBinder(id, (FormFieldView) view);
        }
    }

    @Override
    public void onClick (View v) {
        TaskService.MyBinder binder = ContextHolder.getBinder();
        if (v == cancel) {
            nowShow.goneView();
            nowShow = null;
        } else if (v == confirm) {
            if (nowShow.view instanceof InformationEditor) {
                try {
                    binder.PutTask(new NoReplySessionHostPostTaskFromInformationEditor((InformationEditor) nowShow.view) {
                        @Override
                        protected void onSuccess() {
                            nowShow.goneView();
                            nowShow = null;
                        }

                        @Override
                        protected void onError(int errCode) {
                            Log.i("onError", "err: " + errCode);
                            String message = null;
                            if (nowShow.view instanceof SpecialErrorHandler) {
                                message = ((SpecialErrorHandler) nowShow.view).HandleError(errCode);
                            }
                            if (message == null) {
                                message = "错误代码：" + errCode;
                            }
                            handleError(message);
                        }
                    });
                } catch (Exception e) {
                    String message = null;
                    if (nowShow.view instanceof SpecialErrorHandler) {
                        message = ((SpecialErrorHandler) nowShow.view).HandleError(e);
                    }
                    if (message == null) {
                        handleError(message);
                    }
                    handleError(e.getMessage());
                }
            }
        } else if (v == logout) {
            binder.PutTask(new LogoutTask());
            Context context = ContextHolder.getContext();
            ContextHolder.getContext().startActivity(new Intent(context, LoginPage.class));
            finishActivity.finishActivity();
        }
    }

    private void handleError(String message) {
        messageLabel.setText(message);
    }

    private class editorBinder implements OnClickListener {
        FormFieldView view;
        View label;

        editorBinder(@IdRes int labelId, FormFieldView view) {
            label = findViewById(labelId);
            this.view = view;
            label.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ((View) view).setVisibility(VISIBLE);
            window.setVisibility(VISIBLE);
            nowShow = this;
        }

        List<FormField> getData() throws Exception {
            List<FormField> goal = new LinkedList<>();
            view.collectData(goal);
            return goal;
        }

        void goneView() {
            ((View) view).setVisibility(GONE);
            window.setVisibility(GONE);
        }
    }

    public void setFinishActivity(FinishActivity finishActivity) {
        this.finishActivity = finishActivity;
    }
}
