package cn.edu.hebut.easydesign.Activity.Fragment.UserPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.commonComponents.ViewHelper.UserMiniHelper;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Resources.UserMini.UserMini;
import cn.edu.hebut.easydesign.TaskWorker.Condition;

public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.myHolder> {
    List<UserMini> userMinis;
    FollowListAdapter(List<UserMini> userMinis) {
        this.userMinis = userMinis;
    }
    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        holder.setData(userMinis.get(position));
    }

    @Override
    public int getItemCount() {
        return userMinis.size();
    }
    public static class myHolder extends RecyclerView.ViewHolder {
        UserMiniHelper helper;
        myHolder(@NonNull View itemView) {
            super(itemView);
        }
        void setData(UserMini userMini) {
            if (helper == null) {
                helper = new UserMiniHelper((ViewGroup) itemView, userMini, null, new Condition<>(false));
            } else {
                helper.reset();
                helper.setData(userMini, null, new Condition<>(false));
            }
        }
    }

    void changeData(List<UserMini> userMinis) {
        this.userMinis = userMinis;
        notifyDataSetChanged();
    }

}
