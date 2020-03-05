package cn.edu.hebut.easydesign.Activity.Fragment.HomePage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.edu.hebut.easydesign.Activity.ContextHelp.ContextHolder;
import cn.edu.hebut.easydesign.Activity.PassageList.Config.HotByType;
import cn.edu.hebut.easydesign.Activity.PassageList.Page.Page;
import cn.edu.hebut.easydesign.Activity.PassageList.Page.PassageListPage;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListViewPerformance;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageMultiListView;
import cn.edu.hebut.easydesign.R;

public class HomeFragment extends Fragment {
    private PassageMultiListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout home = (LinearLayout) inflater.inflate(R.layout.home_fragment, container, false);
        listView = home.findViewById(R.id.hot_list);
        List<Page> pages = new ArrayList<>(4);
        pages.add(new PassageListPage(new HotByType((short) 0)));
        pages.add(new PassageListPage(new PassageListViewPerformance(R.layout.title_main_card, R.layout.nil_head, PassageListViewPerformance.Linear), new HotByType((short) 1), null));
        pages.add(new PassageListPage(new HotByType((short) 2)));
        pages.add(new PassageListPage(new HotByType((short) 3)));
        listView.init(pages, new HomeFixedPart(ContextHolder.getContext()));
        return home;
    }

    /*
        use this after the server connected
     */
    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

}
