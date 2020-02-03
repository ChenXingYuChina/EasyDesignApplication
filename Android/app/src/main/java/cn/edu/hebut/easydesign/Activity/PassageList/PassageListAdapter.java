package cn.edu.hebut.easydesign.Activity.PassageList;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.PassageList.Cards.CardFactory;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.PassageList.PassageList;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;

public class PassageListAdapter extends RecyclerView.Adapter {
    private @LayoutRes int cardLayout;
    private @LayoutRes int headLayout;
    private PassageList list;
    private List<UserMini> userMinis;
    private PassageListView father;

    PassageListAdapter(@LayoutRes int cardLayout, @LayoutRes int headLayout, PassageListView father, PassageList list, List<UserMini> userMinis) {
        this.cardLayout = cardLayout;
        this.list = list;
        this.userMinis = userMinis;
        this.father = father;
        this.headLayout = headLayout;
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof ItemHolder)
            ((ItemHolder)holder).card.cancelLoad();
    }

    FootHolder footHolder = null;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case normal:
                return new ItemHolder(CardFactory.makeCard(cardLayout, parent));
            case foot:
                Log.i("adapter", "foot");
                footHolder = new FootHolder((LinearLayout) LayoutInflater.from(ContextHolder.getContext()).inflate(R.layout.passage_list_foot, parent, false), father);
                return footHolder;
            case head:
                return new HeadHolder(headLayout, parent);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position <= list.list.size() && position != 0) {
            ((ItemHolder)holder).card.putItem(list.list.get(position-1), userMinis.get(position-1));
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

}
