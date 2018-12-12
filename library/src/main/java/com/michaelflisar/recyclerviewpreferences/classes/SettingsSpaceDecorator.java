package com.michaelflisar.recyclerviewpreferences.classes;

import android.graphics.Rect;
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

        boolean alternativeHeaderAbove = pos > 0 && parent.getAdapter().getItemViewType(pos - 1) == R.id.id_adapter_setting_alternative_header_item;
        boolean headerAbove = pos > 0 && parent.getAdapter().getItemViewType(pos - 1) == R.id.id_adapter_setting_header_item;

        // Space über jedem Header, falls dieser nicht unter alternativem oder anderem Header ist und nicht an erster Stelle ist
        if (pos > 0 && !alternativeHeaderAbove && !headerAbove&& parent.getChildViewHolder(view) instanceof SettingsHeaderItem.ViewHolder)  {
            outRect.top = paddingBeforeHeader;
        }

        // First and last item padding top
        if (pos == 0 && paddingTop != 0) {
            outRect.top = paddingTop;
        }
        if (pos == count -1 && paddingBottom != 0) {
            outRect.bottom = paddingBottom;
        }

        // item padding left/right
        if (paddingHorizontal != 0) {
            outRect.left = paddingHorizontal;
            outRect.right = paddingHorizontal;
        }
    }
}