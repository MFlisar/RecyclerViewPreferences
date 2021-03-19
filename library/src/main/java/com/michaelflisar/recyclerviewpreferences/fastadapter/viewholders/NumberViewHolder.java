package com.michaelflisar.recyclerviewpreferences.fastadapter.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewNumberBottomBinding;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewNumberTopBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.BaseSettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;

import androidx.databinding.ViewDataBinding;

public class NumberViewHolder<
        Integer,
        CLASS,
        SettData extends ISettData<Integer, CLASS, SettData, VH>,
        VH extends NumberViewHolder<Integer, CLASS, SettData, VH>>
        extends BaseSettingsViewHolder<ViewNumberTopBinding, ViewNumberBottomBinding, Integer, CLASS, SettData, VH> {

    public NumberViewHolder(View view, boolean globalSetting, boolean compact) {
        super(view, globalSetting, compact, true);
    }

    @Override
    public int getTopLayoutResource() {
        return R.layout.view_number_top;
    }

    @Override
    public int getBottomLayoutResource() {
        return R.layout.view_number_bottom;
    }

    @Override
    public void onUpdateCustomViewDependencies(boolean hasIcon, boolean globalSetting) {
        super.onUpdateCustomViewDependencies(hasIcon, globalSetting);
        if (globalSetting) {
        } else {
            boolean b = getIsUseCustomSwitchChecked(hasIcon);
            ((ViewNumberTopBinding) topBinding).sbTop.setEnabled(b);
        }
    }

    @Override
    public void onShowChangeSetting(VH vh, BaseSetting<Integer, CLASS, SettData, VH> data, Activity activity, ViewDataBinding binding,
                                    SettData settData, boolean global, CLASS customSettingsObject) {
        data.onShowChangeSetting(vh, activity, binding, settData, global, customSettingsObject);
    }

    @Override
    public TextView getTitleTextView() {
        return ((ViewNumberTopBinding) topBinding).tvTitle;
    }

    @Override
    public TextView getSubTitleTextView() {
        return ((ViewNumberTopBinding) topBinding).tvSubTitle;
    }

    @Override
    public View getTopValueContainer() {
        return ((ViewNumberTopBinding) topBinding).llCustomValueContainer;
    }

    @Override
    public TextView getIsUsingGlobalTextView() {
        return ((ViewNumberBottomBinding) bottomBinding).tvIsUsingDefault;
    }

    @Override
    public View getValueTopView() {
        return ((ViewNumberTopBinding) topBinding).tvValueTop;
    }

    @Override
    public View getValueBottomView() {
        return ((ViewNumberBottomBinding) bottomBinding).tvValueBottom;
    }

    @Override
    public View getRow2() {
        return ((ViewNumberBottomBinding) bottomBinding).llRow2;
    }
}
