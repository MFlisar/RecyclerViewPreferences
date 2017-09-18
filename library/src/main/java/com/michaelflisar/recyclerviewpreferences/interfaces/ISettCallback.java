package com.michaelflisar.recyclerviewpreferences.interfaces;

import android.app.Activity;
import android.databinding.ViewDataBinding;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

public interface ISettCallback {

    Activity getParentActivity();
    ViewDataBinding getBinding();
    boolean handlesGlobalSetting();
    Object getCustomSettingsObject();
    void showMultiLevelSetting(int groupId);
}
