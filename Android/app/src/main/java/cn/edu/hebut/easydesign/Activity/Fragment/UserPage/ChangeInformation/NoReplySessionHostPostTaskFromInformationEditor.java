package cn.edu.hebut.easydesign.Activity.Fragment.UserPage.ChangeInformation;

import java.util.LinkedList;
import java.util.List;

import cn.edu.hebut.easydesign.Activity.UserInformation.InformationEditor.InformationEditor;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.FormFieldView;
import cn.edu.hebut.easydesign.Session.NeedSessionTask.NoReplySessionHostPostTask;
import cn.edu.hebut.easydesign.Session.Session;

public abstract class NoReplySessionHostPostTaskFromInformationEditor extends NoReplySessionHostPostTask {
    List<FormField> fieldList = new LinkedList<>();
    public NoReplySessionHostPostTaskFromInformationEditor(InformationEditor editor) throws Exception {
        super(editor.getApiUrl());
        editor.collectData(fieldList);
    }

    @Override
    protected int makeForm(Form form, Session session) {
        form.addFields(fieldList);
        return 0;
    }

    @Override
    protected abstract void onSuccess();
}
