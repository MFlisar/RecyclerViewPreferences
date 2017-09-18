package com.michaelflisar.recyclerviewpreferences.fastadapter.header;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.SettingsText;
import com.michaelflisar.recyclerviewpreferences.databinding.AdapterItemMultilevelHeaderBinding;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.iconics.typeface.IIcon;

import java.util.List;

/**
 * Created by flisar on 13.03.2017.
 */

public class SettingsMultilevelHeaderItem extends AbstractItem<SettingsMultilevelHeaderItem, SettingsMultilevelHeaderItem.ViewHolder> {

    private IIcon mIcon;
    private SettingsText mTitle;

    public SettingsMultilevelHeaderItem(IIcon icon, int title, int id, boolean showDivider) {
        this(icon, new SettingsText(title), id);
    }

    public SettingsMultilevelHeaderItem(IIcon icon, SettingsText title, int id) {
        mIcon = icon;
        mTitle = title;
        withIdentifier(id);
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
        Util.setCompoundIconLeftOrClear(viewHolder.binding.tvTitle, mIcon, 24, Util.getTextColor());
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
