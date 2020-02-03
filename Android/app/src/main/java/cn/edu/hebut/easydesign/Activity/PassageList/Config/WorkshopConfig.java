package cn.edu.hebut.easydesign.Activity.PassageList.Config;

import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListViewConfig;
import cn.edu.hebut.easydesign.HttpClient.Form.FormField;
import cn.edu.hebut.easydesign.HttpClient.Form.TextField;
import cn.edu.hebut.easydesign.Resources.PassageList.LoadPassageListTask;

public class WorkshopConfig extends PassageListViewConfig {
    long workshop;
    public WorkshopConfig(long workshop) {
        this.workshop = workshop;
        fields = new TextField[3];
        fields[0] = LoadPassageListTask.field.Workshop.setData(workshop + "");
        fields[1] = LoadPassageListTask.field.Length.setData(length + "");
        fields[2] = LoadPassageListTask.field.Begin.setData("");
        refreshFields = fields;
    }
    @Override
    protected FormField[] getFields(int begin) {
        refreshFields[2].setData(begin + "");
        return fields;
    }

    @Override
    protected FormField[] getRefreshFields(long lastTime) {
        refreshFields[2].setData(0 + "");
        return refreshFields;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof WorkshopConfig) {
            return ((WorkshopConfig) obj).workshop == this.workshop;
        }
        return false;
    }
}
