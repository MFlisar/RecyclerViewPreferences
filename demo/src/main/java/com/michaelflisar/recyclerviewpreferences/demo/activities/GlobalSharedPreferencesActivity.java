package com.michaelflisar.recyclerviewpreferences.demo.activities;

import android.os.Bundle;

import com.michaelflisar.recyclerviewpreferences.demo.SettingsDefinitions;
import com.michaelflisar.recyclerviewpreferences.demo.activities.base.BasePreferenceActivity;

import java.util.List;

public class GlobalSharedPreferencesActivity extends BasePreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // we show global settings only in here
        mGlobalSettings = true;
    }

    @Override
    protected List<Integer> getPrefGroupIds() {
        return SettingsDefinitions.GLOBAL_GROUP_IDS;
    }

    @Override
    public Object getCustomSettingsObject() {
        // we show global settings only in here, so this is irrelevant
        return null;
    }
}
