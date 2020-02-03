package cn.edu.hebut.easydesign.Resources.PassageList;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cn.edu.hebut.easydesign.DataManagement.DataManagement;
import cn.edu.hebut.easydesign.DataManagement.DataType;
import cn.edu.hebut.easydesign.HttpClient.Form.Form;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.TaskWorker.BaseTasks.HostPostTask;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class LoadPassageListTask extends HostPostTask {
    private PassageList passageList;
    private List<UserMini> userMiniList;
    private FormField[] fields;
    public LoadPassageListTask(FormField[] fields) {
        super("passageList");
        this.fields = fields;
    }

    @Override
    protected int makeForm(Form form) {
        for (FormField f : fields) {
            form.AddFields(f);
        }
        return 0;
    }

    @Override
    protected int onPostFinish(Response response) {
        try {
            Log.i("LPL", "code" + response.isSuccessful());
            ResponseBody responseBody = response.body();
            if (responseBody==null) {
                return 704;
            }
            Log.i("LPL", "rb" + responseBody);
            String body = responseBody.string();
            Log.i("ED", "body" + response.code() + body);
            JSONObject object = new JSONObject(body);
            JSONArray passageArray = object.getJSONArray("passage_list");
            if (passageArray.length() == 0) return 702;
            passageList = new PassageList(passageArray);
            userMiniList = new LinkedList<>();
            int i = 0;
            if (!object.isNull("user_mini_list")) {
                JSONArray userMinis = object.getJSONArray("user_mini_list");
                for (; i < userMinis.length(); i++) {
                    userMiniList.add(UserMini.parseJson(userMinis.getJSONObject(i)));
                }
            }
            Log.i("LPL", "before load user");
            for (; i < passageList.list.size(); i++) {
                userMiniList.add((UserMini) DataManagement.getInstance().LoadData(DataType.UserMini, passageList.list.get(i).owner));
            }
            Log.i("LPL", "after load user");
        } catch (JSONException e) {
            Log.i("ED", e.toString());
            return 701;
        } catch (IOException e) {
            return 703;
        }
        return 0;
    }

    @Override
    protected void doOnMain() {
        Log.i("ED", "on main" + condition.condition);
        if (condition.condition != 0) {
            onErrorCode(condition.condition);
            return;
        }
        onSuccess(passageList, userMiniList);
    }

    protected abstract void onSuccess(PassageList passageList, List<UserMini> userMinis);
    protected abstract void onErrorCode(int errCode);

    public enum field {
        Type("type"), Begin("begin"), Length("len"), UserId("id"), Hot("hot"), Keyword("keyword"), LastRefreshTime("refresh"),
        Workshop("workshop");
        field(String name){
            fieldName = name;
        }
        public String fieldName;
        public TextField setData(String data) {
            return new TextField(fieldName, data);
        }
    }
}
