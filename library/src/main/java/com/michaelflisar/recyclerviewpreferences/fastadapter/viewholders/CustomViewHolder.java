package com.michaelflisar.recyclerviewpreferences.fastadapter.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewCustomBottomBinding;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewCustomTopBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.BaseSettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;

import androidx.databinding.ViewDataBinding;

public class CustomViewHolder<
        Value,
        CLASS,
        SettData extends ISettData<Value, CLASS, SettData, VH>, VH extends CustomViewHolder<Value, CLASS, SettData, VH>>
        extends BaseSettingsViewHolder<ViewCustomTopBinding, ViewCustomBottomBinding, Value, CLASS, SettData, VH> {

    public CustomViewHolder(View view, boolean globalSetting, boolean compact) {
        super(view, globalSetting, compact, true);
    }

    @Override
    public int getTopLayoutResource() {
        return R.layout.view_custom_top;
    }

    @Override
    public int getBottomLayoutResource() {
        return R.layout.view_custom_bottom;
    }

    @Override
    public void onShowChangeSetting(VH vh, BaseSetting<Value, CLASS, SettData, VH> data, Activity activity, ViewDataBinding binding, SettData settData,
                                    boolean global, CLASS customSettingsObject) {
        data.onShowChangeSetting(vh, activity, binding, settData, global, customSettingsObject);
    }

    @Override
    public TextView getTitleTextView() {
        return ((ViewCustomTopBinding) topBinding).tvTitle;
    }

    @Override
    public TextView getSubTitleTextView() {
        return ((ViewCustomTopBinding) topBinding).tvSubTitle;
    }

    @Override
    public LinearLayout getTopValueContainer() {
        return ((ViewCustomTopBinding) topBinding).llCustomValueContainer;
    }

    @Override
    public TextView getIsUsingGlobalTextView() {
        return ((ViewCustomBottomBinding) bottomBinding).tvIsUsingDefault;
    }

    @Override
    public View getValueTopView() {
        return ((ViewCustomTopBinding) topBinding).vValueTop.getRoot();
    }

    @Override
    public View getValueBottomView() {
        return ((ViewCustomBottomBinding) bottomBinding).vValueBottom.getRoot();
    }

    @Override
    public View getRow2() {
        return ((ViewCustomBottomBinding) bottomBinding).llRow2;
    }
}
