package com.michaelflisar.recyclerviewpreferences.fastadapter.settings;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.settings.SpinnerSetting;
import com.michaelflisar.recyclerviewpreferences.classes.SimpleSpinnerListener;
import com.michaelflisar.recyclerviewpreferences.databinding.AdapterSettingItemSpinnerBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.BaseSettingViewHolder;
import com.michaelflisar.recyclerviewpreferences.fastadapter.hooks.BaseCustomEventHook;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.views.FixedSwitch;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;

/**
 * Created by Michael on 20.05.2017.
 */

public class SpinnerSettingItem<Parent extends IItem & IExpandable, CLASS, SettData extends ISettData<Integer, CLASS, SettData, VH>, VH extends SpinnerSettingItem.SpinnerViewHolder<Integer, CLASS, SettData, VH>> extends
        BaseSettingsItem<Parent, Integer, CLASS, SettData, VH> {

    // ------------------
    // Factory
    // ------------------

    public SpinnerSettingItem(boolean globalSetting, boolean compact, BaseSetting<Integer, CLASS, SettData, VH> data, ISettCallback callback, boolean withBottomDivider) {
        super(globalSetting, compact, data, callback, withBottomDivider);
    }

    // ------------------
    // Constructor, ID, Layout
    // ------------------

    @Override
    public VH getViewHolder(View v) {
        VH vh = (VH) new SpinnerViewHolder(v, mGlobalSetting, mCompact);
        // prevent Spinner in Row2 from being editable
        vh.getValueBottomView().setOnTouchListener((view, motionEvent) -> true);
        return vh;
    }

    protected void onSpinnerChanged(VH viewHolder, Integer index, boolean topSwitch) {
        if (mGlobalSetting && !topSwitch) {
            return;
        }

        boolean global = mGlobalSetting || !topSwitch;
        int currentID = mData.getValue((CLASS)mCallback.getCustomSettingsObject(), global);
        int newId = ((SpinnerSetting<CLASS, SettData, VH>) mData).getListId(index);
        if (currentID != newId) {
            if (mData.setValue((CLASS)mCallback.getCustomSettingsObject(), global, newId)) {
                mData.onValueChanged(global ? mData.getDefaultId() : mData.getCustomId(), viewHolder.getActivity(), global, (CLASS)mCallback.getCustomSettingsObject());
            }
        }
    }

    @Override
    public void unbindView(VH holder) {
        ArrayAdapter adapter = (ArrayAdapter) ((Spinner) holder.getValueTopView()).getAdapter();
        if (adapter != null) {
            adapter.clear();
            ((Spinner) holder.getValueTopView()).setAdapter(adapter);
        }
        adapter = (ArrayAdapter) ((Spinner) holder.getValueBottomView()).getAdapter();
        if (adapter != null) {
            adapter.clear();
            ((Spinner) holder.getValueBottomView()).setAdapter(adapter);
        }
        super.unbindView(holder);
    }

    // ------------------
    // ViewHolder
    // ------------------

    public static class SpinnerViewHolder<Data, CLASS, SettData extends ISettData<Data, CLASS, SettData, VH>, VH extends SpinnerViewHolder<Data, CLASS, SettData, VH>> extends BaseSettingViewHolder<AdapterSettingItemSpinnerBinding, Data, CLASS, SettData, VH> {
        public SpinnerViewHolder(View view, boolean globalSetting, boolean compact) {
            super(view, globalSetting, compact);
        }

        @Override
        public void onUpdateCustomViewDependencies(boolean globalSetting) {
            super.onUpdateCustomViewDependencies(globalSetting);
            // make sure, that spinner does not consume touches if disabled so that the click listener of row1 works
            if (globalSetting) {
                getValueTopView().setClickable(true);
            } else {
                boolean b = getUseCustomSwitch().isChecked();
                getValueTopView().setClickable(b);
            }
        }

        @Override
        public void onShowChangeSetting(VH vh, BaseSetting<Data, CLASS, SettData, VH> data, Activity activity, ViewDataBinding binding,
                SettData settData, boolean global, CLASS customSettingsObject) {
            data.onShowChangeSetting(vh, activity, binding, settData, global, customSettingsObject);
        }

        @Override
        public ViewDataBinding getBinding() {
            return binding;
        }

        @Override
        public FixedSwitch getUseCustomSwitch() {
            return binding.swEnable;
        }

        @Override
        public TextView getTitleTextView() {
            return binding.tvTitle;
        }

        @Override
        public TextView getSubTitleTextView() {
            return binding.tvSubTitle;
        }

        @Override
        public LinearLayout getTopValueContainer() {
            return binding.llCustomValueContainer;
        }

        @Override
        public TextView getIsUsingGlobalTextView() {
            return binding.tvIsUsingDefault;
        }

        @Override
        public ImageView getIconView() {
            return binding.ivIcon;
        }

        @Override
        public View getInfoButton() {
            return binding.btInfo;
        }

        @Override
        public View getValueTopView() {
            return binding.spValueTop;
        }

        @Override
        public View getValueBottomView() {
            return binding.spValueBottom;
        }

        @Override
        public View getInnerDivider() {
            return binding.vDividerRow;
        }

        @Override
        public View getRow1() {
            return binding.llRow1;
        }

        @Override
        public View getRow2() {
            return binding.llRow2;
        }
    }

    // ------------------
    // Event Hooks
    // ------------------

    public static class SettingsSpinnerTopEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof SpinnerViewHolder) {
                return ((AdapterSettingItemSpinnerBinding) ((SpinnerViewHolder) viewHolder).getBinding()).spValueTop;
            }
            return null;
        }

        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            view.post(() -> {
                SimpleSpinnerListener listener = new SimpleSpinnerListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        onEventOccurred(parent, viewHolder, fastAdapter);
                    }
                };
                ((Spinner) view).setOnItemSelectedListener(listener);
            });
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            ((SpinnerSettingItem) item).onSpinnerChanged((SpinnerViewHolder) viewHolder, ((Spinner) view).getSelectedItemPosition(), true);
        }
    }

    public static class SettingsSpinnerBottomEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof SpinnerViewHolder) {
                return ((AdapterSettingItemSpinnerBinding) ((SpinnerViewHolder) viewHolder).getBinding()).spValueBottom;
            }
            return null;
        }

        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            view.post(() -> {
                SimpleSpinnerListener listener = new SimpleSpinnerListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        onEventOccurred(parent, viewHolder, fastAdapter);
                    }
                };
                ((Spinner) view).setOnItemSelectedListener(listener);
            });
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            ((SpinnerSettingItem) item).onSpinnerChanged((SpinnerViewHolder) viewHolder, ((Spinner) view).getSelectedItemPosition(), false);
        }
    }
}
