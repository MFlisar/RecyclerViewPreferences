package com.michaelflisar.recyclerviewpreferences.simpledemo;

import android.app.Application;
import android.graphics.Color;
import android.util.Log;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.SettingsGroup;
import com.michaelflisar.recyclerviewpreferences.classes.GlobalSetting;
import com.michaelflisar.recyclerviewpreferences.defaults.Setup;
import com.michaelflisar.recyclerviewpreferences.implementations.SharedPreferenceSettData;
import com.michaelflisar.recyclerviewpreferences.settings.BooleanSetting;
import com.michaelflisar.recyclerviewpreferences.settings.ColorSetting;
import com.michaelflisar.recyclerviewpreferences.settings.NumberSetting;
import com.michaelflisar.recyclerviewpreferences.settings.SpinnerSetting;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import java.util.ArrayList;

/**
 * Created by flisar on 21.08.2017.
 */

public class App extends Application {

    public static final ArrayList<Integer> EXAMPLE_SETTING_GROUPS = new ArrayList<>();
    public static Setup SETUP = new Setup();

    @Override
    public void onCreate() {
        super.onCreate();

        // 1) Initialise settings manager once
        SettingsManager.get().init(this, SETUP, "prefs");

        // 2) Initialise setting objects
        initSettings();

        // 3) Optional: listen to EVERY settings change
        SettingsManager.get().addSettingChangedListener((activity, setting, settData, global, customSettingsObject) -> {
            String value = setting.getValue(customSettingsObject, global).toString();
            Log.d("SIMPLE DEMO", String.format("Setting changed: %s [Global: %b] - Value: %s", setting.getTitle().getText(), global, value));
        });
    }

    private void initSettings() {

        // ---------------------------
        // Define groups and settings
        // ---------------------------

        SettingsGroup group1 = new SettingsGroup("Group 1")
                .add(new SettingsGroup(GoogleMaterial.Icon.gmd_android, "Sub group 1.1 - Icons")
                                .add(new NumberSetting(GlobalSetting.class, SharedPreferenceSettData.createIntData("pref1_1", 1), "Icon Size (1-10) - Dialog Mode", GoogleMaterial.Icon.gmd_image,
                                        NumberSetting.Mode.DialogSeekbar, 1, 10, 1,
                                        R.string.unit_dp))
                                .add(new NumberSetting(GlobalSetting.class, SharedPreferenceSettData.createIntData("pref1_2", 1), "Icon Padding (1-10) - Seekbar only mode",
                                        GoogleMaterial.Icon.gmd_border_all, NumberSetting.Mode.Seekbar, 1, 10,
                                        1,
                                        R.string.unit_dp))
                        .add(new SpinnerSetting(GlobalSetting.class, SharedPreferenceSettData.createIntData("pref1_3", IconStyle.Normal.getId()), "Icon Style",
                                GoogleMaterial.Icon.gmd_image,
                                new IconStyle.EnumHelper()))
                        .add(new SpinnerSetting(GlobalSetting.class, SharedPreferenceSettData.createIntData("pref1_4", IconStyleWithIcon.Normal.getId()), "Icon Style with icon",
                                GoogleMaterial.Icon.gmd_image,
                                new IconStyleWithIcon.EnumHelper()))
                )
                .add(new SettingsGroup(GoogleMaterial.Icon.gmd_android, "Sub group 1.2 - Sizes")
                        .add(new NumberSetting(GlobalSetting.class, SharedPreferenceSettData.createIntData("pref2_1", 6), "Rows (1-20) - Seekbar and dialog mode",
                                GoogleMaterial.Icon.gmd_warning, NumberSetting.Mode.SeekbarAndDialogInput, 1, 20, 1, null))
                        .add(new NumberSetting(GlobalSetting.class, SharedPreferenceSettData.createIntData("pref2_2", 4), "Cols (1-20) - Seekbar and dialog mode",
                                GoogleMaterial.Icon.gmd_settings, NumberSetting.Mode.SeekbarAndDialogInput, 1, 20, 1, null))
                )
                .add(new SettingsGroup(GoogleMaterial.Icon.gmd_android, "Sub group 1.3 - Colors")
                        .add(new BooleanSetting(GlobalSetting.class, SharedPreferenceSettData.createBoolData("pref3_1"), "Use custom color", GoogleMaterial.Icon.gmd_settings))
                        .add(new ColorSetting(GlobalSetting.class, SharedPreferenceSettData.createIntData("pref3_2", Color.BLACK), "Color", GoogleMaterial.Icon.gmd_colorize))
                );
        SettingsGroup group2 = new SettingsGroup("Group 2")
                .add(new SettingsGroup(GoogleMaterial.Icon.gmd_android, "Sub group 2.1 - Various")
                        .add(new NumberSetting(GlobalSetting.class, SharedPreferenceSettData.createIntData("pref4_1", 50), "Integer (0-100, steps 5)\nSeekbar and dialog mode",
                                GoogleMaterial.Icon.gmd_warning,
                                NumberSetting.Mode.SeekbarAndDialogInput, 0, 100, 5, null))
                        .add(new BooleanSetting(GlobalSetting.class, SharedPreferenceSettData.createBoolData("pref4_2"), "Switch", GoogleMaterial.Icon.gmd_settings) {
                        })
                        .add(new ColorSetting(GlobalSetting.class, SharedPreferenceSettData.createIntData("pref4_3", Color.RED), "Color", GoogleMaterial.Icon.gmd_settings))
                );

        // ---------------------------
        // 2) add all groups to the settings manager + add an optional listener
        // ---------------------------

        SettingsManager.get().add(group1);
        SettingsManager.get().add(group2);

        // ---------------------------
        // 3) Remember group ids of this block
        // ---------------------------

        EXAMPLE_SETTING_GROUPS.add(group1.getGroupId());
        EXAMPLE_SETTING_GROUPS.add(group2.getGroupId());
    }
}
