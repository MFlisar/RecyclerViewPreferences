package com.michaelflisar.recyclerviewpreferences.interfaces;

/**
 * Created by Michael on 19.05.2017.
 */

public interface ISettingsItem {

    ISetting getSettings();
    void setCompactMode(boolean enabled);
    void setDependencyState(boolean enabled, boolean visible);
    void checkDependency(boolean global, Object customSettingsObject);
}
