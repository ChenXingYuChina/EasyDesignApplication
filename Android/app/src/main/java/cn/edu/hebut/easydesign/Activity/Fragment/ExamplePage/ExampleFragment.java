package cn.edu.hebut.easydesign.Activity.Fragment.ExamplePage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.PassageList.Config.LastByType;
import cn.edu.hebut.easydesign.Activity.PassageList.Page.Page;
import cn.edu.hebut.easydesign.Activity.PassageList.Page.PassageListPage;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListViewPerformance;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageMultiListView;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class ExampleFragment extends Fragment {
    private TaskService.MyBinder binder;
    private PassageMultiListView content;
    private static JSONObject cacheConfig = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.example_fragment, container, false);
        content = view.findViewById(R.id.example_content);
        load: {
            if (cacheConfig != null) {
                try {
                    setup(cacheConfig);
                    break load;
                } catch (Exception e) {
                    Log.e("EF", e.toString());
                }
            }
            binder.PutTask(new LoadExampleTypeListTask("exampleList") {
                @Override
                protected void handleResult(JSONObject result) {
                    try {
                        cacheConfig = result;
                        setup(result);
                        SharedPreferences.Editor cache = ContextHolder.getContext().getSharedPreferences("exampleConfig", Context.MODE_PRIVATE).edit();
                        cache.putLong("exampleConfigLastTime", (new Date()).getTime());
                        cache.putString("exampleConfig", result.toString());
                        cache.apply();
                        // todo save the config
                    } catch (Exception e) {
                        Log.e("EF", e.toString());
                        Toast.makeText(ContextHolder.getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                protected void handleError(int errorCode) {
                    Log.i("EF", errorCode + "");
                    Toast.makeText(ContextHolder.getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    }

    private void setup(JSONObject object) throws Exception {
        JSONArray names = object.getJSONArray("names");
        JSONArray configs = object.getJSONArray("config");
        List<Page> pages = new ArrayList<>(configs.length());
        for (int i = 0; i < configs.length(); i ++) {
            JSONObject config = configs.getJSONObject(i);
            pages.add(new PassageListPage(new PassageListViewPerformance(config), new LastByType((short) config.getInt("type")), null));
        }
        content.init(pages, new ExampleFixedPart(ContextHolder.getContext(), names));
    }

    public static ExampleFragment getInstance() {
        ExampleFragment goal = new ExampleFragment();
        goal.binder = ContextHolder.getBinder();
        if (cacheConfig == null) {
            SharedPreferences cache = ContextHolder.getContext().getSharedPreferences("exampleConfig", Context.MODE_PRIVATE);
            long time = cache.getLong("exampleConfigLastTime", -1);
            if (((new Date()).getTime() - time) % (1000 * 60*60*24) < 1) {
                String value = cache.getString("exampleConfig", null);
                if (value != null) {
                    try {
                        cacheConfig = new JSONObject(value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return goal;
    }
}
