package com.michaelflisar.recyclerviewpreferences.interfaces;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

public interface ISettData<
        Value, CLASS,
        SettData extends ISettData<Value, CLASS, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Value, CLASS, SettData, VH>> {

    Class<CLASS> getSettingsClass();
    Value getValue(CLASS object, boolean global);
    boolean setValue(CLASS object, boolean global, Value value);
    boolean getCustomEnabled(CLASS object);
    void setCustomEnabled(CLASS object, boolean enabled);

    interface IValueChangedListener<CLASS> {
        void onValueChanged(int id, Activity activity, boolean global, CLASS customSettingsObject);
    }
}
