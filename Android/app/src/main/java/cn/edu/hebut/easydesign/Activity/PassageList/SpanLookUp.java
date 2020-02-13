package cn.edu.hebut.easydesign.Activity.PassageList;

import androidx.recyclerview.widget.GridLayoutManager;

public class SpanLookUp extends GridLayoutManager.SpanSizeLookup {
    private PassageListAdapter adapter;
    SpanLookUp(PassageListAdapter adapter) {
        this.adapter = adapter;
    }
    @Override
    public int getSpanSize(int position) {
        if (position == adapter.size() + 1 || position == 0) {
            return 2;
        }
        return 1;
    }
}
