package cn.edu.hebut.easydesign.Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListView;
import cn.edu.hebut.easydesign.Activity.commonComponents.MainHead;
import cn.edu.hebut.easydesign.R;

public class HomeFragment extends Fragment {
    public PassageListView listView;
    MainHead head;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listView = (PassageListView) inflater.inflate(R.layout.home_fragment, container, false);
        return listView;

    }
}
