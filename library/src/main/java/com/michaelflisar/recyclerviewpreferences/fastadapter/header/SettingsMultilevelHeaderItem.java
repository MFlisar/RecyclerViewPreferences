package com.michaelflisar.recyclerviewpreferences.fastadapter.header;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.SettingsText;
import com.michaelflisar.recyclerviewpreferences.databinding.AdapterItemMultilevelHeaderBinding;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;

import java.util.List;

/**
 * Created by flisar on 13.03.2017.
 */

public class SettingsMultilevelHeaderItem extends AbstractItem<SettingsMultilevelHeaderItem, SettingsMultilevelHeaderItem.ViewHolder> {

    private IIcon mIcon;
    private SettingsText mTitle;
    private boolean mFlatStyle;

    public SettingsMultilevelHeaderItem(IIcon icon, int title, int id, boolean showDivider, boolean flatStyle) {
        this(icon, new SettingsText(title), id, flatStyle);
    }

    public SettingsMultilevelHeaderItem(IIcon icon, SettingsText title, int id, boolean flatStyle) {
        mIcon = icon;
        mTitle = title;
        withIdentifier(id);
        if (flatStyle)
            withFlatStyle();
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
        return R.id.id_adapter_setting_header_multi_level_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_item_multilevel_header;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);
        mTitle.display(viewHolder.binding.tvTitle);

        if (mIcon != null) {
            viewHolder.binding.ivIcon.setVisibility(View.VISIBLE);
            IconicsDrawable drawable = new IconicsDrawable(viewHolder.binding.ivIcon.getContext(), mIcon);
//            drawable.paddingDp(data.getIconPaddingDp());
//            if (data.getIconColor() != null) {
//                drawable.color(data.getIconColor());
//            } else {
                drawable.color(Util.getSecondaryTextColor());
//            }
            viewHolder.binding.ivIcon.setIcon(drawable);

//            IconicsDrawable d = ((IconicsDrawable) getIconView().getDrawable());
//            d.icon(icon).color(Util.getTextColor()).paddingDp(data.getIconPaddingDp());
        } else {
            viewHolder.binding.ivIcon.setVisibility(View.GONE);
        }

//        Util.setCompoundIconLeftOrClear(viewHolder.binding.tvTitle, mIcon, 24, Util.getTextColor());

        if (mFlatStyle)
            viewHolder.binding.cardView.setElevation(0);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.binding.unbind();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected final AdapterItemMultilevelHeaderBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
