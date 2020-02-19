package cn.edu.hebut.easydesign.Activity.PassageList.Config;

import android.util.Log;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListViewConfig;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.Resources.PassageList.LoadPassageListTask;

public class UserLastByType extends PassageListViewConfig {
    private short type;
    private long user;
    public UserLastByType(short type, long user) {
        this.user = user;
        this.type = type;
        fields = new TextField[4];
        fields[0] = LoadPassageListTask.field.Type.setData(type + "");
        fields[1] = LoadPassageListTask.field.Length.setData(length + "");
        fields[2] = LoadPassageListTask.field.UserId.setData(user + "");
        fields[3] = LoadPassageListTask.field.Begin.setData("");
        refreshFields = new TextField[4];
        refreshFields[0] = fields[0];
        refreshFields[1] = fields[1];
        refreshFields[2] = fields[2];
        refreshFields[3] = LoadPassageListTask.field.Begin.setData("0");
    }
    @Override
    protected FormField[] getFields(int begin) {
        Log.i("ULBT", "" + begin);
        fields[3].setData(begin + "");
        return fields;
    }

    @Override
    protected FormField[] getRefreshFields(long lastTime) {
        return refreshFields;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof UserLastByType)) return false;
        return ((UserLastByType) obj).type == this.type && ((UserLastByType) obj).user == this.user;
    }

    @Override
    public int hashCode() {
        return (((int) type) ^ (int)user) << 8 + 3;
    }
}
