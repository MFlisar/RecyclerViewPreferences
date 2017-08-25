package com.michaelflisar.recyclerviewpreferences.settings;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.base.SettingsText;
import com.michaelflisar.recyclerviewpreferences.databinding.AdapterSettingItemNumberBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.NumberSettingItem;
import com.michaelflisar.recyclerviewpreferences.fragments.dialogs.NumberSettingsDialogFragmentBundleBuilder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.utils.SettingsFragmentInstanceManager;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.iconics.typeface.IIcon;

import java.util.Arrays;

/**
 * Created by flisar on 16.05.2017.
 */

public class NumberSetting<CLASS, SettData extends ISettData<Integer, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder &
        ISettingsViewHolder<Integer, CLASS, SettData, VH>> extends BaseSetting<Integer, CLASS, SettData, VH> {

    public enum Mode {
        SeekbarAndDialog,
        Seekbar,
        Dialog;

        public int getSeekbarVisibility() {
            return this == Dialog ? View.GONE : View.VISIBLE;
        }

        public boolean getSupportsDialog() {
            return this == SeekbarAndDialog || this == Dialog;
        }
    }

    private Mode mMode;
    private int mMin;
    private int mMax;
    private int mStepSize;
    private Integer mUnitRes;
    private boolean mUseDpSizeDialog;

    public NumberSetting(Class<CLASS> clazz, SettData settData, int title, IIcon icon, Mode mode, int min, int max, int stepSize, Integer unitRes, boolean useDpSizeDialog) {
        this(clazz, settData, new SettingsText(title), icon, mode, min, max, stepSize, unitRes, useDpSizeDialog);
    }

    public NumberSetting(Class<CLASS> clazz, SettData settData, String title, IIcon icon, Mode mode, int min, int max, int stepSize, Integer unitRes, boolean useDpSizeDialog) {
        this(clazz, settData, new SettingsText(title), icon, mode, min, max, stepSize, unitRes, useDpSizeDialog);
    }

    private NumberSetting(Class<CLASS> clazz, SettData settData, SettingsText title, IIcon icon, Mode mode, int min, int max, int stepSize, Integer unitRes, boolean useDpSizeDialog) {
        super(clazz, settData, title, icon);
        mMode = mode;
        mMin = min;
        mMax = max;
        mStepSize = stepSize;
        mUnitRes = unitRes;
        mUseDpSizeDialog = useDpSizeDialog;
    }

    @Override
    public void updateValueView(boolean topView, VH vh, View v, SettData settData, boolean global, CLASS customSettingsObject) {
        String text = mUnitRes == null ? String.valueOf(getValue(customSettingsObject, global)) : SettingsManager.get().getContext().getString(mUnitRes, getValue(customSettingsObject, global));
        ((TextView) v).setText(text);

        if (topView) {
            int seekbarValue = (getValue(customSettingsObject, global) - getMin()) / getStepSize();
            ((AdapterSettingItemNumberBinding) vh.getBinding()).sbTop.setProgress(seekbarValue);
        }
    }

    @Override
    public void bind(VH vh) {
        NumberSettingItem.NumberViewHolder viewHolder = (NumberSettingItem.NumberViewHolder) vh;
        SeekBar seekBar = ((AdapterSettingItemNumberBinding) viewHolder.getBinding()).sbTop;
        seekBar.setMax(mMax - mMin);
    }

    @Override
    public void unbind(VH vh) {
    }

    @Override
    public void onShowChangeSetting(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject) {
        if (mMode.getSupportsDialog()) {
            new NumberSettingsDialogFragmentBundleBuilder(
                    getSettingId(),
                    global,
                    getValue(customSettingsObject, global),
                    getMin(),
                    getStepSize(),
                    getMax(),
                    getTitle().getText(),
                    getUseDpSizeDialog()
            )
                    .createFragment()
                    .show(((FragmentActivity)activity).getSupportFragmentManager(), null);
        }
    }

    @Override
    public final int getLayoutTypeId() {
        return R.id.id_adapter_setting_number_item;
    }

    @Override
    public final int getLayout() {
        return R.layout.adapter_setting_item_number;
    }

    @Override
    public <P extends IItem & IExpandable> BaseSettingsItem<P, Integer, CLASS, SettData, ?> createItem(boolean global, boolean compact, ISettCallback settingsCallback,
            boolean withBottomDivider) {
        return new NumberSettingItem(global, compact, this, settingsCallback, withBottomDivider);
    }

    @Override
    public void updateView(int id, Activity activity, boolean global, Integer newValue, boolean dialogClosed, Object event) {
        if (dialogClosed) {
            SettingsFragmentInstanceManager.get().dispatchHandleNumberChanged(id, activity, Arrays.asList(newValue), global);
        }
    }

    public final Mode getMode() {
        return mMode;
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

    public final Integer getUnitRes() {
        return mUnitRes;
    }

    public final boolean getUseDpSizeDialog() {
        return mUseDpSizeDialog;
    }
}
