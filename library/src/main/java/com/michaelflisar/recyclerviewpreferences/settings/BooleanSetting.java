package com.michaelflisar.recyclerviewpreferences.settings;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.base.SettingsText;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.SwitchSettingItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.views.FixedSwitch;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.iconics.typeface.IIcon;

/**
 * Created by Michael on 16.05.2017.
 */

public class BooleanSetting<CLASS, SettData extends ISettData<Boolean, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder &
        ISettingsViewHolder<Boolean, CLASS, SettData, VH>> extends BaseSetting<Boolean, CLASS, SettData, VH> {

    public BooleanSetting(Class<CLASS> clazz, SettData settData, int title, IIcon icon) {
        this(clazz, settData, new SettingsText(title), icon);
    }

    public BooleanSetting(Class<CLASS> clazz, SettData settData, String title, IIcon icon) {
        this(clazz, settData, new SettingsText(title), icon);
    }

    private BooleanSetting(Class<CLASS> clazz, SettData settData, SettingsText title, IIcon icon) {
        super(clazz, settData, title, icon);
    }

    @Override
    public void updateValueView(boolean topView, VH vh, View v, SettData settData, boolean global, CLASS customSettingsObject) {
        ((FixedSwitch) v).setChecked(getValue(customSettingsObject, global));
    }

    @Override
    public void bind(VH vh) {
    }

    @Override
    public void unbind(VH vh) {
    }

    @Override
    public void onShowChangeSetting(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject) {
        // Switch works with EventHook
        // but we toggle the value here as well => this triggers the event
        ((FixedSwitch) vh.getValueTopView()).setChecked(!getValue(customSettingsObject, global));
    }

    @Override
    public final int getLayoutTypeId() {
        return R.id.id_adapter_setting_boolean_item;
    }

    @Override
    public final int getLayout() {
        return R.layout.adapter_setting_item_switch;
    }

    @Override
    public <P extends IItem & IExpandable> BaseSettingsItem<P, Boolean, CLASS, SettData, ?> createItem(boolean global, boolean compact, ISettCallback settingsCallback,
            boolean withBottomDivider) {
        return new SwitchSettingItem(global, compact, this, settingsCallback, withBottomDivider);
    }
}
