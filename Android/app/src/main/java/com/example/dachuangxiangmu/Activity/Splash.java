package com.example.dachuangxiangmu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.dachuangxiangmu.Adapter.SplashAdapter;
import com.example.dachuangxiangmu.R;

import java.util.ArrayList;
import java.util.List;

public class Splash extends AppCompatActivity {
    private List<View> mViews = new ArrayList<>();
    private ViewPager mViewPager;
    private LinearLayout mPoints;
    private Button btn;
    private ImageView next;
    private TextView zhuce;





    private ImageView[] mIvPoints;
    private static final int[] PICS = {R.drawable.yindao1, R.drawable.yindao2, R.drawable.yindao3, R.drawable.yindao4};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initData();

    }


    private void initView() {
        //设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mViewPager =  findViewById(R.id.viewpager);
        mPoints =  findViewById(R.id.points);
        mPoints.setVisibility(View.GONE);
        zhuce = findViewById(R.id.zhuce);
        zhuce.setVisibility(View.GONE);
        next = findViewById(R.id.next);
        next.setVisibility(View.GONE);
        btn =  findViewById(R.id.btn);
        btn.setVisibility(View.GONE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Splash.this,login1.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        //添加背景图片
        addBackgroundImg();
        mViewPager.setAdapter(new SplashAdapter(mViews, this));
        mViewPager.addOnPageChangeListener(new PageChangeListener());
        //设置点数据
//        setCurDot();
    }


    private void addBackgroundImg() {
        for (int i = 0; i < PICS.length; i++) {
            ImageView img = new ImageView(this);
            img.setBackgroundResource(PICS[i]);
            //添加页面数据
            mViews.add(img);
        }
    }

    private void setCurDot() {
//页面大于1,才显示点
        if (PICS.length > 1) {
            mIvPoints = new ImageView[PICS.length-1];
            for (int i = 0; i < mIvPoints.length; i++) {
                ImageView imageView = new ImageView(this);
                //图片设置属性 宽高
                imageView.setLayoutParams(new ViewGroup.LayoutParams(5, 5));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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

    private class PageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //最后一个页面显示按钮
            if (position == 0) {
                btn.setVisibility(View.GONE);
                mPoints.setVisibility(View.INVISIBLE);
                next.setVisibility(View.GONE);
                zhuce.setVisibility(View.GONE);
            }
            else if (position == 1){

                int visibility = mPoints.getVisibility();
                if(visibility == View.GONE){
                    setCurDot();
                }
                btn.setVisibility(View.GONE);
                mPoints.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                zhuce.setVisibility(View.GONE);
            }
            else if (position == 2){
                btn.setVisibility(View.GONE);
                mPoints.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                zhuce.setVisibility(View.GONE);
            }
            else if (position == 3){
                btn.setVisibility(View.VISIBLE);
                mPoints.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                zhuce.setVisibility(View.VISIBLE);
            }
            // 设置底部小点选中状态
            setPointBg(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setPointBg(int position) {
        for (int i = 0; i < mIvPoints.length; i++) {

            if (i == position-1) {
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

