package com.michaelflisar.recyclerviewpreferences.settings;

import android.app.Activity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.kt.classes.SettingsText;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewNumberTopBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.NumberSettingItem;
import com.michaelflisar.recyclerviewpreferences.fragments.NumberSettingsDialogFragment;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.iconics.typeface.IIcon;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by flisar on 16.05.2017.
 */

public class NumberSetting<
        CLASS,
        SettData extends ISettData<Integer, CLASS, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Integer, CLASS, SettData, VH>>
        extends BaseSetting<Integer, CLASS, SettData, VH> {

    public enum Mode {
        SeekbarAndDialogInput,
        Seekbar,
        DialogInput,
        DialogSeekbar;

        public int getSeekbarVisibility() {
            return (this == DialogInput || this == DialogSeekbar) ? View.GONE : View.VISIBLE;
        }

        public boolean getSupportsDialog() {
            return this != Seekbar;
        }

        public NumberSettingsDialogFragment.Type getNumberPickerType() {
            switch (this) {
                case SeekbarAndDialogInput:
                case DialogInput:
                    return NumberSettingsDialogFragment.Type.Input;
                case Seekbar:
                    break;
                case DialogSeekbar:
                    return NumberSettingsDialogFragment.Type.Seekbar;
            }
            return null;
        }
    }

    private Mode mMode;
    private int mMin;
    private int mMax;
    private int mStepSize;
    private Integer mUnitRes;

    public NumberSetting(Class<CLASS> clazz, SettData settData, int title, IIcon icon, Mode mode, int min, int max, int stepSize, Integer unitRes) {
        this(clazz, settData, new SettingsText(title), icon, mode, min, max, stepSize, unitRes);
    }

    public NumberSetting(Class<CLASS> clazz, SettData settData, String title, IIcon icon, Mode mode, int min, int max, int stepSize, Integer unitRes) {
        this(clazz, settData, new SettingsText(title), icon, mode, min, max, stepSize, unitRes);
    }

    private NumberSetting(Class<CLASS> clazz, SettData settData, SettingsText title, IIcon icon, Mode mode, int min, int max, int stepSize, Integer unitRes) {
        super(clazz, settData, title, icon);
        mMode = mode;
        mMin = min;
        mMax = max;
        mStepSize = stepSize;
        mUnitRes = unitRes;
    }

    @Override
    public void updateValueView(boolean topView, VH vh, View v, SettData settData, boolean global, ISettCallback callback) {
        String text = mUnitRes == null ? String.valueOf(getValue((CLASS) callback.getCustomSettingsObject(), global)) : SettingsManager.get().getContext().getString(mUnitRes,
                getValue((CLASS) callback.getCustomSettingsObject(), global));
        ((TextView) v).setText(text);

        if (topView) {
            int seekbarValue = (getValue((CLASS) callback.getCustomSettingsObject(), global) - getMin()) / getStepSize();
            ((ViewNumberTopBinding) vh.getTopBinding()).sbTop.setProgress(seekbarValue);
        }
    }

    @Override
    public void bind(VH vh) {
        SeekBar seekBar = ((ViewNumberTopBinding) vh.getTopBinding()).sbTop;
        seekBar.setMax(mMax - mMin);
    }

    @Override
    public void unbind(VH vh) {
    }

    @Override
    public void onShowChangeSetting(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject) {
        if (mMode.getSupportsDialog()) {
            SettingsManager.get().getDialogHandler().showNumberPicker(activity, mMode, this, global, customSettingsObject);
        }
    }

    @Override
    public final int getLayoutTypeId() {
        return R.id.id_adapter_setting_number_item;
    }

    @Override
    public final int getLayout() {
        return R.layout.adapter_base_setting_item;
    }

    @Override
    public <P extends IItem & IExpandable> BaseSettingsItem<P, Integer, CLASS, SettData, ?> createItem(boolean global, boolean compact, ISettCallback settingsCallback,  boolean flatStyle) {
        return new NumberSettingItem(global, compact, this, settingsCallback, flatStyle);
    }

    @Override
    public void updateView(int id, Activity activity, boolean global, Integer newValue, boolean dialogClosed, Object event) {
        if (dialogClosed) {
            SettingsManager.get().dispatchNumberChanged(id, activity, newValue, global);
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
}
