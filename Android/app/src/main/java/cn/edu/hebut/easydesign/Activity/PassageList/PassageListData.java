package cn.edu.hebut.easydesign.Activity.PassageList;

import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Resources.PassageList.LoadPassageListTask;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageList;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class PassageListData {
    PassageList passageList;
    List<UserMini> userMinis;
    private PassageListViewConfig config;
    private long lastTime = 0;
    private boolean refresh = true;
    private boolean load = true;
    /*package*/ PassageListData(PassageListViewConfig config) {
        passageList = new PassageList();
        userMinis = new LinkedList<>();
        this.config = config;
    }

    /*package*/ void loadMore(final PassageListView listView) {
        if (load) {
            load = false;
            listView.adapter.footHolder.setTip(listView.texts[PassageListView.text_foot_onLoading]);
            TaskService.MyBinder binder = ContextHolder.getBinder();
            binder.PutTask(new LoadPassageListTask(config.getFields(passageList.list.size())) {
                @Override
                protected void onSuccess(PassageList passageList, List<UserMini> userMinis) {
                    PassageListData.this.append(passageList, userMinis);
                    listView.adapter.notifyDataSetChanged();
                    load = true;
                    listView.adapter.footHolder.setTip(listView.texts[PassageListView.text_foot_toLoad]);
                }

                @Override
                protected void onErrorCode(int errCode) {
                    if (errCode == 702) {
                        listView.adapter.footHolder.setTip(listView.texts[PassageListView.text_foot_onNoNew]);
                    } else {
                        listView.adapter.footHolder.setTip(listView.texts[PassageListView.text_foot_toLoad]);
                        Toast.makeText(ContextHolder.getContext(), listView.texts[PassageListView.text_foot_onError], Toast.LENGTH_SHORT).show();
                    }
                    listView.adapter.notifyItemChanged(passageList.list.size() + 1);
                    load = true;
                }
            });
        }
    }

    private void append(PassageList passageList, List<UserMini> userMinis) {
        this.passageList.append(passageList);
        this.userMinis.addAll(userMinis);
    }

    /*package*/ void refresh(final PassageListView view) {
        if (refresh) {
            refresh = false;
            TaskService.MyBinder binder = ContextHolder.getBinder();
            Log.i("DATA", lastTime + "");
            binder.PutTask(new LoadPassageListTask(config.getRefreshFields(lastTime)) {
                @Override
                protected void onSuccess(PassageList passageList, List<UserMini> userMinis) {
                    clear();
                    PassageListData.this.append(passageList, userMinis);
                    lastTime = (new Date()).getTime();
                    view.adapter.notifyDataSetChanged();
                    view.swipe.setRefreshing(false);
                    refresh = true;
                }

                @Override
                protected void onErrorCode(int errCode) {
                    if (errCode == 702) {
                        Toast.makeText(ContextHolder.getContext(), view.texts[PassageListView.text_refresh_onNoNew], Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ContextHolder.getContext(), view.texts[PassageListView.text_refresh_onError], Toast.LENGTH_SHORT).show();
                    }
                    refresh = true;
                    view.swipe.setRefreshing(false);
                }
            });
        }
    }

    int size() {
        return passageList.list.size();
    }
    public void clear() {
        passageList.list.clear();
        userMinis.clear();
    }
}
