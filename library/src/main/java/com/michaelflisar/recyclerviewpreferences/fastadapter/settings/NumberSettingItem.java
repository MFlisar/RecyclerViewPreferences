package com.michaelflisar.recyclerviewpreferences.fastadapter.settings;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.databinding.AdapterSettingItemNumberBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.BaseSettingViewHolder;
import com.michaelflisar.recyclerviewpreferences.fastadapter.hooks.BaseCustomEventHook;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.settings.NumberSetting;
import com.michaelflisar.recyclerviewpreferences.views.FixedSwitch;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;

import java.util.List;

/**
 * Created by Michael on 20.05.2017.
 */

public class NumberSettingItem<Parent extends IItem & IExpandable, CLASS, SettData extends ISettData<Integer, CLASS, SettData, VH>, VH extends NumberSettingItem.NumberViewHolder<Integer, CLASS,
        SettData, VH>> extends
        BaseSettingsItem<Parent, Integer, CLASS, SettData, VH> {

    // ------------------
    // Factory
    // ------------------

    public NumberSettingItem(boolean globalSetting, boolean compact, BaseSetting<Integer, CLASS, SettData, VH> data, ISettCallback callback, boolean withBottomDivider) {
        super(globalSetting, compact, data, callback, withBottomDivider);
    }

    // ------------------
    // Constructor, ID, Layout
    // ------------------

    @Override
    public VH getViewHolder(View v) {
        return (VH) new NumberViewHolder(v, mGlobalSetting, mCompact);
    }

    @Override
    public void bindView(VH viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);
        AdapterSettingItemNumberBinding binding = (AdapterSettingItemNumberBinding) viewHolder.getBinding();
        NumberSetting sett = (NumberSetting) mData;
        binding.sbTop.setVisibility(((NumberSetting) mData).getMode().getSeekbarVisibility());
        binding.sbTop.setMax((sett.getMax() - sett.getMin()) / sett.getStepSize());
        binding.sbTop.setProgress((mData.getValue((CLASS) mCallback.getCustomSettingsObject(), mGlobalSetting) - ((NumberSetting<CLASS, SettData, VH>) mData).getMin())
                / ((NumberSetting<CLASS, SettData, VH>) mData).getStepSize());
    }

    protected void onSeekbarChanged(VH viewHolder, int progress, boolean topSwitch) {
        if (mGlobalSetting && !topSwitch) {
            return;
        }

        boolean global = mGlobalSetting || !topSwitch;
        int currentInt = mData.getValue((CLASS) mCallback.getCustomSettingsObject(), mGlobalSetting);
        int newInt = ((NumberSetting<CLASS, SettData, VH>) mData).getMin() + progress * ((NumberSetting<CLASS, SettData, VH>) mData).getStepSize();
        if (currentInt != newInt) {
            if (mData.setValue((CLASS) mCallback.getCustomSettingsObject(), global, newInt)) {
                mData.updateValueView(true, viewHolder, viewHolder.getValueTopView(), mData.getSettData(), global, mCallback);
                mData.onValueChanged(global ? mData.getDefaultId() : mData.getCustomId(), viewHolder.getActivity(), global, (CLASS) mCallback.getCustomSettingsObject());
            }
        }
    }

    // ------------------
    // ViewHolder
    // ------------------

    public static class NumberViewHolder<Integer, CLASS, SettData extends ISettData<Integer, CLASS, SettData, VH>, VH extends NumberViewHolder<Integer, CLASS, SettData, VH>> extends
            BaseSettingViewHolder<AdapterSettingItemNumberBinding, Integer, CLASS, SettData, VH> {
        public NumberViewHolder(View view, boolean globalSetting, boolean compact) {
            super(view, globalSetting, compact);
        }

        @Override
        public void onUpdateCustomViewDependencies(boolean globalSetting) {
            super.onUpdateCustomViewDependencies(globalSetting);
            if (globalSetting) {
            } else {
                boolean b = getUseCustomSwitch().isChecked();
                binding.sbTop.setEnabled(b);
            }
        }

        @Override
        public void onShowChangeSetting(VH vh, BaseSetting<Integer, CLASS, SettData, VH> data, Activity activity, ViewDataBinding binding,
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
            return binding.tvValueTop;
        }

        @Override
        public View getValueBottomView() {
            return binding.tvValueBottom;
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

    public static class SettingsSeekbarTopEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof NumberViewHolder) {
                return ((AdapterSettingItemNumberBinding) ((NumberViewHolder) viewHolder).getBinding()).sbTop;
            }
            return null;
        }

        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            view.post(() -> {
                SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            onEventOccurred(seekBar, viewHolder, fastAdapter);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                };
                ((SeekBar) view).setOnSeekBarChangeListener(listener);
            });
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            ((NumberSettingItem) item).onSeekbarChanged((NumberViewHolder) viewHolder, ((SeekBar) view).getProgress(), true);
        }
    }
}
