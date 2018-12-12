package com.michaelflisar.recyclerviewpreferences.fastadapter.header;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.SettingsText;
import com.michaelflisar.recyclerviewpreferences.databinding.AdapterItemAlternativeHeaderBinding;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

/**
 * Created by flisar on 13.03.2017.
 */

public class SettingsAlternativeHeaderItem extends AbstractItem<SettingsAlternativeHeaderItem, SettingsAlternativeHeaderItem.ViewHolder> {

    public SettingsText title;

    // ------------------
    // Item
    // ------------------

    public SettingsAlternativeHeaderItem(int title, int id) {
       this(new SettingsText(title), id);
    }

    public SettingsAlternativeHeaderItem(String title, int id) {
        this(new SettingsText(title), id);
    }

    public SettingsAlternativeHeaderItem(SettingsText title, int id) {
        this.title = title;
        withIdentifier(id);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.id_adapter_setting_alternative_header_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_item_alternative_header;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);
        title.display(viewHolder.binding.tvTitle);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.binding.unbind();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected final AdapterItemAlternativeHeaderBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }
}
