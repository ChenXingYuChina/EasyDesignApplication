package cn.edu.hebut.easydesign.Activity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingParentHelper;

public class NestedScrollParentHelper extends NestedScrollingParentHelper {
    private View target;
    private OverScroller scroller;
    private View topView;
    private View fixedView;
    private View contentView;
    private int lastX;
    private int lastY;
    private int topHeight;
    private int fixedHeight;
    private VelocityTracker tracker;
    private ViewGroup viewGroup;
    private boolean shouldIntercept;

    /**
     * Construct a new helper for a given ViewGroup
     * Which the first view will hide the second view on the top and the third child will the main content
     * use this after or in @function onFinishInflate for getting the children of this view group
     *
     * @param viewGroup the layout
     */
    public NestedScrollParentHelper(@NonNull final ViewGroup viewGroup) {
        super(viewGroup);
        this.viewGroup = viewGroup;
        scroller = new OverScroller(viewGroup.getContext());
        topView = viewGroup.getChildAt(0);
        fixedView = viewGroup.getChildAt(1);
        contentView = viewGroup.getChildAt(2);
        topView.getViewTreeObserver().addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Log.i("helper", "" + viewGroup.getHeight() + " " + fixedHeight + " " + contentView.getHeight());
                    if (fixedHeight != fixedView.getMeasuredHeight()) {
                        topHeight = topView.getMeasuredHeight();
                        ViewGroup.LayoutParams lp = contentView.getLayoutParams();
                        lp.height = viewGroup.getHeight() - fixedView.getHeight();
                        contentView.setLayoutParams(lp);
                    }
                    fixedHeight = fixedView.getMeasuredHeight();

                }
            }
        );
    }

    public boolean canScrollVertically(int direction) {
        return direction > 0 || viewGroup.getScrollY() > 0;
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        this.target = target;
        return target instanceof NestedScrollingChild;
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (showTopView(-dy, target) || hideTopView(-dy)) {//如果需要显示或隐藏图片，即需要自己(parent)滚动
            viewGroup.scrollBy(0, dy);//滚动
            consumed[1] = dy;//告诉child我消费了多少
        }
        if (viewGroup.getScrollY() == 0) {
            //向下滑动mTarget带动parent完全划出,继续向下滑需要划出parent的parent
            viewGroup.getParent().requestDisallowInterceptTouchEvent(false);
        }
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e("cys", "onNestedScroll-> dyUnconsumed:" + dyUnconsumed + " dyConsumed:" + dyConsumed);
        Log.i("NS", (target == this.target) + "");
        if (dyUnconsumed > 0 && this.target.canScrollVertically(1)) {
            if (viewGroup.getScrollY() == 0) {
                //向下滑动mTarget带动parent完全划出,继续向下滑需要划出parent的parent
                viewGroup.getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                viewGroup.getParent().requestDisallowInterceptTouchEvent(true);
            }
            // 如果子View还有未消费的,可以继续消费
            viewGroup.scrollBy(0, dyUnconsumed);//滚动
        }
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (viewGroup.getScrollY() > 0 && viewGroup.getScrollY() < topHeight) {
            fling((int) velocityY);
            return true; //飞滑一旦消费就是全部消费,没有部分消费.
        }
        return false;
    }
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (!consumed) {
            fling((int) velocityY);
            return true;
        }
        return false;
    }

    private boolean showTopView(int dy, View target) {
        if (dy > 0 && topView.isShown()) {
            if (target instanceof NestedScrollingChild) {
                return viewGroup.getScrollY() > 0 &&/* target.getScrollY() == 0*/ !target.canScrollVertically(-1); //显示顶部
            }
        }
        return false;
    }

    private boolean hideTopView(int dy) {
        return dy < 0 && topView.isShown() && viewGroup.getScrollY() < topHeight;
    }


    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > topHeight) {
            y = topHeight;
        }
        viewGroup.scrollTo(0, y);
    }

    public int getTopHeight() {
        return topHeight;
    }

    private void fling(int velocityY) {
        if (topView.isShown()) {
            scroller.fling(0, viewGroup.getScrollY(), 0, velocityY, 0, 0, 0, topHeight);
            viewGroup.invalidate();
        }
    }

    /* use it after super*/
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            viewGroup.scrollTo(0, scroller.getCurrY());
            viewGroup.postInvalidate();
        }
    }


    public boolean onTouchEvent(MotionEvent event) {
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
                int y = (int) (event.getRawY());
                int dy = y - lastY;
                lastY = y;
                //首次点击或者滑动的不是NestedScrollChild的时候是拿不到target的
                if (null != target) {
                    if (!target.canScrollVertically(-1)) {
                        viewGroup.scrollBy(0, -dy); //mTarget处于顶部不能继续下滑的时候才能滑该View自己
                    } else {
                        Log.e("cys", "scrollBy 需要传给mTarget");
                        target.onTouchEvent(event);
                        break;
                    }
                    //解决当该View已经拦截了事件,而mPinkView已经固定,仍然继续上滑时需要划出mTarget
                    if (shouldIntercept && viewGroup.getScrollY() == topHeight) {
                        // Log.e("cys", "ACTION_MOVE 需要传给mTarget");
                        target.onTouchEvent(event);
                    }
                } else {
                    viewGroup.scrollBy(0, -dy);
                }


                break;
            case MotionEvent.ACTION_UP:
                tracker.computeCurrentVelocity(1000);
                int vy = (int) tracker.getYVelocity();
                if (null != target) {
                    //解决当该View已经拦截了事件,飞划mTarget有效果
                    if (shouldIntercept && viewGroup.getScrollY() == topHeight) {
                        Log.e("cys", "ACTION_UP 需要传给mTarget");
                        target.onTouchEvent(event);
                        break;
                    }
                }

                fling(-vy);

                break;
        }

        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) ev.getRawX();
                lastY = (int) ev.getRawY();
                shouldIntercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (ev.getRawX() - lastX);
                int dy = (int) (ev.getRawY() - lastY);
                boolean isVerticalScroll = Math.abs(dy) > Math.abs(dx);
                boolean isPullUp = dy < 0;
                lastX = (int) ev.getRawX();
                lastY = (int) ev.getRawY();

                if (isVerticalScroll && topView.isShown()) {
                    if (isPullUp) { //上拉
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
                shouldIntercept = false;
                break;
        }
        Log.e("cys", "onInterceptTouchEvent->" + shouldIntercept);
        return shouldIntercept;
    }

}
