package cn.edu.hebut.easydesign.Resources.PassageList;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PassageList {
    public ArrayList<PassageListItem> list = new ArrayList<>();
    public PassageList(InputStream inputStream) throws Exception {
        JSONArray array = new JSONArray(new BufferedReader(new InputStreamReader(inputStream)).readLine());
        int length = array.length();
        for (int i = 0; i < length; i++) {
            try {
                list.add(new PassageListItem(array.getJSONObject(i)));
            } catch (Exception e) {
                Log.i("Pass", "PassageList: pass passage at order: "+ i + ", total: " + length);
            }
        }
    }
}
