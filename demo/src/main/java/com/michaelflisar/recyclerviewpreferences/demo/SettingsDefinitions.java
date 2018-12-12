package com.michaelflisar.recyclerviewpreferences.demo;

import android.graphics.Color;
import android.widget.Spinner;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.SettingsGroup;
import com.michaelflisar.recyclerviewpreferences.classes.Dependency;
import com.michaelflisar.recyclerviewpreferences.classes.GlobalSetting;
import com.michaelflisar.recyclerviewpreferences.demo.classes.DemoFolder;
import com.michaelflisar.recyclerviewpreferences.demo.classes.IconStyle;
import com.michaelflisar.recyclerviewpreferences.demo.classes.IconStyleWithIcon;
import com.michaelflisar.recyclerviewpreferences.demo.custom.CustomDialogSetting;
import com.michaelflisar.recyclerviewpreferences.demo.custom.CustomInMemoryOnlySettData;
import com.michaelflisar.recyclerviewpreferences.demo.custom.FolderSettData;
import com.michaelflisar.recyclerviewpreferences.demo.custom.CustomImageSetting;
import com.michaelflisar.recyclerviewpreferences.implementations.SharedPreferenceSettData;
import com.michaelflisar.recyclerviewpreferences.settings.BooleanSetting;
import com.michaelflisar.recyclerviewpreferences.settings.ColorSetting;
import com.michaelflisar.recyclerviewpreferences.settings.NumberSetting;
import com.michaelflisar.recyclerviewpreferences.settings.SpinnerSetting;
import com.michaelflisar.recyclerviewpreferences.settings.TextDialogSetting;
import com.michaelflisar.recyclerviewpreferences.settings.TextSetting;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class SettingsDefinitions {

    public static final ArrayList<Integer> GLOBAL_GROUP_IDS = new ArrayList<>();
    public static final ArrayList<Integer> CUSTOM_IN_MEMORY_GROUPS = new ArrayList<>();
    public static final ArrayList<Integer> GLOBAL_AND_CUSTOM_IN_MEMORY_GROUPS = new ArrayList<>();

    // Hacky solution to use a image picker library without adjustments for the sake of easiness
    public static final AtomicInteger SETT_ID_IMAGE_PICKER = new AtomicInteger(-1);

    public static void initGlobalSettings() {

        // ---------------------------
        // Define groups and settings
        // ---------------------------

        // Optionally we can define identifiers that can be used to get a setting's object from the SettingsManager
        final AtomicInteger SETT_ENABLE_NEXT_SETTING = new AtomicInteger(-1);
        final AtomicInteger SETT_NEXT_SETTING = new AtomicInteger(-1);
        final AtomicInteger SETT_ENABLE_COLOR = new AtomicInteger(-1);
        final AtomicInteger SETT_COLOR = new AtomicInteger(-1);

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
                        .add(new SpinnerSetting(GlobalSetting.class, SharedPreferenceSettData.createIntData("pref1_6", IconStyleWithIcon.Normal.getId()), "Icon Style with icon",
                                GoogleMaterial.Icon.gmd_image,
                                new IconStyleWithIcon.EnumHelper()))
                        .add(new SpinnerSetting(GlobalSetting.class, SharedPreferenceSettData.createIntData("pref1_3a", IconStyle.Normal.getId()), "Icon Style dialog",
                                GoogleMaterial.Icon.gmd_image,
                                new IconStyle.EnumHelper()).withSpinnerMode(Spinner.MODE_DIALOG))
                        .add(new BooleanSetting(GlobalSetting.class, SharedPreferenceSettData.createBoolData("pref1_4"), "Enable next setting", GoogleMaterial.Icon.gmd_settings)
                                .withIdCallback(id -> SETT_ENABLE_NEXT_SETTING.set(id)))
                        .add(new BooleanSetting(GlobalSetting.class, SharedPreferenceSettData.createBoolData("pref1_5"), "Next setting", GoogleMaterial.Icon.gmd_settings)
                                .withIdCallback(id -> SETT_NEXT_SETTING.set(id)))
                        .add(new CustomImageSetting.Setting(GlobalSetting.class, SharedPreferenceSettData.createCustomData("pref1_7", new CustomImageSetting.Data(), new CustomImageSetting.Serialiser()),
                                "Custom image picker", GoogleMaterial.Icon.gmd_image).withIdCallback(id -> SETT_ID_IMAGE_PICKER.set(id)))
                )
                .add(new SettingsGroup(GoogleMaterial.Icon.gmd_android, "Sub group 1.2 - Sizes")
                        .add(new NumberSetting(GlobalSetting.class, SharedPreferenceSettData.createIntData("pref2_1", 6), "Rows (1-20) - Seekbar and dialog mode",
                                GoogleMaterial.Icon.gmd_warning, NumberSetting.Mode.SeekbarAndDialogInput, 1, 20, 1, null))
                        .add(new NumberSetting(GlobalSetting.class, SharedPreferenceSettData.createIntData("pref2_2", 4), "Cols (1-20) - Seekbar and dialog mode",
                                GoogleMaterial.Icon.gmd_settings, NumberSetting.Mode.SeekbarAndDialogInput, 1, 20, 1, null))
                )
                .add(new SettingsGroup(GoogleMaterial.Icon.gmd_android, "Sub group 1.3 - Colors")
                        .add(new BooleanSetting(GlobalSetting.class, SharedPreferenceSettData.createBoolData("pref3_1"), "Use custom color", GoogleMaterial.Icon.gmd_settings)
                                .withIdCallback(id -> SETT_ENABLE_COLOR.set(id)))
                        .add(new ColorSetting(GlobalSetting.class, SharedPreferenceSettData.createIntData("pref3_2", Color.BLACK), "Color", GoogleMaterial.Icon.gmd_colorize)
                                .withIdCallback(id -> SETT_COLOR.set(id)))
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
        // add all groups to the settings manager + add an optional listener
        // ---------------------------

        SettingsManager.get().add(group1);
        SettingsManager.get().add(group2);

        // ---------------------------
        // now settings do have ids, we can use SettingsManager.get().find(id) now and add dependencies easily like this
        // ---------------------------

        SettingsManager.get().find(SETT_NEXT_SETTING.get()).addDependency(Dependency.getBooleanDependency(SettingsManager.get().find(SETT_ENABLE_NEXT_SETTING.get()), Dependency.Type.Disable));
        SettingsManager.get().find(SETT_COLOR.get()).addDependency(Dependency.getBooleanDependency(SettingsManager.get().find(SETT_ENABLE_COLOR.get()), Dependency.Type.Hide));

        // ---------------------------
        // Remember group ids of this block
        // ---------------------------

        GLOBAL_GROUP_IDS.add(group1.getGroupId());
        GLOBAL_GROUP_IDS.add(group2.getGroupId());
    }

    public static void initCustomInMemorySettings() {

        // ---------------------------
        // Define groups and settings
        // ---------------------------


        SettingsGroup group1 = new SettingsGroup("Group 1")
                .add(new SettingsGroup(GoogleMaterial.Icon.gmd_android, "Folder Icons")
                        .add(new NumberSetting(GlobalSetting.class, CustomInMemoryOnlySettData.createIntData("pref1_1", 1), "Icon Size (1-10) - Dialog Mode", GoogleMaterial.Icon.gmd_image,
                                NumberSetting.Mode.DialogSeekbar, 1, 10, 1,
                                R.string.unit_dp))
                        .add(new NumberSetting(GlobalSetting.class, CustomInMemoryOnlySettData.createIntData("pref1_2", 1), "Icon Padding (1-10) - Seekbar only mode",
                                GoogleMaterial.Icon.gmd_border_all, NumberSetting.Mode.Seekbar, 1, 10,
                                1,
                                R.string.unit_dp))
                        .add(new SpinnerSetting(GlobalSetting.class, CustomInMemoryOnlySettData.createIntData("pref1_3", IconStyle.Normal.getId()), "Icon Style",
                                GoogleMaterial.Icon.gmd_image,
                                new IconStyle.EnumHelper()))
                        .add(new SpinnerSetting(GlobalSetting.class, CustomInMemoryOnlySettData.createIntData("pref1_6", IconStyleWithIcon.Normal.getId()), "Icon Style with icon",
                                GoogleMaterial.Icon.gmd_image,
                                new IconStyleWithIcon.EnumHelper()))
                        .add(new CustomDialogSetting.Setting(CustomDialogSetting.Data.class, CustomInMemoryOnlySettData.createCustomDataData("pref1_4", new CustomDialogSetting.Data(4, 6)),
                                "Grid size dialog", GoogleMaterial.Icon.gmd_grid_on))
                );

        // ---------------------------
        // add all groups to the settings manager + add an optional listener
        // ---------------------------

        SettingsManager.get().add(group1);

        CUSTOM_IN_MEMORY_GROUPS.add(group1.getGroupId());
    }

    public static void initGlobalAndCustomInMemorySettings() {

        SettingsGroup group1 = new SettingsGroup("Group 1")
                .add(new SettingsGroup(GoogleMaterial.Icon.gmd_folder, "Folder")
                        .add(new NumberSetting(DemoFolder.class, FolderSettData.createIntData("pref2_1", 48), "Size", GoogleMaterial.Icon.gmd_image, NumberSetting.Mode.Seekbar, 0, 100, 2,
                                R.string.unit_dp))
                        .add(new NumberSetting(DemoFolder.class, FolderSettData.createIntData("pref2_2", 0), "Padding", GoogleMaterial.Icon.gmd_border_all, NumberSetting.Mode.Seekbar, 0, 64,
                                1, R.string.unit_dp))
                        .add(new SpinnerSetting(DemoFolder.class, FolderSettData.createIntData("pref2_3", IconStyle.Normal.getId()), "Style", GoogleMaterial.Icon.gmd_image,
                                new IconStyle.EnumHelper()))
                        .add(new SpinnerSetting(DemoFolder.class, FolderSettData.createIntData("pref2_3a", IconStyle.Normal.getId()), "Style - no icon", null,
                                new IconStyle.EnumHelper()))
                        .add(new ColorSetting(DemoFolder.class, FolderSettData.createIntData("pref2_4", Color.RED), "Color", GoogleMaterial.Icon.gmd_colorize))
                        .add(new BooleanSetting(DemoFolder.class, FolderSettData.createBoolData("pref2_5"), "Show label", GoogleMaterial.Icon.gmd_text_format))
                        .add(new TextSetting(DemoFolder.class, FolderSettData.createStringData("pref2_6", "Unknown folder"), "Label", GoogleMaterial.Icon.gmd_text_fields))
                        .add(new TextDialogSetting(DemoFolder.class, FolderSettData.createStringData("pref2_7", "Custom"), "Custom value", GoogleMaterial.Icon.gmd_text_fields))
                );

        // ---------------------------
        // add all groups to the settings manager + add an optional listener
        // ---------------------------

        SettingsManager.get().add(group1);

        GLOBAL_AND_CUSTOM_IN_MEMORY_GROUPS.add(group1.getGroupId());
    }

    public static void registerDialogHandlers() {
        SettingsManager.get().registerDialogHandler(new CustomDialogSetting.DialogHandler());
        SettingsManager.get().registerDialogHandler(new CustomImageSetting.DialogHandler());
    }
}
