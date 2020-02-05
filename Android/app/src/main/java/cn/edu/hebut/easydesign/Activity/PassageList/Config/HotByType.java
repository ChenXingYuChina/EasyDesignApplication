package cn.edu.hebut.easydesign.Activity.PassageList.Config;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListViewConfig;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.Resources.PassageList.LoadPassageListTask;

public class HotByType extends PassageListViewConfig {
    private short type;
    public HotByType(short type) {
        this.type = type;
        fields = new TextField[4];
        fields[0] = LoadPassageListTask.field.Type.setData(type + "");
        fields[1] = LoadPassageListTask.field.Length.setData(length + "");
        fields[2] = LoadPassageListTask.field.Hot.setData(true + "");
        fields[3] = LoadPassageListTask.field.Begin.setData("");
        refreshFields = new TextField[4];
        refreshFields[0] = fields[0];
        refreshFields[1] = fields[1];
        refreshFields[2] = fields[2];
        refreshFields[3] = LoadPassageListTask.field.LastRefreshTime.setData("");
    }
    @Override
    protected FormField[] getFields(int begin) {
        fields[3].setData(begin + "");
        return fields;
    }

    @Override
    protected FormField[] getRefreshFields(long lastTime) {
        refreshFields[3].setData(lastTime + "");
        return refreshFields;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof HotByType)) {
            return false;
        }
        HotByType hotByType = (HotByType) obj;
        return hotByType.type == this.type;
    }

    @Override
    public int hashCode() {
        return ((int)type << 8) + 0;
    }
}
