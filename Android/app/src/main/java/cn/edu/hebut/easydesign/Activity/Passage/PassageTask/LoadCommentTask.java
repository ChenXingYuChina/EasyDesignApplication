package cn.edu.hebut.easydesign.Activity.Passage.PassageTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import cn.edu.hebut.easydesign.HttpClient.Form.BooleanField;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.LongField;
import cn.edu.hebut.easydesign.HttpClient.Form.ShortField;
import cn.edu.hebut.easydesign.Resources.Passage.Comment;
import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.StringHostPostTask;
import cn.edu.hebut.easydesign.TaskWorker.Condition;

public abstract class LoadCommentTask extends StringHostPostTask {
    private long id;
    private long begin;
    private boolean hot;
    private List<Comment> comments;
    private static final short length = 10;

    public LoadCommentTask(long id, long begin, Condition<Boolean> cancel) {
        super("comment", cancel);
        this.id = id;
        this.begin = begin;
        hot = false;
    }

    /*load hot comment*/
    public LoadCommentTask(long id, Condition<Boolean> cancel) {
        super("comment", cancel);
        this.id = id;
        hot = true;
    }

    @Override
    protected int handleResult(String result) {
        try {
            JSONArray goal = new JSONArray(result);
            comments = new LinkedList<>();
            for (int i = 0; i < goal.length(); i++) {
                JSONObject comment = goal.getJSONObject(i);
                comments.add(new Comment(comment.getJSONObject("com"), comment.getJSONArray("sub_com")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 702;
        }
        return 0;
    }

    @Override
    protected void doOnMainNormal() {
        if (condition.condition != 0) {
            onError(condition.condition);
        } else if (!cancel.condition) {
            onSuccess(comments);
        }
    }

    @Override
    protected int makeForm(Form form) {
        form.addFields(new BooleanField("hot", hot)).addFields(new LongField("id", id));
        if (!hot) {
            form.addFields(new LongField("begin", begin))
                    .addFields(new ShortField("len", length));
        }
        return 0;
    }

    protected abstract void onSuccess(List<Comment> comments);

    protected abstract void onError(int errCode);
}
