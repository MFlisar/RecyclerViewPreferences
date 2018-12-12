package com.michaelflisar.recyclerviewpreferences.classes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItem;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by flisar on 29.05.2017.
 */

// copied from DividerItemDecoration and adjusted!
public class SettingsDividerDecorator extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private final int paddingHorizontal;

    private Drawable mDivider;
    private final Rect mBounds = new Rect();

    public SettingsDividerDecorator(Context context, int paddingHorizontal) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        this.paddingHorizontal = paddingHorizontal;
    }

    private boolean isHeader(int type) {
        return type == R.id.id_adapter_setting_alternative_header_item ||
                type == R.id.id_adapter_setting_header_item ||
                type == R.id.id_adapter_setting_header_multi_level_item;
    }

    private int getViewType(int pos, int count, FastAdapter fa, RecyclerView parent) {
        IItem item = pos < count ? fa.getItem(pos) : null;
        return item != null && pos < count ? parent.getAdapter().getItemViewType(pos) : -1;
    }

    private boolean hasDivider(View view, RecyclerView parent) {
        int pos = parent.getChildAdapterPosition(view);
        int count = parent.getAdapter().getItemCount();

        FastAdapter fa = (FastAdapter) parent.getAdapter();

        int currentType = pos < count ? getViewType(pos, count, fa, parent) : -1;
        int belowType = pos < count - 1 && pos < count ? getViewType(pos + 1, count, fa, parent) : -1;
//        int aboveType = pos > 0 && pos < count ? getViewType(pos - 1, count, fa, parent) : -1;

        boolean isHeader = isHeader(currentType);
        boolean isHeaderBelow = isHeader(belowType);
//        boolean isHeaderAbove = isHeader(aboveType);

        if (pos < count - 1 && !isHeader && !isHeaderBelow) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        boolean hasDivider = hasDivider(view, parent);
        if (hasDivider) {
            if (mDivider == null) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            }
        } else {
            outRect.setEmpty();
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || mDivider == null) {
            return;
        }
        drawVertical(c, parent);
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (hasDivider(child, parent)) {
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
                final int top = bottom - mDivider.getIntrinsicHeight();
                mDivider.setBounds(left + paddingHorizontal, top, right - paddingHorizontal, bottom);
                mDivider.draw(canvas);
            }
        }
        canvas.restore();
    }
}