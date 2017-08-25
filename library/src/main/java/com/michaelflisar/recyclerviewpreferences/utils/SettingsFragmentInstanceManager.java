package com.michaelflisar.recyclerviewpreferences.utils;

import android.app.Activity;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.fragments.SettingsFragment;
import com.michaelflisar.recyclerviewpreferences.implementations.DialogHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by flisar on 21.08.2017.
 */

public class SettingsFragmentInstanceManager {

    // TODO
    // Would work if we use own dialogs ONLY which use this class to broadcast their event instead of activity callbacks!
    // then we would NOT need to implement any interface in the parent activity

    private static SettingsFragmentInstanceManager instance;
    private Set<SettingsFragment> mFragments = new HashSet<>();

    private SettingsFragmentInstanceManager() {
    }

    public static SettingsFragmentInstanceManager get() {
        if (instance == null) {
            instance = new SettingsFragmentInstanceManager();
        }
        return instance;
    }

    public void register(SettingsFragment fragment) {
        mFragments.add(fragment);
    }

    public void unregister(SettingsFragment fragment) {
        mFragments.remove(fragment);
    }

    public void dispatchHandleCustomDialogEvent(final int id, Activity activity, Object data, boolean global) {
        synchronized (mFragments) {
            for (SettingsFragment f : mFragments) {
                if (!f.isViewPager()) {
                    SettingsManager.get().getDialogHandler().handleDialogResult(f, DialogHandler.DialogType.Custom, id, activity, data, global, f.getCustomSettingsObject());
                }
            }
        }
    }

    public void dispatchHandleNumberChanged(final int id, Activity activity, List<Integer> numbers, boolean global) {
        synchronized (mFragments) {
            for (SettingsFragment f : mFragments) {
                if (!f.isViewPager()) {
                    SettingsManager.get().getDialogHandler().handleDialogResult(f, DialogHandler.DialogType.Number, id, activity, numbers, global, f.getCustomSettingsObject());
                }
            }
        }
    }

    public void dispatchHandleTextChanged(final int id, Activity activity, String value, boolean global) {
        synchronized (mFragments) {
            for (SettingsFragment f : mFragments) {
                if (!f.isViewPager()) {
                    SettingsManager.get().getDialogHandler().handleDialogResult(f, DialogHandler.DialogType.Text, id, activity, value, global, f.getCustomSettingsObject());
                }
            }
        }
    }

    public void dispatchHandleColorSelected(final int id, Activity activity, int color, boolean global) {
        synchronized (mFragments) {
            for (SettingsFragment f : mFragments) {
                if (!f.isViewPager()) {
                    SettingsManager.get().getDialogHandler().handleDialogResult(f, DialogHandler.DialogType.Color, id, activity, color, global, f.getCustomSettingsObject());
                }
            }
        }
    }

    public void dispatchDependencyChanged(final int id,  boolean global, Object customSettingsObject) {
        synchronized (mFragments) {
            for (SettingsFragment f : mFragments) {
                if (!f.isViewPager()) {
                    f.getSettingsManager().dispatchDependencyChanged(id, global, customSettingsObject);
                }
            }
        }
    }
}
