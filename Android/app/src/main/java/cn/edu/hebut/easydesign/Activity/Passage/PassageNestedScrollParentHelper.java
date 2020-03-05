package cn.edu.hebut.easydesign.Activity.Passage;

import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import cn.edu.hebut.easydesign.Activity.NestedScrollParentHelper;

public class PassageNestedScrollParentHelper extends NestedScrollParentHelper {
    protected NestedScrollView topView;
    protected int cacheFixedHeight;
    private final static String TAG = "PNSPH";

    /**
     * Construct a new helper for a given ViewGroup
     * Which the first view will hide the second view on the top and the third child will the main content
     * use this after or in @function onFinishInflate for getting the children of this view group
     *
     * @param viewGroup the layout
     */
    public PassageNestedScrollParentHelper(@NonNull final ViewGroup viewGroup) {
        super(viewGroup);
        this.viewGroup = viewGroup;
        scroller = new OverScroller(viewGroup.getContext());
        topView = (NestedScrollView) viewGroup.getChildAt(0);
//        Log.i("helper", "top: " + topView);
        fixedView = viewGroup.getChildAt(1);
//        Log.i("helper", "fixed: " + fixedView);
        contentView = viewGroup.getChildAt(2);
//        Log.i("helper", "content: " + contentView);
        topView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        fixedHeight = fixedView.getHeight();
//                        Log.i("helper", "" + viewGroup.getHeight() + " " + topHeight + " " + fixedHeight + " " + contentView.getHeight() + " " + topView.getChildAt(0).getHeight());
                        if (fixedHeight != 0) {
                            cacheFixedHeight = fixedHeight;
                        }
                        if (fixedHeight != fixedView.getMeasuredHeight() || topHeight != topView.getHeight()) {
                            topHeight = topView.getHeight();
                            topHeightTrue = ((ViewGroup) topView).getChildAt(0).getHeight();
                            if (fixedHeight == 0) {
                                ViewGroup.LayoutParams lp = fixedView.getLayoutParams();
                                lp.height = cacheFixedHeight;
                                fixedView.setLayoutParams(lp);
                            }
                            ViewGroup.LayoutParams lp = contentView.getLayoutParams();
                            lp.height = viewGroup.getHeight() - fixedView.getHeight();
                            contentView.setLayoutParams(lp);
                        }

                    }
                }
        );
        topView.setNestedScrollingEnabled(false);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (topCatch) {
            topView.fling((int) velocityY);
            return true;
        }
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (topCatch) {
            topView.fling((int) velocityY);
            return true;
        }
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
//        Log.i(TAG, "onStartNestedScroll: " + target);
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//        Log.i(TAG, "onNestedPreScroll: ");
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        Log.i(TAG, "onNestedScroll: ");
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    public boolean onTouchEvent(MotionEvent event) {
//        Log.i(TAG, "onTouchEvent: " + event.getAction());

        if (tracker == null) {
            tracker = VelocityTracker.obtain();
        }
        tracker.addMovement(event);
        switch (event.getAction()) {
            //按下
            case MotionEvent.ACTION_DOWN:
                lastY = (int) event.getRawY();
                if (!scroller.isFinished()) { //fling
                    scroller.abortAnimation();
                }
                break;
            //移动
            case MotionEvent.ACTION_MOVE:
//                Log.i("NSPH", "when move");
                int y = (int) (event.getRawY());
                int dy = lastY - y;
                lastY = y;
                if (topCatch) {
                    if (topView.canScrollVertically(dy)) {
                        topView.scrollBy(0, dy);
                        break;
                    } else {
                        topCatch = false;
                    }
                }
                //首次点击或者滑动的不是NestedScrollChild的时候是拿不到target的
                if (null != target) {
                    if (!target.canScrollVertically(-1)) {
                        viewGroup.scrollBy(0, dy); //mTarget处于顶部不能继续下滑的时候才能滑该View自己
                        if (!viewGroup.canScrollVertically(-1)) {
                            topCatch = true;
                            topView.scrollBy(0, dy);
                        }
                    } else {
//                        Log.e("cys", "scrollBy 需要传给mTarget");
                        target.onTouchEvent(event);
                        break;
                    }
                    //解决当该View已经拦截了事件,而mPinkView已经固定,仍然继续上滑时需要划出mTarget
                    if (shouldIntercept && viewGroup.getScrollY() == topHeight) {
                        // Log.e("cys", "ACTION_MOVE 需要传给mTarget");
                        target.onTouchEvent(event);
                    }
                } else {
//                    Log.i("NSPH", "scroll");
                    viewGroup.scrollBy(0, dy);
                    if (!viewGroup.canScrollVertically(-1)) {
                        topCatch = true;
                        topView.scrollBy(0, dy);
                    }
                }


                break;
            case MotionEvent.ACTION_UP:
                tracker.computeCurrentVelocity(1000);
                int vy = (int) tracker.getYVelocity();
                if (topCatch) {
                    topView.fling(-vy);
                    break;
                }
                if (null != target) {
                    //解决当该View已经拦截了事件,飞划mTarget有效果
                    if (shouldIntercept && viewGroup.getScrollY() == topHeight) {
//                        Log.e("cys", "ACTION_UP 需要传给mTarget");
                        target.onTouchEvent(event);
                        break;
                    }
                }

                fling(-vy);

                break;
        }

        return true;
    }

    private boolean topCatch = false;

    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.i(TAG, "onInterceptTouchEvent: " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) ev.getRawX();
                lastY = (int) ev.getRawY();
                shouldIntercept = false;
                topCatch = viewGroup.getScrollY() <= 50;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (ev.getRawX() - lastX);
                int dy = (int) (ev.getRawY() - lastY);
                boolean isVerticalScroll = Math.abs(dy) > Math.abs(dx);
                boolean isPullUp = dy < 0;
                lastX = (int) ev.getRawX();
                lastY = (int) ev.getRawY();

                if (isVerticalScroll && isPullUp) {
                    topCatch = viewGroup.getScrollY() <= 0 && topView.canScrollVertically(1);
//                    Log.i(TAG, "onInterceptTouchEvent: up" + topCatch + viewGroup.getScrollY() + " " + topView.canScrollVertically(1));
                } else {
                    topCatch = viewGroup.getScrollY() <= 0 && topView.canScrollVertically(-1);
//                    Log.i(TAG, "onInterceptTouchEvent: down" + topCatch + viewGroup.getScrollY() + " " + topView.canScrollVertically(-1));
                }
                if (topCatch) {
                    shouldIntercept = true;
                    return true;
                }
                if (isVerticalScroll && topView.isShown()) {
                    if (isPullUp) { //上拉
//                        Log.i("NSPH", "top: " +topHeight + " scroll y: " + viewGroup.getScrollY() + " target: " + target);
                        if (viewGroup.getScrollY() >= 0 && viewGroup.getScrollY() < topHeight) {
                            //target到顶不能往下滑动时,让父View拦截
                            shouldIntercept = null == target || !target.canScrollVertically(-1);
                        }

                    } else { //下拉
                        if (viewGroup.getScrollY() > 0 && viewGroup.getScrollY() <= topHeight) {
                            //target到顶不能往下滑动时,让父View拦截
                            shouldIntercept = null == target || !target.canScrollVertically(-1);
                        }
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                dx = (int) (ev.getRawX() - lastX);
                dy = (int) (ev.getRawY() - lastY);
                isVerticalScroll = Math.abs(dy) > Math.abs(dx);
                isPullUp = dy < 0;
                lastX = (int) ev.getRawX();
                lastY = (int) ev.getRawY();

                if (isVerticalScroll && isPullUp) {
                    topCatch = viewGroup.getScrollY() <= 0 && topView.canScrollVertically(1);
//                    Log.i(TAG, "onInterceptTouchEvent: up" + topCatch + viewGroup.getScrollY() + " " + topView.canScrollVertically(1));
                } else {
                    topCatch = viewGroup.getScrollY() <= 0 && topView.canScrollVertically(-1);
//                    Log.i(TAG, "onInterceptTouchEvent: down" + topCatch + viewGroup.getScrollY() + " " + topView.canScrollVertically(-1));
                }
                if (topCatch) {
                    shouldIntercept = true;
                    return true;
                }
                shouldIntercept = false;
                break;
        }
//        Log.e("cys", "onInterceptTouchEvent->" + shouldIntercept);
        return shouldIntercept;
    }

}
