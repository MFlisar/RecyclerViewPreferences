package com.michaelflisar.recyclerviewpreferences.demo.custom;


import com.michaelflisar.recyclerviewpreferences.demo.classes.DemoFolder;
import com.michaelflisar.recyclerviewpreferences.demo.classes.DemoInMemoryStorage;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class FolderSettData<
        Value,
        SettData extends ISettData<Value, DemoFolder, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Value, DemoFolder, SettData, VH>>
        implements ISettData<Value, DemoFolder, SettData, VH> {

    protected final String preferenceId;
    private final Class<?> mValueClass;
    protected Value defaultValue;

    protected FolderSettData(Class<Value> valueClass, String preferenceId) {
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
        } else {
            throw new RuntimeException("Class " + mValueClass.getName() + " not supported!");
        }
    }

    public static <SettData extends ISettData<Integer, DemoFolder, SettData, VH>,
            VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Integer, DemoFolder, SettData, VH>>
    FolderSettData<Integer, SettData, VH> createIntData(
            String preferenceId, int defaultValue) {
        return new FolderSettData(Integer.class, preferenceId).withDefaultValue(defaultValue);
    }

    public static <SettData extends ISettData<Boolean, DemoFolder, SettData, VH>,
            VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Boolean, DemoFolder, SettData, VH>>
    FolderSettData<Boolean, SettData, VH> createBoolData(
            String preferenceId) {
        return new FolderSettData(Boolean.class, preferenceId);
    }

    public static <SettData extends ISettData<Boolean, DemoFolder, SettData, VH>,
            VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Boolean, DemoFolder, SettData, VH>>
    FolderSettData<Boolean, SettData, VH> createStringData(
            String preferenceId, String defaultValue) {
        return new FolderSettData(String.class, preferenceId).withDefaultValue(defaultValue);
    }

    public FolderSettData<Value, SettData, VH> withDefaultValue(Value value) {
        defaultValue = value;
        return this;
    }

    @Override
    public boolean getCustomEnabled(DemoFolder object) {
        return object.isCustomEnabled(preferenceId);
    }

    @Override
    public void setCustomEnabled(DemoFolder object, boolean enabled) {
        object.setCustomEnabled(preferenceId, enabled);
    }

    @Override
    public Class<DemoFolder> getSettingsClass() {
        return DemoFolder.class;
    }

    @Override
    public Value getValue(DemoFolder object, boolean global) {
        if (global) {
            return DemoInMemoryStorage.getPreferenceValue(mValueClass, preferenceId, defaultValue);
        } else {
            return object.getData(preferenceId, defaultValue);
        }
    }

    @Override
    public boolean setValue(DemoFolder object, boolean global, Value value) {
        if (global) {
            DemoInMemoryStorage.setPreferenceValue(mValueClass, preferenceId, value);
        } else {
            object.saveData(preferenceId, value);
        }
        return true;
    }
}
