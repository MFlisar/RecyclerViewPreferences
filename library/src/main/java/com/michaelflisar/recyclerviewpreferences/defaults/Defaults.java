package com.michaelflisar.recyclerviewpreferences.defaults;


public class Defaults {

    // defaults
    private boolean mUseViewPager = true;
    private boolean mUseExpandableHeaders = true;
    private boolean mDarkTheme = false;

    public void setUseViewPager(boolean useViewPager) {
        mUseViewPager = useViewPager;
    }

    public boolean isUseViewPager() {
        return mUseViewPager;
    }

    public boolean isUseExpandableHeaders() {
        return mUseExpandableHeaders;
    }

    public void setUseExpandableHeaders(boolean useExpandableHeaders) {
        mUseExpandableHeaders = useExpandableHeaders;
    }

    public boolean isDarkTheme() {
        return mDarkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        mDarkTheme = darkTheme;
    }
}
