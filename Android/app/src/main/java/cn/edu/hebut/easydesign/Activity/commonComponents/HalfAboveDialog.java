package cn.edu.hebut.easydesign.Activity.commonComponents;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Guideline;
import cn.edu.hebut.easydesign.R;
import cn.edu.hebut.easydesign.Tools.ResourcesTools;

public class HalfAboveDialog extends FrameLayout {
    private Guideline guideline;
    private FrameLayout dialog;
    private int take;
    private View contentView;
    private onClose onClose = null;

    public HalfAboveDialog(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.half_above_dialog_layout, this);
        take = attrs.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "dialog_height_dp", 200);
        Log.i("HAD", "take: " + take);
        if (take < 100) {
            take = 100;
        }
        boolean autoGone = attrs.getAttributeBooleanValue("http://schemas.android.com/apk/res-auto", "auto_gone", true);
        take = ResourcesTools.dp2px(take);
        float takePercent = attrs.getAttributeFloatValue("http://schemas.android.com/apk/res-auto", "dialog_height_percent", 0);
        if (takePercent != 0) {
            DisplayMetrics defaultDisplay = context.getResources().getDisplayMetrics();
            take = (int) (defaultDisplay.heightPixels * takePercent);
        }
        if (autoGone)
            setVisibility(GONE);
        guideline = findViewById(R.id.dialog_top);
        dialog = findViewById(R.id.dialog);
        TextView head = findViewById(R.id.useless_head);
        ImageView back = findViewById(R.id.close_dialog);
        OnClickListener close = new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        };
        head.setOnClickListener(close);
        back.setOnClickListener(close);
        guideline.setGuidelineEnd(take);
    }

    public void show(View content) {
        if (contentView != content) {
            // FIXME: 2020/3/11 
            contentView = content;
            dialog.removeAllViews();
            dialog.addView(content);
        }
        ValueAnimator animator = ValueAnimator.ofInt(10, take);
        animator.setDuration(100);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setVisibility(VISIBLE);
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                guideline.setGuidelineEnd((Integer) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public void close() {
        Log.i("HAD", "call close: from " + take);
        ValueAnimator animator = ValueAnimator.ofInt(take, 10);
        animator.setDuration(100);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(GONE);
                if (onClose != null) {
                    onClose.onClose(contentView);
                    onClose = null;
                }
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                guideline.setGuidelineEnd((Integer) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public void setOnClose(onClose onClose) {
        this.onClose = onClose;
    }

    public interface onClose {
        void onClose(View content);
    }
}
