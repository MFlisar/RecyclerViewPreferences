package com.michaelflisar.recyclerviewpreferences.classes;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.fastadapter.header.SettingsHeaderItem;

/**
 * Created by flisar on 29.05.2017.
 */

public class SettingsSpaceDecorator extends RecyclerView.ItemDecoration {

    private final int paddingHorizontal;
    private final int paddingBeforeHeader;
    private final int paddingTopBottom;

    public SettingsSpaceDecorator(int paddingHorizontal, int paddingBeforeHeader, int paddingTopBottom) {
        this.paddingHorizontal = paddingHorizontal;
        this.paddingBeforeHeader = paddingBeforeHeader;
        this.paddingTopBottom = paddingTopBottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int pos = parent.getChildAdapterPosition(view);
        int count = parent.getAdapter().getItemCount();

        boolean alternativeHeaderAbove = pos > 0 && parent.getAdapter().getItemViewType(pos - 1) == R.id.id_adapter_setting_alternative_header_item;

        // Space über jedem Header, falls dieser nicht unter alternativem Header ist und nicht an erster Stelle ist
        if (pos > 0 && !alternativeHeaderAbove && parent.getChildViewHolder(view) instanceof SettingsHeaderItem.ViewHolder)  {
            outRect.top = paddingBeforeHeader;
        }

        // First and last item padding top
        if (pos == 0 && paddingTopBottom != 0) {
            outRect.top = paddingBeforeHeader;
        }
        if (pos == count -1 && paddingTopBottom != 0) {
            outRect.bottom = paddingBeforeHeader;
        }

        // item padding left/right
        if (paddingHorizontal != 0) {
            outRect.left = paddingHorizontal;
            outRect.right = paddingHorizontal;
        }
    }
}