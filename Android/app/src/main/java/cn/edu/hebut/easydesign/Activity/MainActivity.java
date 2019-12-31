package cn.edu.hebut.easydesign.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import cn.edu.hebut.easydesign.Adapter.SplashAdapter;
import cn.edu.hebut.easydesign.Fragment.biFragment;
import cn.edu.hebut.easydesign.Fragment.huojianFragment;
import cn.edu.hebut.easydesign.Fragment.shubiaoFragment;
import cn.edu.hebut.easydesign.Fragment.weixinFragment;
import cn.edu.hebut.easydesign.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<View> mViews = new ArrayList<>();
    private ScrollView scroll;
    private LinearLayout tubiao;
    private RadioButton home;
    private ImageView shubiao;
    private ImageView weixin;
    private ImageView huojian;
    private ImageView bi;
    private Fragment fragment;
    private ViewPager mViewPager;
    private LinearLayout mPoints;
    private ImageView[] mIvPoints;
    private static final int[] PICS = {R.drawable.zhujiemian1, R.drawable.zhujiemian2, R.drawable.zhujiemian3};
    private SearchView mSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        //设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        tubiao = (LinearLayout) findViewById(R.id.tubiao);
        home = (RadioButton) findViewById(R.id.zhuye);
        shubiao = (ImageView) findViewById(R.id.shubiao);
        weixin = findViewById(R.id.weixin);
        huojian = findViewById(R.id.huojian);
        bi = findViewById(R.id.bi);
        mViewPager =  findViewById(R.id.viewpager);
        mPoints =  findViewById(R.id.points);
        mSearchView = findViewById(R.id.search_bar);
        scroll = findViewById(R.id.scroll);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        shubiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll.setVisibility(View.GONE);
                changeFragment(new shubiaoFragment());
            }
        });

        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll.setVisibility(View.GONE);
                changeFragment(new weixinFragment());
            }
        });

        huojian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll.setVisibility(View.GONE);
                changeFragment(new huojianFragment());
            }
        });

        bi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll.setVisibility(View.GONE);
                changeFragment(new biFragment());
            }
        });

    }
    private void initData() {
        //添加背景图片
        addBackgroundImg();
        mViewPager.setAdapter(new SplashAdapter(mViews, this));
        mViewPager.addOnPageChangeListener(new MainActivity.PageChangeListener());
        //设置点数据
        setCurDot();
    }

    private void addBackgroundImg() {
        for (int i = 0; i < PICS.length; i++) {
            ImageView img = new ImageView(this);
            img.setBackgroundResource(PICS[i]);
            int width = img.getWidth();
            int height = width*(9/16);
            img.setLayoutParams(new ViewGroup.LayoutParams(width,height));
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
                ImageView imageView = new ImageView(this);
                //图片设置属性 宽高
                imageView.setLayoutParams(new ViewGroup.LayoutParams(5, 5));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(10,10));
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
    private void changeFragment(Fragment fragment)
    {

        FragmentManager fragmentManager = getSupportFragmentManager();//开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment , fragment);
        transaction.commit();
    }



    }



