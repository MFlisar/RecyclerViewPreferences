package com.michaelflisar.recyclerviewpreferences.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SettingsRootView extends CardView {
    //XXLayout could be LinearLayout, RelativeLayout or others
    private Paint m_paint;
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
        m_paint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        m_paint.setColorFilter(filter);
//        m_paint.setColorFilter(new ColorMatrixColorFilter(cm));
    }

    public void setState(boolean enabled, boolean visible) {
        mIsVisible = visible;
        mIsEnabled = enabled;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (!mIsEnabled) {
            canvas.saveLayer(null, m_paint, Canvas.ALL_SAVE_FLAG);
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
        if (!mIsVisible)
            setMeasuredDimension(getMeasuredWidth(), getPaddingBottom() + getPaddingTop());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        // consume all touches if disabled
        if (!mIsEnabled) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}