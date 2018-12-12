package com.michaelflisar.recyclerviewpreferences.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import androidx.core.graphics.ColorUtils;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;

public class SettingsRootView extends CardView {
    private Paint mPaint;
    private boolean mIsVisible = true;
    private boolean mIsEnabled = true;

    public SettingsRootView(Context context) {
        super(context);
        init();
    }

    public SettingsRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SettingsRootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        mPaint = new Paint();
//        ColorFilter filter = new PorterDuffColorFilter(SettingsManager.get().getState().isDarkTheme() ?
//                // material design: disabled text color is white with 50% opacity
//                ColorUtils.setAlphaComponent(Color.WHITE, 128) :
//                // material design: disabled text color is black with 38% opacity
//                ColorUtils.setAlphaComponent(Color.BLACK, 97), PorterDuff.Mode.SRC_ATOP);
//        ColorFilter filter = new PorterDuffColorFilter(SettingsManager.get().getState().isDarkTheme() ? Color.GRAY : Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        ColorFilter filter = new PorterDuffColorFilter(SettingsManager.get().getState().isDarkTheme() ? Color.WHITE : Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        mPaint.setColorFilter(filter);
        mPaint.setAlpha(SettingsManager.get().getState().isDarkTheme() ? 128 : 97);
//        mPaint.setColorFilter(new ColorMatrixColorFilter(cm));
    }

    public void setState(boolean enabled, boolean visible) {
        mIsVisible = visible;
        mIsEnabled = enabled;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (!mIsEnabled) {
            canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        }
        super.dispatchDraw(canvas);
        if (!mIsEnabled) {
            canvas.restore();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, mIsVisible ? heightMeasureSpec : 0);
        // keep padding, as it may contain the padding of the RecyclerViews decorator
        if (!mIsVisible) {
            setMeasuredDimension(getMeasuredWidth(), getPaddingBottom() + getPaddingTop());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // consume all touches if disabled
        if (!mIsEnabled) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}