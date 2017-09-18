package com.michaelflisar.recyclerviewpreferences;

import android.app.Activity;

import com.michaelflisar.recyclerviewpreferences.implementations.DialogHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by flisar on 21.08.2017.
 *
 * Internal helper class used by SettingsFragment and SettingsManager
 * Necessary functions are exposed via SettingsManager
 */
class SettingsFragmentInstanceManager {

    private static SettingsFragmentInstanceManager instance;
    private Set<SettingsFragment> mFragments = new HashSet<>();

    private SettingsFragmentInstanceManager() {
    }

    static SettingsFragmentInstanceManager get() {
        if (instance == null) {
            instance = new SettingsFragmentInstanceManager();
        }
        return instance;
    }

    void register(SettingsFragment fragment) {
        mFragments.add(fragment);
    }

    void unregister(SettingsFragment fragment) {
        mFragments.remove(fragment);
    }

    void dispatchCustomDialogEvent(final int id, Activity activity, Object data, boolean global) {
        synchronized (mFragments) {
            for (SettingsFragment f : mFragments) {
                if (!f.isViewPager() && f.getSettingsManager() != null && f.handlesGlobalSetting() == global) {
                    SettingsManager.get().getDialogHandler().handleDialogResult(f, DialogHandler.DialogType.Custom, id, activity, data, global, f.getCustomSettingsObject());
                }
            }
        }
    }

    void dispatchNumberChanged(final int id, Activity activity, Integer number, boolean global) {
        synchronized (mFragments) {
            for (SettingsFragment f : mFragments) {
                if (!f.isViewPager() && f.getSettingsManager() != null && f.handlesGlobalSetting() == global) {
                    SettingsManager.get().getDialogHandler().handleDialogResult(f, DialogHandler.DialogType.Number, id, activity, number, global, f.getCustomSettingsObject());
                }
            }
        }
    }

    void dispatchTextChanged(final int id, Activity activity, String value, boolean global) {
        synchronized (mFragments) {
            for (SettingsFragment f : mFragments) {
                if (!f.isViewPager() && f.getSettingsManager() != null && f.handlesGlobalSetting() == global) {
                    SettingsManager.get().getDialogHandler().handleDialogResult(f, DialogHandler.DialogType.Text, id, activity, value, global, f.getCustomSettingsObject());
                }
            }
        }
    }

    void dispatchColorSelected(final int id, Activity activity, int color, boolean global) {
        synchronized (mFragments) {
            for (SettingsFragment f : mFragments) {
                if (!f.isViewPager() && f.getSettingsManager() != null && f.handlesGlobalSetting() == global) {
                    SettingsManager.get().getDialogHandler().handleDialogResult(f, DialogHandler.DialogType.Color, id, activity, color, global, f.getCustomSettingsObject());
                }
            }
        }
    }

    void dispatchDependencyChanged(final int id, boolean global, Object customSettingsObject) {
        synchronized (mFragments) {
            for (SettingsFragment f : mFragments) {
                if (!f.isViewPager() && f.getSettingsManager() != null && f.handlesGlobalSetting() == global) {
                    f.getSettingsManager().dispatchDependencyChanged(id, global, customSettingsObject);
                }
            }
        }
    }
}
