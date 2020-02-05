package cn.edu.hebut.easydesign.Activity.PassageList;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.R;

class FootHolder extends RecyclerView.ViewHolder {
    private TextView tip;
    FootHolder(LinearLayout footer, final PassageListView view) {
        super(footer);
        tip = footer.findViewById(R.id.foot);
        Log.i("foot", footer +"");
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ContextHolder.getContext(), "加载", Toast.LENGTH_SHORT).show();
                view.data.LoadMore(view.adapter);
            }
        });
    }

    void setLoad() {
        tip.setText(R.string.load);
    }
    void setLoading() {
        tip.setText(R.string.loading_list);
    }
    void setFinishLoad() {
        tip.setText(R.string.finish_list);
    }
    void setTip(String text) {
        tip.setText(text);
    }
}
