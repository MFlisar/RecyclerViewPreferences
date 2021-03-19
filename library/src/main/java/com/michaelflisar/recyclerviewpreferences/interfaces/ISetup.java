package com.michaelflisar.recyclerviewpreferences.interfaces;

import android.os.Parcelable;

import com.michaelflisar.recyclerviewpreferences.defaults.Setup;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;

/**
 * Created by flisar on 21.08.2017.
 */

public interface ISetup<S extends ISetup> extends Parcelable {

    Setup.SettingsStyle getSettingsStyle();

    S setSettingsStyle(Setup.SettingsStyle style);

    IFilter getFilter();

    S setFilter(IFilter filter);

    Setup.LayoutStyle getLayoutStyle();

    S setLayoutStyle(Setup.LayoutStyle style);

    boolean isUseExpandableHeaders();

    S setUseExpandableHeaders(boolean enabled);

    boolean isDarkTheme();

    boolean hasScrollablePagerTabs();

    S setHideSingleHeader(boolean enabled);

    boolean isHideSingleHeader();

    S setHideEmptyHeaders(boolean enabled);

    boolean isHideEmptyHeaders();

    boolean supportSpinnerDropDownHighlighting();

    S setFlatStyle(boolean value);

    boolean getFlatStyle();

    S setDividerStyle(Setup.DividerStyle value);

    Setup.DividerStyle getDividerStyle();

    int getGridSpan();

    boolean setGridSpan(int span);

    ISetup copy();

    Integer getActivityTheme();

    interface IFilter extends Parcelable {
        boolean isEnabled(BaseSettingsItem item);
    }
}
