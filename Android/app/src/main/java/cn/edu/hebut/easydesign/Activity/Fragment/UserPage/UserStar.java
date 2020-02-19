package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListViewConfig;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.Resources.PassageList.LoadPassageListTask;

public class UserStar extends PassageListViewConfig {
    private long id;
    public UserStar(long id) {
        this.id = id;
        api = "starPassage";
        fields = new TextField[3];
        fields[0] = LoadPassageListTask.field.Length.setData(length + "");
        fields[1] = LoadPassageListTask.field.UserId.setData(id + "");
        fields[2] = LoadPassageListTask.field.Begin.setData("");
        refreshFields = new TextField[3];
        refreshFields[0] = fields[0];
        refreshFields[1] = fields[1];
        refreshFields[2] = LoadPassageListTask.field.Begin.setData("0");
    }
    @Override
    protected FormField[] getFields(int begin) {
        fields[2].setData(begin + "");
        return fields;
    }

    @Override
    protected FormField[] getRefreshFields(long lastTime) {
        return refreshFields;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
