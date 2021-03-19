package com.michaelflisar.recyclerviewpreferences.fastadapter.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewTextBottomBinding;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewTextTopBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.BaseSettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;

import androidx.databinding.ViewDataBinding;

public class TextViewHolder<
        Value,
        CLASS,
        SettData extends ISettData<Value, CLASS, SettData, VH>,
        VH extends TextViewHolder<Value, CLASS, SettData, VH>> extends
        BaseSettingsViewHolder<ViewTextTopBinding, ViewTextBottomBinding, Value, CLASS, SettData, VH> {

    public TextViewHolder(View view, boolean globalSetting, boolean compact) {
        super(view, globalSetting, compact, true);
    }

    @Override
    public int getTopLayoutResource() {
        return R.layout.view_text_top;
    }

    @Override
    public int getBottomLayoutResource() {
        return R.layout.view_text_bottom;
    }

    @Override
    public void onShowChangeSetting(VH vh, BaseSetting<Value, CLASS, SettData, VH> data, Activity activity,
                                    ViewDataBinding binding,
                                    SettData settData, boolean global, CLASS customSettingsObject) {
        data.onShowChangeSetting(vh, activity, binding, settData, global, customSettingsObject);
    }

    @Override
    public TextView getTitleTextView() {
        return ((ViewTextTopBinding) topBinding).tvTitle;
    }

    @Override
    public TextView getSubTitleTextView() {
        return ((ViewTextTopBinding) topBinding).tvSubTitle;
    }

    @Override
    public View getTopValueContainer() {
        return ((ViewTextTopBinding) topBinding).llCustomValueContainer;
    }

    @Override
    public TextView getIsUsingGlobalTextView() {
        return ((ViewTextBottomBinding) bottomBinding).tvIsUsingDefault;
    }

    @Override
    public View getValueTopView() {
        return ((ViewTextTopBinding) topBinding).tvValueTop;
    }

    @Override
    public View getValueBottomView() {
        return ((ViewTextBottomBinding) bottomBinding).tvValueBottom;
    }

    @Override
    public View getRow2() {
        return ((ViewTextBottomBinding) bottomBinding).llRow2;
    }
}
