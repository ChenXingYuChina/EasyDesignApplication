package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import cn.edu.hebut.easydesign.DataManager.DataManager;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.LongField;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMiniLoader;
import cn.edu.hebut.easydesign.Session.User.User;
import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.HostPostTask;
import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.StringHostPostTask;
import cn.edu.hebut.easydesign.TaskWorker.Condition;

public abstract class LoadFollowListTask extends StringHostPostTask {
    private List<UserMini> userMiniList = new LinkedList<>();
    private long who;
    public LoadFollowListTask(long who, Condition<Boolean> cancel) {
        super("loadFollow", cancel);
        this.who = who;
    }

    @Override
    protected int handleResult(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                if (object.isNull("user_mini")) {
                    UserMini userMini = UserMiniLoader.getInstance().load(object.getLong("id"));
                    if (userMini != null) {
                        userMiniList.add(userMini);
                    } else {
                        Log.i("follow", "handleResult: " + 702);
                        return 702;
                    }
                } else {
                    userMiniList.add(UserMini.parseJson(object.getJSONObject("user_mini")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return 702;
        }

        return 0;
    }

    @Override
    protected void doOnMainNormal() {
        handleResult(userMiniList);
    }

    @Override
    protected int makeForm(Form form) {
        form.addFields(new LongField("who", who));
        return 0;
    }

    protected abstract void handleResult(List<UserMini> userMinis);
}
