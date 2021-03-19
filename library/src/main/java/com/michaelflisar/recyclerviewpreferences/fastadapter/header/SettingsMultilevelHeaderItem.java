package com.michaelflisar.recyclerviewpreferences.fastadapter.header;

import android.view.View;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.kt.classes.SettingsText;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by flisar on 13.03.2017.
 */

public class SettingsMultilevelHeaderItem extends AbstractItem<SettingsMultilevelHeaderItem, SettingsMultilevelHeaderItem.ViewHolder> {

    private boolean mGrid;
    private IIcon mIcon;
    private SettingsText mTitle;
    private boolean mFlatStyle;

    public SettingsMultilevelHeaderItem(boolean grid, IIcon icon, int title, int id, boolean showDivider, boolean flatStyle) {
        this(grid, icon, new SettingsText(title), id, flatStyle);
    }

    public SettingsMultilevelHeaderItem(boolean grid, IIcon icon, SettingsText title, int id, boolean flatStyle) {
        mGrid = grid;
        mIcon = icon;
        mTitle = title;
        withIdentifier(id);
        if (flatStyle) {
            withFlatStyle();
        }
    }

    public SettingsMultilevelHeaderItem withFlatStyle() {
        mFlatStyle = true;
        return this;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    // ------------------
    // Item
    // ------------------

    @Override
    public int getType() {
        if (mGrid) {
            return R.id.id_adapter_setting_header_grid_multi_level_item;
        } else {
            return R.id.id_adapter_setting_header_multi_level_item;
        }
    }

    @Override
    public int getLayoutRes() {
        if (mGrid) {
            return R.layout.adapter_item_grid_multilevel_header;
        } else {
            return R.layout.adapter_item_multilevel_header;
        }
    }

    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);
        mTitle.display(viewHolder.tvTitle);

        if (mIcon != null) {
            viewHolder.ivIcon.setVisibility(View.VISIBLE);
            IconicsDrawable drawable = new IconicsDrawable(viewHolder.ivIcon.getContext(), mIcon);
//            drawable.paddingDp(data.getIconPaddingDp());
//            if (data.getIconColor() != null) {
//                drawable.color(data.getIconColor());
//            } else {
            drawable.color(Util.getSecondaryTextColor());
//            }
            viewHolder.ivIcon.setIcon(drawable);

//            IconicsDrawable d = ((IconicsDrawable) getIconView().getDrawable());
//            d.icon(icon).color(Util.getTextColor()).paddingDp(data.getIconPaddingDp());
        } else {
            viewHolder.ivIcon.setVisibility(mGrid ? View.INVISIBLE : View.GONE);
        }

//        Util.setCompoundIconLeftOrClear(viewHolder.binding.tvTitle, mIcon, 24, Util.getTextColor());

        if (mFlatStyle) {
            viewHolder.cardView.setElevation(0);
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected final CardView cardView;
        protected final TextView tvTitle;
        protected final IconicsImageView ivIcon;

        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            tvTitle = view.findViewById(R.id.tvTitle);
            ivIcon = view.findViewById(R.id.ivIcon);
        }
    }
}
