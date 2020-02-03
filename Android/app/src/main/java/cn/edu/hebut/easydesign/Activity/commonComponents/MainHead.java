package cn.edu.hebut.easydesign.Activity.commonComponents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import cn.edu.hebut.easydesign.Activity.Adapter.SplashAdapter;
import cn.edu.hebut.easydesign.R;

public class MainHead extends FrameLayout {
    private List<View> mViews = new ArrayList<>();
    private ViewPager mViewPager;
    private LinearLayout mPoints;
    public ImageView[] mIvPoints;
    private static int[] PICS = {R.drawable.zhujiemian1, R.drawable.zhujiemian2, R.drawable.zhujiemian3};
    public SearchView mSearchView;
    private Context context;

    public MainHead(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.main_activity_head, this);
        this.context = context;
        initView();
        initData();
    }

    private void initView() {
        mViewPager = findViewById(R.id.viewpager);
        mPoints = findViewById(R.id.points);
        mSearchView = findViewById(R.id.search_bar);
    }

    private void initData() {
        //添加背景图片
        addBackgroundImg();
        mViewPager.setAdapter(new SplashAdapter(mViews, context));
        mViewPager.addOnPageChangeListener(new MainHead.PageChangeListener());
        //设置点数据
        setCurDot();
    }

    private void addBackgroundImg() {
        for (int i = 0; i < PICS.length; i++) {
            ImageView img = new ImageView(context);
            img.setBackgroundResource(PICS[i]);
            int width = img.getWidth();
            int height = width * 9 / 16;
            img.setLayoutParams(new ViewGroup.LayoutParams(width, height));
            //添加页面数据
            mViews.add(img);
        }
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setPointBg(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setCurDot() {
//页面大于1,才显示点
        if (PICS.length > 1) {
            mIvPoints = new ImageView[PICS.length];
            for (int i = 0; i < mIvPoints.length; i++) {
                ImageView imageView = new ImageView(context);
                //图片设置属性 宽高
                imageView.setLayoutParams(new ViewGroup.LayoutParams(5, 5));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(10, 10));
                //设置左右边距
                layoutParams.leftMargin = (int) getResources().getDimension(R.dimen.app_home_points_item_margin);
                layoutParams.rightMargin = (int) getResources().getDimension(R.dimen.app_home_points_item_margin);
                //默认
                // 第一个选中
                if (i == 0) {
                    imageView.setBackgroundResource(R.drawable.page_selected_indicator);
                } else {
                    imageView.setBackgroundResource(R.drawable.page_normal_indicator);
                }
                mIvPoints[i] = imageView;
                mPoints.addView(imageView, layoutParams);
            }
            mPoints.setVisibility(View.VISIBLE);
        } else {
            mPoints.setVisibility(View.VISIBLE);
        }
    }

    private void setPointBg(int position) {
        for (int i = 0; i < mIvPoints.length; i++) {

            if (i == position) {
                //两种方式设置
                mIvPoints[i].setBackgroundResource(R.drawable.page_selected_indicator);
//                mIvPoints[i].setBackground(getResources().getDrawable(R.drawable.shape_point_solid_selcted));
            } else {
                mIvPoints[i].setBackgroundResource(R.drawable.page_normal_indicator);
//                mIvPoints[i].setBackground(getResources().getDrawable(R.drawable.shape_point_solid_normal));
            }
        }
    }
}
