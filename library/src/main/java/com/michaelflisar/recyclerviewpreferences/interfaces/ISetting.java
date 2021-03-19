package com.michaelflisar.recyclerviewpreferences.interfaces;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.kt.classes.SettingsText;
import com.michaelflisar.recyclerviewpreferences.classes.Dependency;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.iconics.typeface.IIcon;

import java.util.List;

/**
 * Created by flisar on 16.05.2017.
 */

public interface ISetting<Value, CLASS, SettData extends ISettData<Value, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder &
        ISettingsViewHolder<Value, CLASS, SettData, VH>>
{
    int getSettingId();
    void setSettingId(int id);
    SettData getSettData();

    // IDs
    int getParentId();
    int getDefaultId();
    int getCustomId();
    int getUseCustomId();
    int getViewHolderId();

    // FastAdapter
    int getLayoutTypeId();
    int getLayout();
    <P extends IItem & IExpandable> BaseSettingsItem<P, ?, ?, SettData, ?> createItem(boolean global, boolean compact, ISettCallback settingsCallback, boolean flatStyle);

    // Icon
    IIcon getIcon();
    int getIconPaddingDp();
    Integer getIconColor();

    // Backgorund
//    Integer getBackgroundTint();

    // Titel
    SettingsText getTitle();
    SettingsText getSubTitle();
    Integer getTextColor();
    Integer getTitleTextColor();

    // Info
    SettingsText getInfo();
    boolean isInfoHtml();

    // Type
    BaseSetting.SupportType getSupportType();
    BaseSetting.SettingType getSettingType();

    // Values
    Value getValue(CLASS object, boolean global);
    boolean setValue(CLASS object, boolean global, Value value);
    boolean getCustomEnabled(CLASS object);
    void setCustomEnabled(CLASS object, boolean enabled);

    // Events
    void onValueChanged(int id, Activity activity, boolean global, CLASS customSettingsObject);
    void updateView(int id, Activity activity, boolean global, Value newValue, boolean dialogClosed, Object event);
    void handleDialogEvent(int id, Activity activity, boolean global, CLASS customSettingsObject, Object event);

    // Dependencies
    void clearDependencies();
    void addDependency(Dependency dependency);
    List<Dependency> getDependencies();

    // Functions
    boolean checkId(int id);
}
