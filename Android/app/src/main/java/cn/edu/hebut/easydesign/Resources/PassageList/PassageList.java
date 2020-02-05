package cn.edu.hebut.easydesign.Resources.PassageList;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PassageList {
    public List<PassageListItem> list = new LinkedList<>();
    public PassageList() {

    }
    public PassageList(JSONArray array) {
        int length = array.length();
        for (int i = 0; i < length; i++) {
            try {
                list.add(new PassageListItem(array.getJSONObject(i)));
            } catch (Exception e) {
                Log.i("Pass", "PassageList: pass passage at order: "+ i + ", total: " + length);
            }
        }
    }
    public void append(PassageList list) {
        this.list.addAll(list.list);
    }
}
