package com.michaelflisar.recyclerviewpreferences.fastadapter.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewSwitchBottomBinding;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewSwitchTopBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.BaseSettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;

import androidx.databinding.ViewDataBinding;

public class SwitchViewHolder<
        Data,
        CLASS,
        SettData extends ISettData<Data, CLASS, SettData, VH>,
        VH extends SwitchViewHolder<Data, CLASS, SettData, VH>> extends
        BaseSettingsViewHolder<ViewSwitchTopBinding, ViewSwitchBottomBinding, Data, CLASS, SettData, VH> {

    public SwitchViewHolder(View view, boolean globalSetting, boolean compact) {
        super(view, globalSetting, compact, true);
    }

    @Override
    public int getTopLayoutResource() {
        return R.layout.view_switch_top;
    }

    @Override
    public int getBottomLayoutResource() {
        return R.layout.view_switch_bottom;
    }

    @Override
    public void onUpdateCustomViewDependencies(boolean hasIcon, boolean globalSetting) {
        super.onUpdateCustomViewDependencies(hasIcon, globalSetting);
        // make sure, that switch does not consume touches if disabled so that the click listener of row1 works
        if (globalSetting) {
            getValueTopView().setClickable(true);
        } else {
            boolean b = getIsUseCustomSwitchChecked(hasIcon);
            getValueTopView().setClickable(b);
        }
    }

    @Override
    public void onShowChangeSetting(VH vh, BaseSetting<Data, CLASS, SettData, VH> data, Activity activity,
                                    ViewDataBinding binding,
                                    SettData settData, boolean global, CLASS customSettingsObject) {
        data.onShowChangeSetting(vh, activity, binding, settData, global, customSettingsObject);
    }

    @Override
    public TextView getTitleTextView() {
        return ((ViewSwitchTopBinding) topBinding).tvTitle;
    }

    @Override
    public TextView getSubTitleTextView() {
        return ((ViewSwitchTopBinding) topBinding).tvSubTitle;
    }

    @Override
    public LinearLayout getTopValueContainer() {
        return ((ViewSwitchTopBinding) topBinding).llCustomValueContainer;
    }

    @Override
    public TextView getIsUsingGlobalTextView() {
        return ((ViewSwitchBottomBinding) bottomBinding).tvIsUsingDefault;
    }

    @Override
    public View getValueTopView() {
        return ((ViewSwitchTopBinding) topBinding).swValueTop;
    }

    @Override
    public View getValueBottomView() {
        return ((ViewSwitchBottomBinding) bottomBinding).swValueBottom;
    }

    @Override
    public View getRow2() {
        return ((ViewSwitchBottomBinding) bottomBinding).llRow2;
    }
}
