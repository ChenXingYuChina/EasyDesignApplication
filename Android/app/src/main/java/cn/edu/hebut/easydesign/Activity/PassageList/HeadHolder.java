package cn.edu.hebut.easydesign.Activity.PassageList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;

public class HeadHolder extends RecyclerView.ViewHolder {
    public HeadHolder(@LayoutRes int head, ViewGroup parent) {
        super(LayoutInflater.from(ContextHolder.getContext()).inflate(head, parent, false));
    }
}
