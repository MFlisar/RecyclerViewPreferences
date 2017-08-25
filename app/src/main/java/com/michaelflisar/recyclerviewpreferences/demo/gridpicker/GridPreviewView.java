package com.michaelflisar.recyclerviewpreferences.demo.gridpicker;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class GridPreviewView extends View {

    private Paint mPaint;
    private int size = 0;
    private int mSpacing = 5;

    private int mRows = 2;
    private int mCols = 2;

    public GridPreviewView(Context context) {
        super(context);
        initPaints();
    }

    public GridPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public GridPreviewView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaints();
    }

    private void initPaints() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        TypedValue typedValue = new TypedValue();
        TypedArray a = getContext().obtainStyledAttributes(typedValue.data, new int[]{com.michaelflisar.recyclerviewpreferences.R.attr.colorAccent});
        int color = a.getColor(0, 0);
        a.recycle();
        mPaint.setColor(color);
    }

    public void setGrid(int rows, int cols) {
        mRows = rows;
        mCols = cols;
        invalidate();
    }

    public void setCols(int cols) {
        mCols = cols;
        invalidate();
    }

    public void setRows(int rows) {
        mRows = rows;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        size = Math.min(w, h);
//        setMeasuredDimension(size, size);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        float rectWidth = ((float) size - ((mCols - 1) * mSpacing)) / mCols;
        float rectHeight = ((float) size - ((mRows - 1) * mSpacing)) / mRows;
        float left, top;
        for (int i = 0; i < mCols; i++) {
            for (int j = 0; j < mRows; j++) {
                left = i * rectWidth + (i + 1) * mSpacing;
                top = j * rectHeight + (j + 1) * mSpacing;
                canvas.drawRect(left, top, left + rectWidth, top + rectHeight, mPaint);
            }
        }
    }
}