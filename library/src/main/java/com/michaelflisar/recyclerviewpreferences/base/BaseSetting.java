package com.michaelflisar.recyclerviewpreferences.base;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.classes.Dependency;
import com.michaelflisar.recyclerviewpreferences.interfaces.IIDSetCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.mikepenz.iconics.typeface.IIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flisar on 16.05.2017.
 */

public abstract class BaseSetting<Value, CLASS, SettData extends ISettData<Value, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder &
        ISettingsViewHolder<Value, CLASS, SettData, VH>> implements ISetting<Value, CLASS, SettData, VH> {

    private int mId;
    private IIDSetCallback mIdSetCallback;
    private SettData mSettData;

    private Class<CLASS> mClazz;

    private IIcon mIcon;
    private int mIconPadding;
    private SettingsText mTitle;
    private SettingsText mSubTitle;
    private SettingsText mInfo;
    private boolean mInfoIsHtml;
    private List<MultiSettingData> mMultiDatas;

    private Dependency mDependency;

    public BaseSetting(Class<CLASS> clazz, SettData settData, SettingsText title, IIcon icon) {
        mIdSetCallback = null;
        mClazz = clazz;
        mSettData = settData;
        mTitle = title;
        mSubTitle = null;
        mInfo = null;
        mInfoIsHtml = false;
        mIcon = icon;
        mIconPadding = 0;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withIdCallback(IIDSetCallback callback) {
        mIdSetCallback = callback;
        return this;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withId(int id) {
        mId = id;
        return this;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withSubTitle(int subTitle) {
        mSubTitle = new SettingsText(subTitle);
        return this;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withSubTitle(String subTitle) {
        mSubTitle = new SettingsText(subTitle);
        return this;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withInfo(SettingsText info, boolean isHtml) {
        mInfo = info;
        mInfoIsHtml = isHtml;
        return this;
    }

    @Override
    public final SettData getSettData() {
        return mSettData;
    }

    @Override
    public final int getSettingId() {
        return mId;
    }

    @Override
    public final void setSettingId(int id) {
        mId = id;
        if (mIdSetCallback != null) {
            mIdSetCallback.onIdSet(id);
        }
    }

    @Override
    public final int getParentId() {
        return mId;
    }

    @Override
    public final int getDefaultId() {
        return mId + 1;
    }

    @Override
    public final int getCustomId() {
        return mId + 2;
    }

    @Override
    public final int getUseCustomId() {
        return mId + 3;
    }

    @Override
    public final SettingsText getTitle() {
        return mTitle;
    }

    @Override
    public final SettingsText getSubTitle() {
        return mSubTitle;
    }

    @Override
    public final SettingsText getInfo() {
        return mInfo;
    }

    @Override
    public final boolean isInfoHtml() {
        return mInfoIsHtml;
    }

    @Override
    public final boolean checkId(int id) {
        return getParentId() == id || getDefaultId() == id || getCustomId() == id || getUseCustomId() == id;
    }

    @Override
    public final IIcon getIcon() {
        return mIcon;
    }

    @Override
    public final int getIconPaddingDp() {
        return mIconPadding;
    }

    // --------------------
    // Getter/Setter
    // --------------------

    public abstract void updateValueView(boolean topView, VH vh, View v, SettData settData, boolean global, CLASS customSettingsObject);

    public abstract void bind(VH vh);

    public abstract void unbind(VH vh);

    public abstract void onShowChangeSetting(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject);

    @Override
    public final Value getValue(CLASS object, boolean global) {
        return mSettData.getValue(object, global);
    }

    @Override
    public final boolean setValue(CLASS object, boolean global, Value value) {
        return mSettData.setValue(object, global, value);
    }

    @Override
    public final boolean getCustomEnabled(CLASS object) {
        return mSettData.getCustomEnabled(object);
    }

    @Override
    public final void setCustomEnabled(CLASS object, boolean enabled) {
        mSettData.setCustomEnabled(object, enabled);
    }

    @Override
    public final void onValueChanged(int id, Activity activity, boolean global, CLASS customSettingsObject) {
        SettingsManager.get().notifySettingChangedListeners(activity, this, mSettData, global, customSettingsObject);
        SettingsManager.get().notifyDependencyListeners(activity, this, mSettData, global, customSettingsObject);
        if (mSettData instanceof ISettData.IValueChangedListener) {
            ((ISettData.IValueChangedListener<CLASS>) mSettData).onValueChanged(id, activity, global, customSettingsObject);
        }
    }

    @Override
    public final Dependency getDependency() {
        return mDependency;
    }

    @Override
    public final void setDependency(Dependency dependency) {
        mDependency = dependency;
    }

    // --------------------
    // Multi Setting
    // --------------------

    @Override
    public boolean isMultiSetting() {
        return mMultiDatas != null;
    }

    @Override
    public int getMultiSettingCount() {
        if (mMultiDatas == null) {
            return 1;
        }
        return mMultiDatas.size();
    }

    @Override
    public MultiSettingData getMultiSetting(int index) {
        return mMultiDatas.get(index);
    }

    protected void addMultiTitle(int text, int defaultId, int customId) {
        if (mMultiDatas == null) {
            mMultiDatas = new ArrayList<>();
        }
        mMultiDatas.add(new MultiSettingData(defaultId, customId, text));
    }

    // --------------------
    // Events
    // --------------------

    @Override
    public void updateView(int id, Activity activity, boolean global, Value newValue, boolean dialogClosed, Object event) {
    }
}
