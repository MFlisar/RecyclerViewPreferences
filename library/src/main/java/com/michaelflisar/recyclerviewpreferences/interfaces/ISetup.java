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

    boolean isHideEmptyHeaders();

    boolean supportSpinnerDropDownHighlighting();

    ISetup copy();

    Integer getActivityTheme();

    interface IFilter extends Parcelable {
        boolean isEnabled(BaseSettingsItem item);
    }
}
