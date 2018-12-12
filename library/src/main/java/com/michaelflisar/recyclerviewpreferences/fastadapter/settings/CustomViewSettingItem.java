package com.michaelflisar.recyclerviewpreferences.fastadapter.settings;

import android.view.View;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.fastadapter.viewholders.CustomViewHolder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;

/**
 * Created by Michael on 20.05.2017.
 */

public class CustomViewSettingItem<
        Parent extends IItem & IExpandable,
        Value,
        CLASS,
        SettData extends ISettData<Value, CLASS, SettData, VH>,
        VH extends CustomViewHolder<Value, CLASS, SettData, VH>>
        extends BaseSettingsItem<Parent, Value, CLASS, SettData, VH> {

    // ------------------
    // Factory
    // ------------------

    public CustomViewSettingItem(boolean globalSetting, boolean compact, BaseSetting<Value, CLASS, SettData, VH> data, ISettCallback callback, boolean flatStyle) {
        super(globalSetting, compact, data, callback, flatStyle);
    }

    // ------------------
    // Constructor, ID, Layout
    // ------------------

    @Override
    public VH getViewHolder(View v) {
        return (VH) new CustomViewHolder(v, mGlobalSetting, mCompact);
    }
}
