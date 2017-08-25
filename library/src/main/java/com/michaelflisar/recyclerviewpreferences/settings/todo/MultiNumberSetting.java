package com.michaelflisar.recyclerviewpreferences.settings.todo;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.base.SettingsText;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.TextSettingItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.utils.SettingsFragmentInstanceManager;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.iconics.typeface.IIcon;

import java.util.Arrays;
import java.util.List;

/**
 * Created by flisar on 16.05.2017.
 */

public abstract class MultiNumberSetting<CLASS, SettData extends ISettData<List<Integer>, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder &
        ISettingsViewHolder<List<Integer>, CLASS, SettData, VH>> extends BaseSetting<List<Integer>, CLASS, SettData, VH> {
    private int mMin;
    private int mMax;
    private int mStepSize;
    private int mUnitResForDialog;
    private int mDisplayRes;
    private boolean mUseDpSizeDialog;

    public MultiNumberSetting(Class<CLASS> clazz, SettData settData, SettingsText title, IIcon icon, int min, int max, int stepSize, int unitResForDialog, int displayRes,
            boolean useDpSizeDialog) {
        super(clazz, settData, title, icon);
        mMin = min;
        mMax = max;
        mStepSize = stepSize;
        mUnitResForDialog = unitResForDialog;
        mDisplayRes = displayRes;
        mUseDpSizeDialog = useDpSizeDialog;
    }

    @Override
    public void updateValueView(boolean topView, VH vh, View v, SettData settData, boolean global, CLASS customSettingsObject) {
        String text = SettingsManager.get().getContext().getString(mDisplayRes, (Object[]) getValue(customSettingsObject, global).toArray(new Integer[getMultiSettingCount()]));
        ((TextView) v).setText(text);
    }

    @Override
    public void unbind(VH vh) {
    }

    @Override
    public void onShowChangeSetting(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject) {
        // TODO: Dialog
//        SettingsManager.get().getDialogHandler().showChangeNumber(global, this, global ? getDefaultId() : getCustomId(), activity, settData, customSettingsObject);
    }

    @Override
    public final int getLayoutTypeId() {
        return R.id.id_adapter_setting_text_item;
    }

    @Override
    public final int getLayout() {
        return R.layout.adapter_setting_item_text;
    }

    @Override
    public <P extends IItem & IExpandable> BaseSettingsItem<P, List<Integer>, CLASS, SettData, ?> createItem(boolean global, boolean compact, ISettCallback settingsCallback,
            boolean withBottomDivider) {
        return new TextSettingItem(global, compact, this, settingsCallback, withBottomDivider);
    }

    @Override
    public void updateView(int id, Activity activity, boolean global, List<Integer> newValues, boolean dialogClosed, Object event) {
        if (dialogClosed) {
            SettingsFragmentInstanceManager.get().dispatchHandleNumberChanged(id, activity, newValues, global);
//            ((DialogUtil.NumberCallback) activity).onNumberSelected(id, newValue, global);
        }
    }

    public final int getMin() {
        return mMin;
    }

    public final int getMax() {
        return mMax;
    }

    public final int getStepSize() {
        return mStepSize;
    }

    public final int getUnitResForDialog() {
        return mUnitResForDialog;
    }

    public final boolean getUseDpSizeDialog() {
        return mUseDpSizeDialog;
    }
}
