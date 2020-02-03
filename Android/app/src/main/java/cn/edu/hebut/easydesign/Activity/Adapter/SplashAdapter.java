package cn.edu.hebut.easydesign.Activity.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class SplashAdapter extends PagerAdapter {

    private final List<View> mData;
    private final Context mContext;

    public SplashAdapter(List<View> views, Context context) {
        mData = views;
        mContext = context;
    }
    /**
     * 初始化item
     * @param container 容器
     * @param position  位置
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //添加对应位置view到容器中
//        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@");
//        System.out.println(mData.get(position));
//        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@");
        container.addView(mData.get(position));
        return mData.get(position);
    }

    /**
     * 获取孩子数量
     * @return
     */
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        System.out.println("#######################");
        container.removeView((View)object);
    }
}
