package com.michaelflisar.recyclerviewpreferences.interfaces;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.databinding.AdapterBaseSettingItemBinding;

import androidx.cardview.widget.CardView;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Michael on 20.05.2017.
 */

public interface ISettingsViewHolder<
        Value,
        CLASS,
        SettData extends ISettData<Value, CLASS, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Value, CLASS, SettData, VH>> {

    void onShowChangeSetting(VH vh, BaseSetting<Value, CLASS, SettData, VH> data, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject);
    void updateValues(BaseSetting<Value, CLASS, SettData, VH> data, boolean globalSetting, ISettCallback callback);
    void onUpdateCustomViewDependencies(boolean hasIcon, boolean globalSetting);
    AdapterBaseSettingItemBinding getBinding();
    <T extends ViewDataBinding> T getTopBinding();
    <T extends ViewDataBinding> T getBottomBinding();
    void updateCompactMode(boolean globalSetting, boolean compact);
    void updateIcon(BaseSetting<Value, CLASS, SettData, VH> data, boolean globalSetting);
    void updateState(boolean enabled, boolean visible);
    void bind(BaseSetting<Value, CLASS, SettData, VH> data, boolean globalSetting);
    void unbind(BaseSetting<Value, CLASS, SettData, VH> data);

    CardView getCardView();
    TextView getTitleTextView();
    TextView getSubTitleTextView();
    LinearLayout getTopValueContainer();
    TextView getIsUsingGlobalTextView();
//    ImageView getIconView();
    ImageView getInnerIconView();
    View getInfoButton();

    void hideCustomSwitches();
    View getUseCustomSwitch(boolean hasIcon);
    void setUseCustomSwitchChecked(boolean hasIcon, boolean checked);
    boolean getIsUseCustomSwitchChecked(boolean hasIcon);
    void setCustomSwitchListeners(ICustomSwitchListener listener);

    View getValueTopView();
    View getValueBottomView();

    View getInnerDivider();
    View getRow1();
    View getRow2();

    interface ICustomSwitchListener {
        void onSwitchChanged(View view, boolean checked);
    }
}
