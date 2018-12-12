package com.michaelflisar.recyclerviewpreferences.base;

import android.app.Activity;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.classes.Dependency;
import com.michaelflisar.recyclerviewpreferences.interfaces.IIDSetCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.utils.SettingsId;
import com.mikepenz.iconics.typeface.IIcon;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by flisar on 16.05.2017.
 */

public abstract class BaseSetting<
        Value,
        CLASS,
        SettData extends ISettData<Value, CLASS, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Value, CLASS, SettData, VH>>
        implements ISetting<Value, CLASS, SettData, VH>
{

    public enum SupportType
    {
        Normal,
        CustomOnly,
        GlobalOnly
    }

    public enum SettingType
    {
        Normal,
        Info
    }

    private SettingsId mId = null;
    private IIDSetCallback mIdSetCallback;
    private SettData mSettData;

    private Class<CLASS> mClazz;

    private IIcon mIcon;
    private int mIconPadding;
    private Integer mIconColor;
    private SettingsText mTitle;
    private SettingsText mSubTitle;
    private SettingsText mInfo;
    private boolean mInfoIsHtml;
    private SupportType mSupportType;
    private SettingType mSettingType;
    private Integer mTitleTextColor;
    private Integer mTextColor;
//    private Integer mBackgroundTint;

    private List<Dependency> mDependency;

    public BaseSetting(Class<CLASS> clazz, SettData settData, SettingsText title, IIcon icon)
    {
        mDependency = new ArrayList<>();
        mIdSetCallback = null;
        mClazz = clazz;
        mSettData = settData;
        mTitle = title;
        mSubTitle = null;
        mInfo = null;
        mInfoIsHtml = false;
        mIcon = icon;
        mIconPadding = 0;
        mIconColor = null;
        mSupportType = SupportType.Normal;
        mSettingType = SettingType.Normal;
        mTitleTextColor = null;
        mTextColor = null;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withIdCallback(IIDSetCallback callback)
    {
        mIdSetCallback = callback;
        return this;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withId(int id)
    {
        mId = new SettingsId(id);
        return this;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withSubTitle(int subTitle)
    {
        if (subTitle > 0)
        {
            mSubTitle = new SettingsText(subTitle);
        }
        return this;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withSubTitle(String subTitle)
    {
        mSubTitle = new SettingsText(subTitle);
        return this;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withInfo(SettingsText info, boolean isHtml)
    {
        mInfo = info;
        mInfoIsHtml = isHtml;
        return this;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withInfo(int info, boolean isHtml)
    {
        if (info > 0)
        {
            mInfo = new SettingsText(info);
        }
        mInfoIsHtml = isHtml;
        return this;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withTitleTextColor(int color)
    {
        mTitleTextColor = color;
        return this;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withTextColor(int color)
    {
        mTextColor = color;
        return this;
    }

//    public BaseSetting<Value, CLASS, SettData, VH> withBackgroundTint(int color)
//    {
//        mBackgroundTint = color;
//        return this;
//    }

    public BaseSetting<Value, CLASS, SettData, VH> withInfo(String info, boolean isHtml)
    {
        mInfo = new SettingsText(info);
        mInfoIsHtml = isHtml;
        return this;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withIconSetup(int iconPadding, Integer iconColor)
    {
        mIconPadding = iconPadding;
        mIconColor = iconColor;
        return this;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withSupportType(SupportType supportType)
    {
        mSupportType = supportType;
        return this;
    }

    public BaseSetting<Value, CLASS, SettData, VH> withSettingType(SettingType settingType)
    {
        mSettingType = settingType;
        return this;
    }

    @Override
    public final SettData getSettData()
    {
        return mSettData;
    }

    @Override
    public final int getSettingId()
    {
        return mId == null ? -1 : mId.getId();
    }

    @Override
    public final void setSettingId(int id)
    {
        mId = new SettingsId(id);
        if (mIdSetCallback != null)
        {
            mIdSetCallback.onIdSet(id);
        }
    }

    @Override
    public final int getParentId()
    {
        return mId == null ? -1 : mId.getId();
    }

    @Override
    public final int getDefaultId()
    {
        return mId == null ? -1 : mId.getDefaultId();
    }

    @Override
    public final int getCustomId()
    {
        return mId == null ? -1 : mId.getCustomId();
    }

    @Override
    public final int getUseCustomId()
    {
        return mId == null ? -1 : mId.getUseCustomId();
    }

    @Override
    public final int getViewHolderId()
    {
        return mId == null ? -1 : mId.getViewHolderId();
    }

//    @Override
//    public final Integer getBackgroundTint() {
//        return mBackgroundTint;
//    }

    @Override
    public final SettingsText getTitle()
    {
        return mTitle;
    }

    @Override
    public final SettingsText getSubTitle()
    {
        return mSubTitle;
    }

    @Override
    public final Integer getTextColor()
    {
        return mTextColor;
    }

    @Override
    public final Integer getTitleTextColor()
    {
        return mTitleTextColor;
    }

    @Override
    public final SettingsText getInfo()
    {
        return mInfo;
    }

    @Override
    public final boolean isInfoHtml()
    {
        return mInfoIsHtml;
    }

    @Override
    public final SupportType getSupportType()
    {
        return mSupportType;
    }

    @Override
    public final SettingType getSettingType()
    {
        return mSettingType;
    }

    @Override
    public final boolean checkId(int id)
    {
        return getParentId() == id || getDefaultId() == id || getCustomId() == id || getUseCustomId() == id;
    }

    @Override
    public final IIcon getIcon()
    {
        return mIcon;
    }

    @Override
    public final int getIconPaddingDp()
    {
        return mIconPadding;
    }

    @Override
    public final Integer getIconColor()
    {
        return mIconColor;
    }

    // --------------------
    // Getter/Setter
    // --------------------

    public abstract void updateValueView(boolean topView, VH vh, View v, SettData settData, boolean global, ISettCallback callback);

    public abstract void bind(VH vh);

    public abstract void unbind(VH vh);

    public abstract void onShowChangeSetting(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject);

    @Override
    public final Value getValue(CLASS object, boolean global)
    {
        return mSettData.getValue(object, global);
    }

    @Override
    public final boolean setValue(CLASS object, boolean global, Value value)
    {
        return mSettData.setValue(object, global, value);
    }

    @Override
    public final boolean getCustomEnabled(CLASS object)
    {
        return mSettData.getCustomEnabled(object);
    }

    @Override
    public final void setCustomEnabled(CLASS object, boolean enabled)
    {
        mSettData.setCustomEnabled(object, enabled);
    }

    @Override
    public final void onValueChanged(int id, Activity activity, boolean global, CLASS customSettingsObject)
    {
        SettingsManager.get().notifySettingChangedListeners(activity, this, mSettData, global, customSettingsObject);
        SettingsManager.get().notifyDependencyListeners(activity, this, mSettData, global, customSettingsObject);
        if (mSettData instanceof ISettData.IValueChangedListener)
        {
            ((ISettData.IValueChangedListener<CLASS>) mSettData).onValueChanged(id, activity, global, customSettingsObject);
        }
    }

    @Override
    public final void clearDependencies()
    {
        mDependency.clear();
    }

    @Override
    public final List<Dependency> getDependencies()
    {
        return mDependency;
    }

    @Override
    public final void addDependency(Dependency dependency)
    {
        mDependency.add(dependency);
    }

    // --------------------
    // Events
    // --------------------

    @Override
    public void handleDialogEvent(int id, Activity activity, boolean global, CLASS customSettingsObject, Object event)
    {

    }

    @Override
    public void updateView(int id, Activity activity, boolean global, Value newValue, boolean dialogClosed, Object event)
    {
    }

    public void onLayoutReady(VH viewHolder)
    {

    }
}
