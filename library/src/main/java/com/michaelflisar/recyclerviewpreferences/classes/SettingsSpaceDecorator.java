package com.michaelflisar.recyclerviewpreferences.classes;

import android.graphics.Rect;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.fastadapter.header.SettingsHeaderItem;

/**
 * Created by flisar on 29.05.2017.
 */

public class SettingsSpaceDecorator extends RecyclerView.ItemDecoration {

    private final int paddingHorizontal;
    private final int paddingBeforeHeader;
    private final int paddingTop;
    private final int paddingBottom;

    public SettingsSpaceDecorator(int paddingHorizontal, int paddingBeforeHeader, int paddingTop, int paddingBottom) {
        this.paddingHorizontal = paddingHorizontal;
        this.paddingBeforeHeader = paddingBeforeHeader;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int pos = parent.getChildAdapterPosition(view);
        int count = parent.getAdapter().getItemCount();
        int span = parent.getLayoutManager() instanceof GridLayoutManager ? ((GridLayoutManager)parent.getLayoutManager()).getSpanCount() : 1;

        boolean alternativeHeaderAbove = pos > 0 && parent.getAdapter().getItemViewType(pos - 1) == R.id.id_adapter_setting_alternative_header_item;
        boolean headerAbove = pos > 0 && parent.getAdapter().getItemViewType(pos - 1) == R.id.id_adapter_setting_header_item;

        boolean isLastRow = pos == count -1;
        if (span != 1) {
            GridLayoutManager glm = (GridLayoutManager)parent.getLayoutManager();
            int rows = 0;
            int spanCurrent = 0;
            int spanSumTotal = 0;
            int spanCount;
            for (int i = 0; i < count; i++) {
                spanCount = glm.getSpanSizeLookup().getSpanSize(i);
                spanSumTotal += spanCount;
                if (i == pos)
                    spanCurrent = spanSumTotal;
            }

            isLastRow = Math.ceil((float)spanSumTotal / (float)span) == Math.ceil((float)spanCurrent / (float)span);
        }

        // Space über jedem Header, falls dieser nicht unter alternativem oder anderem Header ist und nicht an erster Stelle ist
        if (pos > 0 && !alternativeHeaderAbove && !headerAbove&& parent.getChildViewHolder(view) instanceof SettingsHeaderItem.ViewHolder)  {
            outRect.top = paddingBeforeHeader;
        }

        // First and last item/row padding top
        if (pos == 0 && paddingTop != 0) {
            outRect.top = paddingTop;
        }
        if (isLastRow && paddingBottom != 0) {
            outRect.bottom = paddingBottom;
        }

        // item padding left/right
        if (paddingHorizontal != 0) {
            outRect.left = paddingHorizontal;
            outRect.right = paddingHorizontal;
        }
    }
}