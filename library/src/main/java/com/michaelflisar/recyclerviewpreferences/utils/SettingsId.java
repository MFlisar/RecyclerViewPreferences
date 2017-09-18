package com.michaelflisar.recyclerviewpreferences.utils;

/**
 * Created by flisar on 01.09.2017.
 */

public class SettingsId {

    private int mId;

    public SettingsId(int settingsId) {
        mId = settingsId;
    }

    public final int getId() {
        return mId;
    }

    public final int getDefaultId() {
        return mId + 1;
    }

    public final int getCustomId() {
        return mId + 2;
    }

    public final int getUseCustomId() {
        return mId + 3;
    }

    public final int getViewHolderId() {
        return getDefaultId();
    }
}
