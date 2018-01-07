package com.michaelflisar.recyclerviewpreferences.base;


import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.mikepenz.iconics.typeface.IIcon;

import java.util.ArrayList;
import java.util.List;

public class SettingsGroup {
    private IIcon mIcon;
    private int mId;
    private SettingsText mTitle;
    private SettingsGroup mParent;
    private List<SettingsGroup> mGroups;
    private List<BaseSetting> mSettings;

    public SettingsGroup(int title) {
        mIcon = null;
        mTitle = new SettingsText(title);
        mParent = null;
        mGroups = new ArrayList<>();
        mSettings = null;
        mId = -1;
    }

    public SettingsGroup(String title) {
        mIcon = null;
        mTitle = new SettingsText(title);
        mParent = null;
        mGroups = new ArrayList<>();
        mSettings = null;
        mId = -1;
    }

    public SettingsGroup(IIcon icon, int title) {

        mIcon = icon;
        mTitle = new SettingsText(title);
        mParent = null;
        mGroups = null;
        mSettings = new ArrayList<>();
        mId = -1;
    }

    public SettingsGroup(IIcon icon, String title) {

        mIcon = icon;
        mTitle = new SettingsText(title);
        mParent = null;
        mGroups = null;
        mSettings = new ArrayList<>();
        mId = -1;
    }

    public void setGroupId(int id) {
        mId = id;
    }

    public SettingsGroup add(SettingsGroup group) {
        mGroups.add(group);
        group.setParent(this);
        return this;
    }

    public SettingsGroup add(BaseSetting... setting) {
        for (BaseSetting s : setting) {
            mSettings.add(s);
        }
        return this;
    }

    public SettingsGroup add(boolean shouldAdd, BaseSetting... setting) {
        if (!shouldAdd) {
            return this;
        }
        for (BaseSetting s : setting) {
            mSettings.add(s);
        }
        return this;
    }

    public boolean check(int id) {
        if (getGroupId() == id) {
            return true;
        }
        if (mGroups != null) {
            for (SettingsGroup group : mGroups) {
                if (group.check(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    public SettingsGroup getParent() {
        return mParent;
    }

    public SettingsGroup setParent(SettingsGroup parent) {
        mParent = parent;
        return this;
    }

    public List<SettingsGroup> getGroups() {
        return mGroups;
    }

    public boolean isGroupHolder() {
        return mGroups != null;
    }

    public List<BaseSetting> getSettings() {
        return mSettings;
    }

    public IIcon getIcon() {
        return mIcon;
    }

    public List<BaseSettingsItem> getSettingItems(boolean global, boolean compact, ISettCallback settingCallback, boolean withBottomDivider) {
        List<BaseSettingsItem> items = Util.convertList(mSettings, setting -> setting.createItem(global, compact, settingCallback, withBottomDivider));
        if (global) {
            return Util.filterList(items, item -> item.getSettings().getSupportType() != BaseSetting.SupportType.CustomOnly);
        } else {
            return Util.filterList(items, item -> item.getSettings().getSupportType() != BaseSetting.SupportType.GlobalOnly);
        }
    }

    public SettingsText getTitle() {
        return mTitle;
    }

    public int getGroupId() {
        return mId;
    }
}
