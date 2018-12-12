package com.michaelflisar.recyclerviewpreferences.implementations;


import com.michaelflisar.recyclerviewpreferences.classes.GlobalSetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import hundredthirtythree.sessionmanager.SessionManager;

public class SharedPreferenceSettData<
        Value,
        SettData extends ISettData<Value, GlobalSetting, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Value, GlobalSetting, SettData, VH>>
        implements ISettData<Value, GlobalSetting, SettData, VH> {

    protected final String preferenceId;
    private final Class<?> mClass;
    protected Value defaultValue;
    private ISharedPreferenceStringSerialiser mSerialiser;

    private SharedPreferenceSettData(Class<?> clazz, String preferenceId) {
        this.preferenceId = preferenceId;
        mClass = clazz;
        if (mClass.equals(Integer.class)) {
            withDefaultValue((Value) (Integer) 0);
        } else if (mClass.equals(Boolean.class)) {
            withDefaultValue((Value) (Boolean) false);
        } else if (mClass.equals(String.class)) {
            withDefaultValue((Value) "");
        } else if (mClass.equals(List.class)) {
            withDefaultValue((Value) new ArrayList<Integer>());
        } else if (ISharedPreferenceStringSerialiser.class.isAssignableFrom(mClass)) {
            withDefaultValue(null);
        } else {
            throw new RuntimeException("Class " + mClass.getName() + " not supported!");
        }
    }

    public static <SettData extends ISettData<Integer, GlobalSetting, SettData, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<
            Integer, GlobalSetting, SettData, VH>>
    SharedPreferenceSettData createIntData(
            String preferenceId, int defaultValue) {
        return new SharedPreferenceSettData(Integer.class, preferenceId)
                .withDefaultValue(defaultValue);
    }

    public static <SettData extends ISettData<Boolean, GlobalSetting, SettData, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<
            Boolean, GlobalSetting, SettData, VH>>
    SharedPreferenceSettData<Boolean, SettData, VH> createBoolData(
            String preferenceId) {
        return new SharedPreferenceSettData(Boolean.class, preferenceId);
    }

    public static <SettData extends ISettData<Boolean, GlobalSetting, SettData, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<
            Boolean, GlobalSetting, SettData, VH>>
    SharedPreferenceSettData<Boolean, SettData, VH> createBoolData(
            String preferenceId, boolean defaultValue) {
        return new SharedPreferenceSettData(Boolean.class, preferenceId)
                .withDefaultValue(defaultValue);
    }

    public static <SettData extends ISettData<String, GlobalSetting, SettData, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<
            String, GlobalSetting, SettData, VH>>
    SharedPreferenceSettData createStringData(
            String preferenceId, String defaultValue) {
        return new SharedPreferenceSettData(String.class, preferenceId)
                .withDefaultValue(defaultValue);
    }

    public static <SettData extends ISettData<String, GlobalSetting, SettData, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<
            String, GlobalSetting, SettData, VH>, T, S extends
            ISharedPreferenceStringSerialiser<T>> SharedPreferenceSettData createCustomData(
            String preferenceId, T defaultValue, S serialiser) {
        return new SharedPreferenceSettData(String.class, preferenceId)
                .withDefaultValue(serialiser.toString(defaultValue))
                .withSerialiser(serialiser);
    }

    public final String getPreferenceId() {
        return preferenceId;
    }

//    public static SharedPreferenceSettData<List<Integer>, ?, ?> createIntListData(String preferenceId) {
//        return new SharedPreferenceSettData(List.class, preferenceId);
//    }

    public SharedPreferenceSettData<Value, SettData, VH> withDefaultValue(Value value) {
        defaultValue = value;
        return this;
    }

    public <T, S extends ISharedPreferenceStringSerialiser<T>> SharedPreferenceSettData<Value, SettData, VH> withSerialiser(S serialiser) {
        mSerialiser = serialiser;
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
        if (mSerialiser != null) {
            return (Value) mSerialiser.fromString(SessionManager.getString(preferenceId, (String) defaultValue));
        } else if (mClass.equals(Integer.class)) {
            return (Value) (Integer) SessionManager.getInt(preferenceId, (Integer) defaultValue);
        } else if (mClass.equals(Boolean.class)) {
            return (Value) (Boolean) SessionManager.getBoolean(preferenceId, (Boolean) defaultValue);
        } else if (mClass.equals(String.class)) {
            return (Value) SessionManager.getString(preferenceId, (String) defaultValue);
        } else if (mClass.equals(List.class)) {
            return (Value) stringToList(SessionManager.getString(preferenceId, listToString((List<Integer>) defaultValue)));
        } else {
            throw new RuntimeException("Class " + mClass.getName() + " not supported!");
        }

    }

    @Override
    public boolean setValue(GlobalSetting object, boolean global, Value value) {
        if (mSerialiser != null) {
            SessionManager.putString(preferenceId, mSerialiser.toString(value));
        } else if (mClass.equals(Integer.class)) {
            SessionManager.putInt(preferenceId, (Integer) value);
        } else if (mClass.equals(Boolean.class)) {
            SessionManager.putBoolean(preferenceId, (Boolean) value);
        } else if (mClass.equals(String.class)) {
            SessionManager.putString(preferenceId, (String) value);
        } else if (mClass.equals(List.class)) {
            SessionManager.putString(preferenceId, listToString((List<Integer>) value));
        } else {
            throw new RuntimeException("Class " + mClass.getName() + " not supported!");
        }
        return true;
    }

    private String listToString(List<Integer> values) {
        if (values == null || values.size() == 0) {
            return "";
        }
        StringBuilder csvBuilder = new StringBuilder();
        for (Integer value : values) {
            csvBuilder.append(value);
            csvBuilder.append(";");
        }

        return csvBuilder.toString();
    }

    private List<Integer> stringToList(String value) {
        if (value == null || value.length() == 0) {
            return new ArrayList<>();
        }
        return Util.convertList(Arrays.asList(value.split(";")), v -> Integer.parseInt(v));
    }

    public interface ISharedPreferenceStringSerialiser<T> {
        String toString(T value);

        T fromString(String data);
    }
}
