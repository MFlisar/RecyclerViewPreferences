package com.michaelflisar.recyclerviewpreferences.interfaces;

import android.app.Activity;
import androidx.databinding.ViewDataBinding;

public interface ISettCallback {

    Activity getParentActivity();
    ViewDataBinding getBinding();
    boolean handlesGlobalSetting();
    Object getCustomSettingsObject();
    void showMultiLevelSetting(int groupId);
}
