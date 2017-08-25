package com.michaelflisar.recyclerviewpreferences.utils;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;

public class Definitions {

    // 4 IDs per setting: ID, ID for global value, ID for custom value, ID for enabling value
    public static final int IDS_PER_SETTING = 4;

    // tag id for dialogs under which the setting id of the caller of the dialog is saved
    public static final String DIALOG_SETTINGS_ID = SettingsManager.class.getName() + "|SettingsID";
    public static final String DIALOG_SETTING_IS_GLOBAL = SettingsManager.class.getName() + "|SettingsIsGlobal";

    public static final String PAYLOAD_UPDATE_HEADER_DECORATOR = "PAYLOAD_UPDATE_HEADER_DECORATOR";
}
