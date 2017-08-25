package com.michaelflisar.recyclerviewpreferences.defaults;


import com.michaelflisar.recyclerviewpreferences.interfaces.IStateManager;

public class DefaultState implements IStateManager {

    private boolean mUseViewPager = true;
    private boolean mUseExpandableHeaders = true;
    private boolean mDarkTheme = false;
    private boolean mUseCompact = false;

    @Override
    public boolean isUseViewPager() {
        return mUseViewPager;
    }

    @Override
    public boolean isUseExpandableHeaders() {
        return mUseExpandableHeaders;
    }

    @Override
    public boolean isUseCompact() {
        return mUseCompact;
    }

    @Override
    public boolean isDarkTheme() {
        return mDarkTheme;
    }

    public void setIsDarkTheme(boolean dark) {
        mDarkTheme = dark;
    }
}
