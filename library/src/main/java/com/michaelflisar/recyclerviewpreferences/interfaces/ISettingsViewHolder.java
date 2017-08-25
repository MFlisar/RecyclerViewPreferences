package com.michaelflisar.recyclerviewpreferences.interfaces;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.views.FixedSwitch;

/**
 * Created by Michael on 20.05.2017.
 */

public interface ISettingsViewHolder<Value, CLASS, SettData extends ISettData<Value, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder &
        ISettingsViewHolder<Value, CLASS, SettData, VH>> {


    void onShowChangeSetting(VH vh, BaseSetting<Value, CLASS, SettData, VH> data, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject);
    void updateValues(BaseSetting<Value, CLASS, SettData, VH> data, boolean globalSetting, ISettCallback callback);
    void onUpdateCustomViewDependencies(boolean globalSetting);
    ViewDataBinding getBinding();
    void updateCompactMode(boolean globalSetting, boolean compact);
    void updateIcon(BaseSetting<Value, CLASS, SettData, VH> data, boolean globalSetting);
    void updateState(boolean enabled, boolean visible);
    void bind(BaseSetting<Value, CLASS, SettData, VH> data);
    void unbind(BaseSetting<Value, CLASS, SettData, VH> data);

    FixedSwitch getUseCustomSwitch();
    TextView getTitleTextView();
    TextView getSubTitleTextView();
    LinearLayout getTopValueContainer();
    TextView getIsUsingGlobalTextView();
    ImageView getIconView();
    View getInfoButton();

    View getValueTopView();
    View getValueBottomView();

    View getInnerDivider();
    View getRow1();
    View getRow2();
}
