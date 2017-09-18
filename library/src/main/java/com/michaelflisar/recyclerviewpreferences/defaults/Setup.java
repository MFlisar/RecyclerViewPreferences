package com.michaelflisar.recyclerviewpreferences.defaults;


import android.os.Parcel;
import android.os.Parcelable;

import com.michaelflisar.recyclerviewpreferences.interfaces.ISetup;

public class Setup implements Cloneable, ISetup<Setup>, Parcelable {

    public enum SettingsStyle {
        ViewPager, // Pages will always use List currently and never a multi level list!
        List,
        MultiLevelList;

        public SettingsStyle getPageStyle() {
            return List;
        }
    }

    public enum LayoutStyle {
        Normal,
        Compact
    }

    private SettingsStyle mSettingsStyle = SettingsStyle.ViewPager;
    private LayoutStyle mLayoutStyle = LayoutStyle.Normal;
    private boolean mUseExpandableHeaders = true;
    private boolean mDarkTheme = false;
    private boolean mScrollablePagerTabs = true;
    private boolean mHideEmptyHeaders = false;
    private boolean mSupportSpinnerDropDownhighlighting = true; // buggy, currently does not update the current selected item if item is changed and dropdown is opened again

    public Setup() {

    }

    protected Setup(Parcel in) {
        mUseExpandableHeaders = in.readByte() != 0;
        mDarkTheme = in.readByte() != 0;
        mScrollablePagerTabs = in.readByte() != 0;
        mSettingsStyle = SettingsStyle.values()[in.readInt()];
        mLayoutStyle = LayoutStyle.values()[in.readInt()];
        mSupportSpinnerDropDownhighlighting = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (mUseExpandableHeaders ? 1 : 0));
        dest.writeByte((byte) (mDarkTheme ? 1 : 0));
        dest.writeByte((byte) (mScrollablePagerTabs ? 1 : 0));
        dest.writeInt(mSettingsStyle.ordinal());
        dest.writeInt(mLayoutStyle.ordinal());
        dest.writeByte((byte) (mSupportSpinnerDropDownhighlighting ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Setup> CREATOR = new Creator<Setup>() {
        @Override
        public Setup createFromParcel(Parcel in) {
            return new Setup(in);
        }

        @Override
        public Setup[] newArray(int size) {
            return new Setup[size];
        }
    };

    @Override
    public SettingsStyle getSettingsStyle() {
        return mSettingsStyle;
    }

    @Override
    public LayoutStyle getLayoutStyle() {
        return mLayoutStyle;
    }

    @Override
    public boolean isUseExpandableHeaders() {
        return mUseExpandableHeaders;
    }

    @Override
    public boolean isDarkTheme() {
        return mDarkTheme;
    }

    @Override
    public boolean hasScrollablePagerTabs() {
        return mScrollablePagerTabs;
    }

    @Override
    public boolean isHideEmptyHeaders() {
        return mHideEmptyHeaders;
    }

    @Override
    public boolean supportSpinnerDropDownHighlighting() {
        return mSupportSpinnerDropDownhighlighting;
    }

    public void setIsDarkTheme(boolean dark) {
        mDarkTheme = dark;
    }

    public void setHasScrollablePagerTabs(boolean enabled) {
        mScrollablePagerTabs = enabled;
    }

    public Setup setUseExpandableHeaders(boolean enabled) {
        mUseExpandableHeaders = enabled;
        return this;
    }

    public Setup setLayoutStyle(LayoutStyle style) {
        mLayoutStyle = style;
        return this;
    }

    public Setup setSettingsStyle(SettingsStyle style) {
        mSettingsStyle = style;
        return this;
    }

    public Setup copy() {
        try {
            return (Setup) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getActivityTheme() {
        return null;
    }
}