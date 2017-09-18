package com.michaelflisar.recyclerviewpreferences;


import android.app.Activity;
import android.content.Context;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.base.SettingsGroup;
import com.michaelflisar.recyclerviewpreferences.defaults.Setup;
import com.michaelflisar.recyclerviewpreferences.implementations.DialogHandler;
import com.michaelflisar.recyclerviewpreferences.interfaces.IDialogHandler;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetup;
import com.michaelflisar.recyclerviewpreferences.utils.Definitions;
import com.michaelflisar.recyclerviewpreferences.utils.SettingsUtil;
import com.michaelflisar.recyclerviewpreferences.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hundredthirtythree.sessionmanager.SessionManager;

public class SettingsManager {

    private static SettingsManager instance;
    private Context mContext;
    private Set<OnSettingsChangedListener> mSettingChangedListeners;
    private int mLastId = 0;
    private List<SettingsGroup> mTopGroup;
    private HashMap<Integer, SettingsGroup> mGroupsMap;
    private List<BaseSetting> mSettings;
    private HashMap<Integer, BaseSetting> mSettingsMap;
    private Set<Integer> mCollapsedSettingIds;
    private ISetup mStateManager;
    private DialogHandler mDialogHandler = null;

    private SettingsManager() {
        mSettingChangedListeners = new HashSet<>();
        mTopGroup = new ArrayList<>();
        mGroupsMap = new HashMap<>();
        mSettings = new ArrayList<>();
        mSettingsMap = new HashMap<>();
        mCollapsedSettingIds = new HashSet<>();
        mStateManager = new Setup();
        mDialogHandler = new DialogHandler();
    }

    public static SettingsManager get() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public void init(Context context, ISetup stateManager, String sharedPreferenceName) {
        mContext = context.getApplicationContext();
        mStateManager = stateManager == null ? new Setup() : stateManager;

        if (sharedPreferenceName != null) {
            new SessionManager.Builder()
                    .setContext(mContext)
                    .setPrefsName(sharedPreferenceName)
                    .build();
        }
    }

    public DialogHandler getDialogHandler() {
        if (mDialogHandler == null) {
            mDialogHandler = new DialogHandler();
        }
        return mDialogHandler;
    }

    public void setDialogHandler(DialogHandler dialogHandler) {
        mDialogHandler = dialogHandler;
    }

    public <T> void registerDialogHandler(IDialogHandler<T> handler) {
        mDialogHandler.registerDialogHandler(handler.getHandledClass(), handler);
    }

    public boolean isInitialised() {
        return mContext != null;
    }

    private void checkInit() {
        if (mContext == null) {
            throw new RuntimeException("SettingsSetup have not been initialised!");
        }
    }

    public ISetup getState() {
        return mStateManager;
    }

    public Context getContext() {
        checkInit();
        return mContext;
    }

    public void addSettingChangedListener(OnSettingsChangedListener listener) {
        mSettingChangedListeners.add(listener);
    }

    public void removeSettingChangedListener(OnSettingsChangedListener listener) {
        mSettingChangedListeners.remove(listener);
    }

    public void notifySettingChangedListeners(Activity activity, ISetting setting, ISettData settData, boolean global, Object customSettingsObject) {
        synchronized (mSettingChangedListeners) {
            for (OnSettingsChangedListener l : mSettingChangedListeners) {
                l.onSettingsChanged(activity, setting, settData, global, customSettingsObject);
            }
        }
    }

    public void notifyDependencyListeners(Activity activity, ISetting setting, ISettData settData, boolean global, Object customSettingsObject) {
        synchronized (mSettings) {
            ISetting s = Util.find(mSettings, sett -> sett.getSettingId() == setting.getSettingId());
            if (s != null){
                SettingsFragmentInstanceManager.get().dispatchDependencyChanged(s.getSettingId(), global, customSettingsObject);
            }
        }
    }

    public void dispatchNumberChanged(final int id, Activity activity, Integer number, boolean global) {
        SettingsFragmentInstanceManager.get().dispatchNumberChanged(id, activity, number, global);
    }

    public void dispatchTextChanged(final int id, Activity activity, String value, boolean global) {
        SettingsFragmentInstanceManager.get().dispatchTextChanged(id, activity, value, global);
    }

    public void dispatchColorSelected(final int id, Activity activity, int color, boolean global) {
        SettingsFragmentInstanceManager.get().dispatchColorSelected(id, activity, color, global);
    }

    public void dispatchCustomDialogEvent(final int id, Activity activity, Object data, boolean global) {
        SettingsFragmentInstanceManager.get().dispatchCustomDialogEvent(id, activity, data, global);
    }

    public <S extends BaseSetting> void add(S setting) {
        // 1) create unique id in this manager ONLY if no manual id is not set yet
        if (setting.getSettingId() == -1) {
            mLastId += Definitions.IDS_PER_SETTING;
            setting.setSettingId(mLastId);
        }
        // 2) add the setting to the list and map
        mSettings.add(setting);
        if (mSettingsMap.containsKey(setting.getSettingId())) {
            throw new RuntimeException("Not unique id found: " + setting.getSettingId() + " - make sure to apply unique ids or let the SettingsSetup handle the ids for you!");
        }
        mSettingsMap.put(setting.getSettingId(), setting);
    }

    public void add(SettingsGroup... groups) {
        add(Arrays.asList(groups));
    }

    public void add(List<SettingsGroup> groups) {
        for (SettingsGroup group : groups) {
            add(group, true);
        }
    }

    public void add(SettingsGroup group) {
        add(group, true);
    }

    public ISetting find(int settingsId) {
        return mSettingsMap.get(settingsId);
    }

    private void add(SettingsGroup group, boolean isTopGroup) {
        // 1) add all settings to all settings list and map
        if (group.getSettings() != null) {
            for (int i = 0; i < group.getSettings().size(); i++) {
                add(group.getSettings().get(i));
            }
        } else {
            for (SettingsGroup g : group.getGroups()) {
                add(g, false);
            }
        }
        // 2) create unique id in this manager ONLY if no manual id is not set yet
        if (group.getGroupId() == -1) {
            mLastId += 1;
            group.setGroupId(mLastId);
        }
        // 2) add the group to the list and map
        if (isTopGroup) {
            mTopGroup.add(group);
        }
        if (mGroupsMap.containsKey(group.getGroupId())) {
            throw new RuntimeException("Not unique id found: " + group.getGroupId() + " - make sure to apply unique ids or let the SettingsSetup handle the ids for you!");
        }
        mGroupsMap.put(group.getGroupId(), group);

        // TODO: validate that only unique preference keys are used!
    }

    public void setSettingCollapsed(int id) {
        mCollapsedSettingIds.add(id);
    }

    public Set<Integer> getCollapsedSettingIds() {
        return mCollapsedSettingIds;
    }

    public List<SettingsGroup> getTopGroup() {
        return mTopGroup;
    }

    public ArrayList<Integer> getTopGroupIds() {
        return Util.convertList(mTopGroup, group -> group.getGroupId());
    }

    public SettingsGroup getGroupById(int id) {
        return mGroupsMap.get(id);
    }

    public List<ISetting> getSettingsByGroupId(int id) {
        List<ISetting> settings = new ArrayList<>();
        SettingsGroup group = getGroupById(id);
        if (group != null) {
            if (group.getGroups() != null) {
                for (SettingsGroup g : group.getGroups()) {
                    settings.addAll(getSettingsByGroupId(g.getGroupId()));
                }
            } else {
                settings.addAll(group.getSettings());
            }
        }
        return settings;
    }

    public List<ISetting> getAllSettings() {
        List<ISetting> settings = SettingsUtil.getAllSettings(mTopGroup);
        return settings;
    }

    public interface OnSettingsChangedListener {
        void onSettingsChanged(Activity activity, ISetting setting, ISettData settData, boolean global, Object customSettingsObject);
    }
}
