package com.michaelflisar.recyclerviewpreferences.interfaces;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;

public interface ISettData<Value, CLASS, SettData extends ISettData<Value, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder &
        ISettingsViewHolder<Value, CLASS, SettData, VH>> {

    Class<CLASS> getSettingsClass();
    Value getValue(CLASS object, boolean global);
    boolean setValue(CLASS object, boolean global, Value value);
//    boolean supportsCustomValue();
    boolean getCustomEnabled(CLASS object);
    void setCustomEnabled(CLASS object, boolean enabled);

    interface IValueChangedListener<CLASS> {
        void onValueChanged(int id, Activity activity, boolean global, CLASS customSettingsObject);
    }

//    interface IGlobalSettData<Value, CLASS, BaseSettData extends ISettData<Value, CLASS, BaseSettData, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Value, CLASS, BaseSettData, VH>> extends ISettData<Value, CLASS, BaseSettData, VH> {
//
//
//    }
}
