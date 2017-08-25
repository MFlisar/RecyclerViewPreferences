package com.michaelflisar.recyclerviewpreferences.demo;

import android.app.Application;
import android.util.Log;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;

/**
 * Created by flisar on 21.08.2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 1) Initialise settings manager once
        SettingsManager.get().init(this, null, "prefs");
        // 2) Initialise setting objects
        SettingsDefinitions.initGlobalSettings();
        SettingsDefinitions.initCustomInMemorySettings();
        SettingsDefinitions.initGlobalAndCustomInMemorySettings();
        SettingsDefinitions.registerDialogHandlers();

        SettingsManager.get().addSettingChangedListener((activity, setting, settData, global, customSettingsObject) -> {
            String value = setting.getValue(customSettingsObject, global).toString();
            Log.d(SettingsDefinitions.class.getSimpleName(),
                    String.format("Setting changed: %s [Global: %b] - Value: %s", setting.getTitle().getText(), global, value));
        });
    }
}
