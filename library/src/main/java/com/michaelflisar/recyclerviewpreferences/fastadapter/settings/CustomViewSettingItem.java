package com.michaelflisar.recyclerviewpreferences.fastadapter.settings;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.databinding.AdapterSettingItemCustomViewBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.BaseSettingViewHolder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.views.FixedSwitch;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;

/**
 * Created by Michael on 20.05.2017.
 */

public class CustomViewSettingItem<Parent extends IItem & IExpandable, Value, CLASS, SettData extends ISettData<Value, CLASS, SettData, VH>, VH extends CustomViewSettingItem.TextViewHolder<Value, CLASS, SettData, VH>> extends BaseSettingsItem<Parent, Value, CLASS, SettData, VH> {

    // ------------------
    // Factory
    // ------------------

    public CustomViewSettingItem(boolean globalSetting, boolean compact, BaseSetting<Value, CLASS, SettData, VH> data, ISettCallback callback, boolean withBottomDivider) {
        super(globalSetting, compact, data, callback, withBottomDivider);
    }

    // ------------------
    // Constructor, ID, Layout
    // ------------------

    @Override
    public VH getViewHolder(View v) {
        return (VH) new TextViewHolder(v, mGlobalSetting, mCompact);
    }

    // ------------------
    // ViewHolder
    // ------------------

    public static class TextViewHolder<Value, CLASS, SettData extends ISettData<Value, CLASS, SettData, VH>, VH extends CustomViewSettingItem.TextViewHolder<Value, CLASS, SettData, VH>> extends BaseSettingViewHolder<AdapterSettingItemCustomViewBinding, Value, CLASS, SettData, VH> {
        public TextViewHolder(View view, boolean globalSetting, boolean compact) {
            super(view, globalSetting, compact);
        }

        @Override
        public void onShowChangeSetting(VH vh, BaseSetting<Value, CLASS, SettData, VH> data, Activity activity, ViewDataBinding binding,
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
            return binding.vValueTop.getRoot();
        }

        @Override
        public View getValueBottomView() {
            return binding.vValueBottom.getRoot();
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
}
