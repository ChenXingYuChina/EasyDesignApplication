package cn.edu.hebut.easydesign.Activity.PassageList;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.PassageList.Cards.CardFactory;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.PassageList.LoadPassageListTask;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageList;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.TaskWorker.TaskService;

public class PassageListAdapter extends RecyclerView.Adapter implements SwipeRefreshLayout.OnRefreshListener {
    private @LayoutRes
    int cardLayout;
    private @LayoutRes
    int headLayout;
    private PassageListViewConfig config;
    private PassageList list;
    private List<UserMini> userMinis;
    private PassageListContainer father;
    private TipResources tips;
    private long lastTime = -1;

    PassageListAdapter(PassageListViewPerformance performance, PassageListContainer father, PassageListViewConfig config, TipResources tipResources) {
        this.cardLayout = performance.card;
        this.list = new PassageList();
        this.userMinis = new LinkedList<>();
        this.father = father;
        this.headLayout = performance.head;
        this.config = config;
        tips = tipResources;
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof ItemHolder) {
            ((ItemHolder) holder).card.cancelLoad();
        }
    }

    FootHolder footHolder = null;
    HeadHolder headHolder = null;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case normal:
                return new ItemHolder(CardFactory.makeCard(cardLayout, parent));
            case foot:
                Log.i("adapter", "foot");
                footHolder = new FootHolder((LinearLayout) LayoutInflater.from(ContextHolder.getContext()).inflate(R.layout.passage_list_foot, parent, false), this);
                return footHolder;
            case head:
                headHolder = new HeadHolder(headLayout, parent);
                return headHolder;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position <= list.list.size() && position != 0) {
            ((ItemHolder) holder).card.putItem(list.list.get(position - 1), userMinis.get(position - 1));
        } else if (holder instanceof HeadHolder) {
            if (holder.itemView instanceof OnHeadBind) {
                ((OnHeadBind) holder.itemView).onHeadBind(father);
            }
        }
    }

    private final static int normal = 0;
    private final static int foot = 1;
    private final static int head = 2;

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return head;
        } else if (position == list.list.size() + 1) {
            return foot;
        }
        return normal;
    }

    @Override
    public int getItemCount() {
        return list.list.size() + 2;
    }

    void loadMore() {
        footHolder.setTip(tips.texts[TipResources.text_foot_onLoading]);
        TaskService.MyBinder binder = ContextHolder.getBinder();
        binder.PutTask(new LoadPassageListTask(config.api, config.getFields(list.list.size())) {
            @Override
            protected void onSuccess(PassageList passageList, List<UserMini> userMinis) {
                append(passageList, userMinis);
                int length = userMinis.size();
                notifyItemRangeInserted(PassageListAdapter.this.userMinis.size() - length + 1, length);
                footHolder.setTip(tips.texts[TipResources.text_foot_toLoad]);
            }

            @Override
            protected void onErrorCode(int errCode) {
                if (errCode == 702) {
                    footHolder.setTip(tips.texts[TipResources.text_foot_onNoNew]);
                } else {
                    footHolder.setTip(tips.texts[TipResources.text_foot_toLoad]);
                    Toast.makeText(ContextHolder.getContext(), tips.texts[TipResources.text_foot_onError], Toast.LENGTH_SHORT).show();
                }
                notifyItemChanged(list.list.size() + 1);
            }
        });

    }

    void refresh() {
        TaskService.MyBinder binder = ContextHolder.getBinder();
        binder.PutTask(new LoadPassageListTask(config.api, config.getRefreshFields(lastTime)) {
            @Override
            protected void onSuccess(PassageList passageList, List<UserMini> userMinis) {
                clear();
                append(passageList, userMinis);
                lastTime = (new Date()).getTime();
                notifyDataSetChanged();
                father.swipe.setRefreshing(false);
            }

            @Override
            protected void onErrorCode(int errCode) {
                if (errCode == 702) {
                    Toast.makeText(ContextHolder.getContext(), tips.texts[TipResources.text_refresh_onNoNew], Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ContextHolder.getContext(), tips.texts[TipResources.text_refresh_onError], Toast.LENGTH_SHORT).show();
                }
                father.swipe.setRefreshing(false);
            }
        });

    }

    private void append(PassageList passageList, List<UserMini> userMinis) {
        list.append(passageList);
        this.userMinis.addAll(userMinis);
    }

    private void clear() {
        list.list.clear();
        userMinis.clear();
    }
    int size() {
        return list.list.size();
    }

    @Override
    public void onRefresh() {
        refresh();
    }
}
