package com.michaelflisar.recyclerviewpreferences.fastadapter.header;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.SettingsText;
import com.michaelflisar.recyclerviewpreferences.databinding.AdapterItemHeaderBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.utils.Definitions;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.expandable.items.AbstractExpandableItem;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;

import java.util.List;

/**
 * Created by flisar on 13.03.2017.
 */

public class SettingsHeaderItem<T extends BaseSettingsItem> extends AbstractExpandableItem<SettingsHeaderItem, SettingsHeaderItem.ViewHolder, T> {

    private boolean mExpandable;
    private IIcon mIcon;
    private SettingsText mTitle;
    private OnClickListener<SettingsHeaderItem> mOnClickListener;

    //we define a clickListener in here so we can directly animate
    final private OnClickListener<SettingsHeaderItem> onClickListener = new OnClickListener<SettingsHeaderItem>() {
        @Override
        public boolean onClick(View v, IAdapter adapter, SettingsHeaderItem item, int position) {
            if (mExpandable && item.getSubItems() != null) {
                if (!item.isExpanded()) {
                    ViewCompat.animate(v.findViewById(R.id.ivIcon)).rotation(180).start();
                } else {
                    ViewCompat.animate(v.findViewById(R.id.ivIcon)).rotation(0).start();
                }
                // this notifies the header about changes so that the SettingsSpaceDecorator can update the margins of the header as well
                // this calls through to the underlying RV and skips fast adapters expand/collapse logic
                adapter.getFastAdapter().notifyItemRangeChanged(position, 1, Definitions.PAYLOAD_UPDATE_HEADER_DECORATOR);
                return mOnClickListener == null || mOnClickListener.onClick(v, adapter, item, position);
            }
            return mOnClickListener != null && mOnClickListener.onClick(v, adapter, item, position);
        }
    };

    public SettingsHeaderItem(boolean expandable, IIcon icon, int title, int id) {
        this(expandable, icon, new SettingsText(title), id);
    }

    public SettingsHeaderItem(boolean expandable, IIcon icon, SettingsText title, int id) {
        mExpandable = expandable;
        mIcon = icon;
        mTitle = title;
        withIdentifier(id);
        withIsExpanded(true);
    }

    public void setExpandable(boolean expandable) {
        mExpandable = expandable;
    }

    @Override
    public boolean isAutoExpanding() {
        return mExpandable;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public OnClickListener<SettingsHeaderItem> getOnClickListener() {
        return mOnClickListener;
    }

    // ------------------
    // Item
    // ------------------

    public SettingsHeaderItem withOnClickListener(OnClickListener<SettingsHeaderItem> mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        return this;
    }

    @Override
    public OnClickListener<SettingsHeaderItem> getOnItemClickListener() {
        return onClickListener;
    }

    @Override
    public boolean isSelectable() {
//        return getSubItems() == null;
        return false;
    }

    @Override
    public int getType() {
        return R.id.id_adapter_setting_header_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.adapter_item_header;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        if (payloads != null && payloads.size() == 1 && payloads.contains(Definitions.PAYLOAD_UPDATE_HEADER_DECORATOR)) {
            // only the decorator needs to be updated!
            return;
        }
        super.bindView(viewHolder, payloads);
        mTitle.display(viewHolder.binding.tvTitle);

        // Util.setCompoundIconLeftOrClear(viewHolder.binding.tvTitle, mIcon, ...);

        viewHolder.binding.ivIcon.setVisibility(mExpandable && getSubItems() != null ? View.VISIBLE : View.GONE);
        if (mExpandable) {
            if (isExpanded()) {
                ViewCompat.setRotation(viewHolder.binding.ivIcon, 0);
            } else {
                ViewCompat.setRotation(viewHolder.binding.ivIcon, 180);
            }
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.binding.ivIcon.clearAnimation();
        holder.binding.unbind();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected final AdapterItemHeaderBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            binding.ivIcon.setImageDrawable(new IconicsDrawable(view.getContext()).icon(GoogleMaterial.Icon.gmd_keyboard_arrow_up).sizeDp(16).color(binding.tvTitle.getTextColors()));
        }
    }
}
