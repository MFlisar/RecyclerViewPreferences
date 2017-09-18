package com.michaelflisar.recyclerviewpreferences.utils;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.michaelflisar.recyclerviewpreferences.SettingsFragment;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;

/**
 * Created by flisar on 29.08.2017.
 */

public class DialogUtil {

    public static <Data,
            CLASS,
            SD extends ISettData<Data, CLASS, SD, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Data, CLASS, SD, VH>>
    boolean handleCustomEvent(SettingsFragment settingsFragment, ISetting setting, int id, Activity activity,
            Data value,
            boolean global, CLASS customSettingsObject) {

        // gracyfully try to handle the event
        try {
            if (!setting.getValue(customSettingsObject, global).equals(value)) {
                setting.setValue(customSettingsObject, global, value);
                setting.onValueChanged(id, activity, global, customSettingsObject);
                settingsFragment.updateViews(id);
                return true;
            }
        } catch (ClassCastException e) {
            Log.e(DialogUtil.class.getSimpleName(), "Custom dialog event skipped because it could not be handled by the default dialog handler!", e);
        }
        return false;
    }
}
