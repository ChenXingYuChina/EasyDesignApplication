package cn.edu.hebut.easydesign.Activity.PassageList;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import cn.edu.hebut.easydesign.Activity.PassageList.Page.Page;

public class PassageListPagerAdapter extends PagerAdapter {
    private List<Page> pages;

    PassageListPagerAdapter(List<Page> pages) {
        this.pages = pages;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Page page = pages.get(position);
        container.addView(page.getView());
        page.onRefresh();
        return page.getView();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(pages.get(position).getView());
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    public void refreshAt(int position) {
        pages.get(position).onRefresh();
    }

    public boolean canRefresh(int position) {
        return pages.get(position).canRefresh();
    }

}
