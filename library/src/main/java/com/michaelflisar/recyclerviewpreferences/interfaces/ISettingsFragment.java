package com.michaelflisar.recyclerviewpreferences.interfaces;


import com.michaelflisar.recyclerviewpreferences.classes.SettingsFragmentManager;

public interface ISettingsFragment {
    void setUseExpandableHeaders(boolean enabled);
    void setUseCompactSettings(boolean enabled);
    boolean isViewPager();
    SettingsFragmentManager getSettingsManager();
}
