package cn.edu.hebut.easydesign.Activity.PassageList;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.R;

class FootHolder extends RecyclerView.ViewHolder {
    private TextView tip;
    FootHolder(LinearLayout footer, final PassageListAdapter adapter) {
        super(footer);
        tip = footer.findViewById(R.id.foot);
        Log.i("foot", footer +"");
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.loadMore();
            }
        });
    }
    void setTip(String text) {
        tip.setText(text);
    }
}
