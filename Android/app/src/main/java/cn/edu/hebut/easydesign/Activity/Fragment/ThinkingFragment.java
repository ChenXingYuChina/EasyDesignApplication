package cn.edu.hebut.easydesign.Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.edu.hebut.easydesign.Activity.PassageList.Config.LastByType;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListView;
import cn.edu.hebut.easydesign.R;

public class ThinkingFragment extends Fragment {
    public PassageListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout thinking = (LinearLayout) inflater.inflate(R.layout.thinking_fragment, container, false);
        listView = thinking.findViewById(R.id.last_content);
        listView.init(new LastByType((short) 0));
        return thinking;
    }

    public static ThinkingFragment getInstance() {
        return new ThinkingFragment();
    }
}
