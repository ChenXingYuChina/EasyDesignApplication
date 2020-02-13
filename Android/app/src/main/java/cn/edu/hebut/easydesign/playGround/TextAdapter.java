package cn.edu.hebut.easydesign.playGround;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebut.easydesign.R;

public class TextAdapter extends RecyclerView.Adapter<TextViewHolder> {
    Context context;
    TextAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("pg", "create");
        return new TextViewHolder(LayoutInflater.from(context).inflate(R.layout.recycle_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        holder.setText(position + "");
    }

    @Override
    public int getItemCount() {
        return 10000;
    }

    class viewHolder extends RecyclerView.ViewHolder {

        public viewHolder(Context context) {
            super(new TextView(context));
        }
    }
}
