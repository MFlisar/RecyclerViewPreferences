package com.michaelflisar.recyclerviewpreferences.demo.classes;

import com.michaelflisar.recyclerviewpreferences.demo.custom.CustomSetting;

import java.util.HashMap;
import java.util.List;

/**
 * Created by flisar on 21.08.2017.
 */

public class DemoInMemoryStorage {

    private static HashMap<String, Object> mGlobalData = new HashMap<>();
    private static HashMap<Integer, DemoFolder> mObjectData = new HashMap<>();

    public static <T> T getData(String key, T defaultValue) {
        if (mGlobalData.containsKey(key)) {
            return (T) mGlobalData.get(key);
        }
        return defaultValue;
    }

    public static <T> void saveData(String key, Object data) {
        mGlobalData.put(key, data);
    }

    public static DemoFolder getAndRememberFolder(int index) {
        DemoFolder folder = mObjectData.get(index);
        if (folder == null) {
            folder = new DemoFolder(index);
            mObjectData.put(index, folder);
        }
        return folder;
    }

    // ----------------------------------
    // Helper functions for Preferences
    // ----------------------------------

    public static <Value> Value getPreferenceValue(Class<?> valueClass, String preferenceId, Value defaultValue) {
        if (valueClass.equals(Integer.class) || valueClass.equals(int.class)) {
            return (Value) (Integer) getData(preferenceId, (Integer) defaultValue);
        } else if (valueClass.equals(Boolean.class) || valueClass.equals(boolean.class)) {
            return (Value) (Boolean) getData(preferenceId, (Boolean) defaultValue);
        } else if (valueClass.equals(String.class)) {
            return (Value) getData(preferenceId, (String) defaultValue);
        } else if (valueClass.equals(List.class)) {
            return (Value) getData(preferenceId, (List<Integer>) defaultValue);
        } else if (valueClass.equals(CustomSetting.Data.class)) {
            return (Value) getData(preferenceId, (CustomSetting.Data) defaultValue);
        } else {
            throw new RuntimeException("Class " + valueClass.getName() + " not supported!");
        }
    }

    public static <Value> void setPreferenceValue(Class<?> valueClass, String preferenceId, Value value) {
        if (valueClass.equals(Integer.class) || valueClass.equals(int.class)) {
            saveData(preferenceId, value);
        } else if (valueClass.equals(Boolean.class) || valueClass.equals(boolean.class)) {
            saveData(preferenceId, value);
        } else if (valueClass.equals(String.class)) {
            saveData(preferenceId, value);
        } else if (valueClass.equals(List.class)) {
            saveData(preferenceId, value);
        } else if (valueClass.equals(CustomSetting.Data.class)) {
            saveData(preferenceId, value);
        } else {
            throw new RuntimeException("Class " + valueClass.getName() + " not supported!");
        }
    }
}
