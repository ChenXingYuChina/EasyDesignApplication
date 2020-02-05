package cn.edu.hebut.easydesign.Activity.commonComponents;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebut.easydesign.Activity.SearchResultActivity;
import cn.edu.hebut.easydesign.R;

public class SearchBar extends FrameLayout {
    private SearchView searchView;
    private TextView textView;
    private Context context;
    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflate(context, R.layout.search_bar_frame, this);
        searchView = findViewById(R.id.search_bar);
        textView = findViewById(R.id.searchHelp);
        searchView.setOnSearchClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(View.GONE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                textView.setVisibility(View.VISIBLE);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent search = new Intent(SearchBar.this.context, SearchResultActivity.class);
                search.putExtra("keyword", query);
                SearchBar.this.context.startActivity(search);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
                textView.setVisibility(View.GONE);
            }
        });
    }
    public void setHint(String hint) {
        textView.setText(hint);
        searchView.setQueryHint(hint);
    }
}
