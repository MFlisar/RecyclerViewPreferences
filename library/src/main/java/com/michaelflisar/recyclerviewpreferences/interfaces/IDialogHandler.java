package com.michaelflisar.recyclerviewpreferences.interfaces;

import android.app.Activity;

import com.michaelflisar.recyclerviewpreferences.SettingsFragment;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by flisar on 25.08.2017.
 */

public interface IDialogHandler<Value> {

    Class<Value> getHandledClass();

    <CLASS,
            SD extends ISettData<Value, CLASS, SD, VH>,
            VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Value, CLASS, SD, VH>>
    boolean handleCustomEvent(SettingsFragment settingsFragment, ISetting setting, final int id, Activity activity, Value value, boolean global, CLASS customSettingsObject);

}
