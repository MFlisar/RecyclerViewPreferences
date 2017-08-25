package com.michaelflisar.recyclerviewpreferences.fastadapter.settings;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.databinding.AdapterSettingItemSwitchBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.BaseSettingViewHolder;
import com.michaelflisar.recyclerviewpreferences.fastadapter.hooks.BaseCustomEventHook;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.views.FixedSwitch;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;

/**
 * Created by Michael on 20.05.2017.
 */

public class SwitchSettingItem<Parent extends IItem & IExpandable, Boolean, CLASS, SettData extends ISettData<Boolean, CLASS, SettData, VH>, VH extends SwitchSettingItem.SwitchViewHolder<Boolean, CLASS, SettData, VH>> extends BaseSettingsItem<Parent, Boolean, CLASS, SettData, VH> {

    // ------------------
    // Factory
    // ------------------

    public SwitchSettingItem(boolean globalSetting, boolean compact, BaseSetting<Boolean, CLASS, SettData, VH> data, ISettCallback callback, boolean withBottomDivider) {
        super(globalSetting, compact, data, callback, withBottomDivider);
    }

    // ------------------
    // Constructor, ID, Layout
    // ------------------

    @Override
    public VH getViewHolder(View v) {
        VH vh = (VH) new SwitchViewHolder(v, mGlobalSetting, mCompact);
        // prevent EditText in Row2 from being editable
        vh.getValueBottomView().setOnTouchListener((view, motionEvent) -> true);
        return vh;
    }

    protected void onSwitchChanged(VH viewHolder, Boolean value, boolean topSwitch) {
        if (mGlobalSetting && !topSwitch) {
            return;
        }

        boolean global = mGlobalSetting || !topSwitch;
        if (!mData.getValue((CLASS)mCallback.getCustomSettingsObject(), global).equals(value)) {
            if (mData.setValue((CLASS)mCallback.getCustomSettingsObject(), global, value)) {
                mData.onValueChanged(global ? mData.getDefaultId() : mData.getCustomId(), viewHolder.getActivity(), global, (CLASS)mCallback.getCustomSettingsObject());
            }
        }
    }

    // ------------------
    // ViewHolder
    // ------------------

    public static class SwitchViewHolder<Data, CLASS, SettData extends ISettData<Data, CLASS, SettData, VH>, VH extends SwitchViewHolder<Data, CLASS, SettData, VH>> extends BaseSettingViewHolder<AdapterSettingItemSwitchBinding, Data, CLASS, SettData, VH> {
        public SwitchViewHolder(View view, boolean globalSetting, boolean compact) {
            super(view, globalSetting, compact);
        }

        @Override
        public void onUpdateCustomViewDependencies(boolean globalSetting) {
            super.onUpdateCustomViewDependencies(globalSetting);
            // make sure, that switch does not consume touches if disabled so that the click listener of row1 works
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
            return binding.swValueTop;
        }

        @Override
        public View getValueBottomView() {
            return binding.swValueBottom;
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

    public static class SettingsSwitchTopEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof SwitchViewHolder) {
                return ((AdapterSettingItemSwitchBinding) ((SwitchViewHolder) viewHolder).getBinding()).swValueTop;
            }
            return null;
        }

        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            ((FixedSwitch) view).setOnCheckedChangeListener((v, b) -> onEventOccurred(v, viewHolder, fastAdapter));
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            ((SwitchSettingItem) item).onSwitchChanged((SwitchViewHolder) viewHolder, ((FixedSwitch) view).isChecked(), true);
        }
    }

    public static class SettingsSwitchBottomEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof SwitchViewHolder) {
                return ((AdapterSettingItemSwitchBinding) ((SwitchViewHolder) viewHolder).getBinding()).swValueBottom;
            }
            return null;
        }

        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            ((FixedSwitch) view).setOnCheckedChangeListener((v, b) -> onEventOccurred(v, viewHolder, fastAdapter));
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            ((SwitchSettingItem) item).onSwitchChanged((SwitchViewHolder) viewHolder, ((FixedSwitch) view).isChecked(), false);
        }
    }
}
