package com.michaelflisar.recyclerviewpreferences.defaults;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.michaelflisar.recyclerviewpreferences.SettingsFragment;
import com.michaelflisar.recyclerviewpreferences.interfaces.IDialogHandler;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.utils.DialogUtil;

/**
 * Created by flisar on 29.08.2017.
 */

public class DefaultCustomDialogHandler<Data> implements IDialogHandler<Data> {

    @Override
    public Class<Data> getHandledClass() {
        return null; // not used, this one will handle every event if no custom handler is available
    }

    @Override
    public <CLASS,
            SD extends ISettData<Data, CLASS, SD, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Data, CLASS, SD, VH>>
    boolean handleCustomEvent(SettingsFragment settingsFragment, ISetting setting, int id, Activity activity, Data value, boolean global, CLASS customSettingsObject) {
        return DialogUtil.handleCustomEvent(settingsFragment, setting, id, activity, value, global, customSettingsObject);
    }
}