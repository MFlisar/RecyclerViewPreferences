package com.michaelflisar.recyclerviewpreferences.demo.custom;


import android.support.v7.widget.RecyclerView;

import com.michaelflisar.recyclerviewpreferences.classes.GlobalSetting;
import com.michaelflisar.recyclerviewpreferences.demo.classes.DemoInMemoryStorage;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CustomInMemoryOnlySettData<Value,
        SettData extends ISettData<Value, GlobalSetting, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Value, GlobalSetting, SettData, VH>>
        implements ISettData<Value, GlobalSetting, SettData, VH> {

    protected final String preferenceId;
    private final Class<?> mValueClass;
    protected Value defaultValue;

    protected CustomInMemoryOnlySettData(Class<Value> valueClass, String preferenceId) {
        this.preferenceId = preferenceId;
        mValueClass = valueClass;
        if (mValueClass.equals(Integer.class)) {
            withDefaultValue((Value) (Integer) 0);
        } else if (mValueClass.equals(Boolean.class)) {
            withDefaultValue((Value) (Boolean) false);
        } else if (mValueClass.equals(String.class)) {
            withDefaultValue((Value) "");
        } else if (mValueClass.equals(List.class)) {
            withDefaultValue((Value) new ArrayList<Integer>());
        } else if (mValueClass.equals(CustomDialogSetting.Data.class)) {
            withDefaultValue((Value) new CustomDialogSetting.Data());
        } else {
            throw new RuntimeException("Class " + mValueClass.getName() + " not supported!");
        }
    }

    public static <SettData extends ISettData<Integer, GlobalSetting, SettData, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Integer, GlobalSetting, SettData, VH>>
    CustomInMemoryOnlySettData
    createIntData(
            String preferenceId, int defaultValue) {
        return new CustomInMemoryOnlySettData(Integer.class, preferenceId).withDefaultValue(defaultValue);
    }

    public static <SettData extends ISettData<Boolean, GlobalSetting, SettData, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Boolean, GlobalSetting, SettData, VH>>
    CustomInMemoryOnlySettData<Boolean, SettData, VH> createBoolData(
            String preferenceId) {
        return new CustomInMemoryOnlySettData(Boolean.class, preferenceId);
    }

    public static <SettData extends ISettData<Integer, GlobalSetting, SettData, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Integer, GlobalSetting, SettData, VH>>
    CustomInMemoryOnlySettData
    createStringData(
            String preferenceId, String defaultValue) {
        return new CustomInMemoryOnlySettData(String.class, preferenceId).withDefaultValue(defaultValue);
    }

    public static <SettData extends ISettData<CustomDialogSetting.Data, GlobalSetting, SettData, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<CustomDialogSetting.Data, GlobalSetting, SettData, VH>>
    CustomInMemoryOnlySettData
    createCustomDataData(
            String preferenceId, CustomDialogSetting.Data defaultValue) {
        return new CustomInMemoryOnlySettData(CustomDialogSetting.Data.class, preferenceId).withDefaultValue(defaultValue);
    }

    public CustomInMemoryOnlySettData<Value, SettData, VH> withDefaultValue(Value value) {
        defaultValue = value;
        return this;
    }

    @Override
    public boolean getCustomEnabled(GlobalSetting object) {
        // this item only supports global values, so the custom value is never enabled
        return false;
    }

    @Override
    public void setCustomEnabled(GlobalSetting object, boolean enabled) {
        // can be ignored, because this item only supports global values
    }

    @Override
    public Class<GlobalSetting> getSettingsClass() {
        return GlobalSetting.class;
    }

    @Override
    public Value getValue(GlobalSetting object, boolean global) {
        // object can be ignored, we only support global values here
        return DemoInMemoryStorage.getPreferenceValue(mValueClass, preferenceId, defaultValue);
    }

    @Override
    public boolean setValue(GlobalSetting object, boolean global, Value value) {
        // object can be ignored, we only support global values here
        DemoInMemoryStorage.setPreferenceValue(mValueClass, preferenceId, value);
        return true;
    }
}
