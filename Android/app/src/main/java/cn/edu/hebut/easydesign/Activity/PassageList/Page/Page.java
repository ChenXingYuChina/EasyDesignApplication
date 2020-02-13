package cn.edu.hebut.easydesign.Activity.PassageList.Page;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.edu.hebut.easydesign.Activity.PassageList.PassageListContainer;

public interface Page extends SwipeRefreshLayout.OnRefreshListener {

    View getView();

    boolean canRefresh();

    void bind(@NonNull PassageListContainer father);

}
