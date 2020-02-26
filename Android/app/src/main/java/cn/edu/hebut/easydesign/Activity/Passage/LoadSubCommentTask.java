package cn.edu.hebut.easydesign.Activity.Passage;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.IntField;
import cn.edu.hebut.easydesign.HttpClient.Form.LongField;
import cn.edu.hebut.easydesign.Resources.Passage.SubComment;
import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.StringHostPostTask;
import cn.edu.hebut.easydesign.TaskWorker.Condition;

public abstract class LoadSubCommentTask extends StringHostPostTask {
    private long pid;
    private int position;
    private List<SubComment> subComments;

    public LoadSubCommentTask(long passageId, int position, Condition<Boolean> cancel) {
        super("subComment", cancel);
        this.pid = passageId;
        this.position = position;
    }

    @Override
    protected int makeForm(Form form) {
        form.addFields(new LongField("pid", pid)).addFields(new IntField("father", position));
        return 0;
    }

    @Override
    protected void doOnMainNormal() {
        if (condition.condition != 0) {
            onError(condition.condition);
        } else {
            onSuccess(subComments);
        }
    }

    protected abstract void onError(int errCode);

    protected abstract void onSuccess(List<SubComment> subComments);

    @Override
    protected int handleResult(String result) {
        try {
            JSONArray subs = new JSONArray(result);
            subComments = new ArrayList<>(subs.length());
            for (int i = 0; i < subs.length(); i++) {
                subComments.add(new SubComment(subs.getJSONObject(i)));
            }
        } catch (Exception e) {
            return 702;
        }
        return 0;
    }
}
