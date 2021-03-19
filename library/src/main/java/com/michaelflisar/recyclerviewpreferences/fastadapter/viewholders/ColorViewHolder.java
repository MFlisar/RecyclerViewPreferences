package com.michaelflisar.recyclerviewpreferences.fastadapter.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewColorBottomBinding;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewColorTopBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.BaseSettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class ColorViewHolder<
        Value,
        CLASS,
        SettData extends ISettData<Value, CLASS, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Value, CLASS, SettData, VH>>
        extends BaseSettingsViewHolder<ViewColorTopBinding, ViewColorBottomBinding, Value, CLASS, SettData, VH> {

    public ColorViewHolder(View view, boolean globalSetting, boolean compact) {
        super(view, globalSetting, compact, true);
    }

    @Override
    public int getTopLayoutResource() {
        return R.layout.view_color_top;
    }

    @Override
    public int getBottomLayoutResource() {
        return R.layout.view_color_bottom;
    }

    @Override
    public void onUpdateCustomViewDependencies(boolean hasIcon, boolean globalSetting) {
        super.onUpdateCustomViewDependencies(hasIcon, globalSetting);
        if (globalSetting) {
        } else {
            boolean b = getIsUseCustomSwitchChecked(hasIcon);
            getValueTopView().setAlpha(b ? 1f : 0.5f);
            getValueBottomView().setAlpha(!b ? 1f : 0.5f);
        }
    }

    @Override
    public void onShowChangeSetting(VH vh, BaseSetting<Value, CLASS, SettData, VH> data, Activity activity,
                                    ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject) {
        data.onShowChangeSetting(vh, activity, binding, settData, global, customSettingsObject);
    }

    @Override
    public TextView getTitleTextView() {
        return ((ViewColorTopBinding) topBinding).tvTitle;
    }

    @Override
    public TextView getSubTitleTextView() {
        return ((ViewColorTopBinding) topBinding).tvSubTitle;
    }

    @Override
    public View getTopValueContainer() {
        return ((ViewColorTopBinding) topBinding).llCustomValueContainer;
    }

    @Override
    public TextView getIsUsingGlobalTextView() {
        return ((ViewColorBottomBinding) bottomBinding).tvIsUsingDefault;
    }

    @Override
    public View getValueTopView() {
        return ((ViewColorTopBinding) topBinding).vValueTop;
    }

    @Override
    public View getValueBottomView() {
        return ((ViewColorBottomBinding) bottomBinding).vValueBottom;
    }

    @Override
    public View getRow2() {
        return ((ViewColorBottomBinding) bottomBinding).llRow2;
    }
}
