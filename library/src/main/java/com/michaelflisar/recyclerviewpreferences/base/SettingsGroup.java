package com.michaelflisar.recyclerviewpreferences.base;


import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetup;
import com.michaelflisar.recyclerviewpreferences.kt.classes.SettingsText;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.mikepenz.iconics.typeface.IIcon;

import java.util.ArrayList;
import java.util.List;

public class SettingsGroup {
    // general
    private IIcon mIcon;
    private int mId;
    private SettingsText mTitle;
    private SettingsGroup mParent;
    private boolean mHideInLayout;

    // Group Holder
    private List<SettingsGroup> mGroups;

    // Settings Holder
    private List<BaseSetting> mSettings;

    public SettingsGroup(int title) {
        init(new SettingsText(title));
    }

    public SettingsGroup(String title) {
        init(new SettingsText(title));
    }

    public SettingsGroup(int title, IIcon icon) {
        init(new SettingsText(title));
        setIcon(icon);
    }

    public SettingsGroup(String title, IIcon icon) {
        init(new SettingsText(title));
        setIcon(icon);
    }

    private void init(SettingsText title) {
        mIcon = null;
        mTitle = title;
        mParent = null;
        mId = -1;
        mHideInLayout = false;

        mGroups = null;
        mSettings = null;
    }

    void setIcon(IIcon icon) {
        mIcon = icon;
    }

    public void setGroupId(int id) {
        mId = id;
    }

    public SettingsGroup add(SettingsGroup group) {
        if (mSettings != null) {
            throw new RuntimeException("Either add settings or other groups to a SettingsGroup only, don't mix them!");
        }
        if (mGroups == null) {
            mGroups = new ArrayList<>();
        }
        mGroups.add(group);
        group.setParent(this);
        return this;
    }

    public SettingsGroup add(BaseSetting... setting) {
        if (mGroups != null) {
            throw new RuntimeException("Either add settings or other groups to a SettingsGroup only, don't mix them!");
        }
        if (mSettings == null) {
            mSettings = new ArrayList<>();
        }
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
            add(s);
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

    public SettingsGroup withHideInLayout() {
        mHideInLayout = true;
        return this;
    }

    public boolean isHideInLayout() {
        return mHideInLayout;
    }

    public List<BaseSettingsItem> getSettingItems(boolean global, boolean compact, ISettCallback settingCallback, ISetup.IFilter filter, boolean flatStyle) {
        List<BaseSettingsItem> items = Util.convertList(mSettings, setting -> setting.createItem(global, compact, settingCallback, flatStyle));
        if (global) {
            items = Util.filterList(items, item -> item.getSettings().getSupportType() != BaseSetting.SupportType.CustomOnly);
        } else {
            items = Util.filterList(items, item -> item.getSettings().getSupportType() != BaseSetting.SupportType.GlobalOnly);
        }
        if (filter != null) {
            items = Util.filterList(items, item -> filter.isEnabled(item));
        }
        return items;
    }

    public SettingsText getTitle() {
        return mTitle;
    }

    public int getGroupId() {
        return mId;
    }
}
