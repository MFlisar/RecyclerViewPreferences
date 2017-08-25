package com.michaelflisar.recyclerviewpreferences.demo.classes;

import java.util.HashMap;

/**
 * Created by flisar on 22.08.2017.
 */

public class DemoFolder {

    private HashMap<String, Boolean> mEnabledData = new HashMap<>();
    private HashMap<String, Object> mData = new HashMap<>();
    private int mIndex;

    public DemoFolder(int index) {
        mIndex = index;
    }

    public int getIndex() {
        return mIndex;
    }

    public <T> T getData(String key, T defaultValue) {
        if (mData.containsKey(key)) {
            return (T) mData.get(key);
        }
        return defaultValue;
    }

    public void saveData(String key, Object data) {
        mData.put(key, data);
    }

    public boolean isCustomEnabled(String key) {
        if (mEnabledData.containsKey(key)) {
            return mEnabledData.get(key);
        }
        mEnabledData.put(key, false);
        return false;
    }

    public void setCustomEnabled(String key, boolean customEnabled) {
        mEnabledData.put(key, customEnabled);
    }
}
