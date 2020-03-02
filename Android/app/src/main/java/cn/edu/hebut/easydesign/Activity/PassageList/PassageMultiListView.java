package cn.edu.hebut.easydesign.Activity.PassageList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import cn.edu.hebut.easydesign.Activity.PassageList.Page.Page;
import cn.edu.hebut.easydesign.R;

public class PassageMultiListView extends PassageListContainer {
    private ViewPager pager;
    private FrameLayout fixed;
    private CategoryGroup group;
    private List<Page> pages;
    private FrameLayout head;
    private View top;
    public PassageMultiListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.passage_mulit_list_view, this);
        pager = findViewById(R.id.pager);
        swipe = findViewById(R.id.list_swipe);
        if (attrs == null) throw new IllegalArgumentException();
        int publicHead = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "top", R.layout.nil_head);
        head = findViewById(R.id.top_view);
        inflate(context, publicHead, head);
        top = head.getChildAt(0);
        Log.i("PMLV", "top: " + top);
        if (top instanceof OnHeadBind) {
            ((OnHeadBind) top).onHeadBind(this);
        }
        fixed = findViewById(R.id.fixed_view);
    }

    public void init(@NonNull final List<Page> pageList, @NonNull final FixedPart part) {
        pages = pageList;
        group = part.getGroup();
        for (Page page : this.pages) {
            page.bind(this);
        }
        pager.setAdapter(new PassageListPagerAdapter(pages));
        swipe.setOnRefreshListener(pages.get(0));
        fixed.addView(part);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Page page = pages.get(position);
                swipe.setEnabled(page.canRefresh());
                swipe.setOnRefreshListener(page);
                group.check(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        group.setOnSelectedChangeListener(new CategoryGroup.onSelectedChangeListener() {
            @Override
            public void onChange(int position) {
                Log.i("PMLV", "the new select: " + position);
                Page page = pages.get(position);
                pager.setCurrentItem(position);
                swipe.setEnabled(page.canRefresh());
                swipe.setOnRefreshListener(page);
            }
        });
        group.check(0);
        Log.i("PMLV", "" + fixed.getHeight() + pager.getHeight());
    }

    public View getHead() {
        return top;
    }

    public void closeRefresh() {
        swipe.setRefreshing(false);
    }

}
