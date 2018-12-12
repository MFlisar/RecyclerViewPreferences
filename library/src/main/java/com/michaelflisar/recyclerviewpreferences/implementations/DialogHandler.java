package com.michaelflisar.recyclerviewpreferences.implementations;

import android.app.Activity;

import com.michaelflisar.recyclerviewpreferences.SettingsFragment;
import com.michaelflisar.recyclerviewpreferences.defaults.DefaultCustomDialogHandler;
import com.michaelflisar.recyclerviewpreferences.fragments.NumberSettingsDialogFragment;
import com.michaelflisar.recyclerviewpreferences.interfaces.IDialogHandler;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.settings.ColorSetting;
import com.michaelflisar.recyclerviewpreferences.settings.NumberSetting;
import com.michaelflisar.recyclerviewpreferences.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by flisar on 25.08.2017.
 */

public class DialogHandler {

    private HashMap<Class, List<IDialogHandler>> mCustomDialogHandlers = new HashMap<>();
    private IDialogHandler mDefaultDialogHandler = new DefaultCustomDialogHandler();

    public <T> void registerDialogHandler(Class<T> clazz, IDialogHandler<T> handler) {
        List<IDialogHandler> handlers = mCustomDialogHandlers.get(clazz);
        if (handlers == null) {
            handlers = new ArrayList<>();
            mCustomDialogHandlers.put(clazz, handlers);
        }
        handlers.add(handler);
    }

    public <CLASS,
            SD extends ISettData<Integer, CLASS, SD, VH>,
            VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Integer, CLASS, SD, VH>>
    void showNumberPicker(Activity activity, NumberSetting.Mode mode, NumberSetting<CLASS, SD, VH> setting, boolean global, CLASS customSettingsObject) {
        if (mode.getSupportsDialog()) {
            NumberSettingsDialogFragment.Companion.create(
                    mode.getNumberPickerType(),
                    setting.getSettingId(),
                    global,
                    setting.getValue(customSettingsObject, global),
                    setting.getMin(),
                    setting.getStepSize(),
                    setting.getMax(),
                    setting.getTitle().getText(),
                    setting.getUnitRes()
            )
                    .show(((FragmentActivity) activity).getSupportFragmentManager(), null);
        }
    }

    public final <CLASS,
            SD extends ISettData<Integer, CLASS, SD, VH>,
            VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Integer, CLASS, SD, VH>,
            SDM extends ISettData<List<Integer>, CLASS, SDM, VHM>,
            VHM extends RecyclerView.ViewHolder & ISettingsViewHolder<List<Integer>, CLASS, SDM, VHM>>
    boolean handleDialogResult(SettingsFragment settingsFragment, DialogType type, final int id, Activity activity, Object data, boolean global, CLASS customSettingsObject) {
        switch (type) {
            case Number:
                return handleNumberChanged(settingsFragment, id, activity, (Integer) data, global, customSettingsObject);
            case Text:
                return handleTextChanged(settingsFragment, id, activity, (String) data, global, customSettingsObject);
            case Color:
                return handleColorSelected(settingsFragment, id, activity, (Integer) data, global, customSettingsObject);
            case Custom:
                return handleCustomEvent(settingsFragment, id, activity, data, global, customSettingsObject);
        }
        return false;
    }

    protected <CLASS,
            SD extends ISettData<Integer, CLASS, SD, VH>,
            VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Integer, CLASS, SD, VH>, SDM extends
            ISettData<List<Integer>, CLASS, SDM, VHM>,
            VHM extends RecyclerView.ViewHolder & ISettingsViewHolder<List<Integer>, CLASS, SDM, VHM>>
    boolean handleCustomEvent(SettingsFragment settingsFragment, final int id, Activity activity, Object data, boolean global, CLASS customSettingsObject) {
        ISetting setting = Util.find(settingsFragment.getSettingsManager().getSettings(), sett -> sett.checkId(id));
        if (setting != null) {
            Class clazz = data.getClass();
            List<IDialogHandler> handlers = mCustomDialogHandlers.get(clazz);
            if (handlers != null) {
                for (IDialogHandler handler : handlers) {
                    handler.handleCustomEvent(settingsFragment, setting, id, activity, data, global, customSettingsObject);
                }
            } else {
                return mDefaultDialogHandler.handleCustomEvent(settingsFragment, setting, id, activity, data, global, customSettingsObject);
            }
        }

        return false;
    }

    protected <CLASS,
            SD extends ISettData<Integer, CLASS, SD, VH>,
            VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Integer, CLASS, SD, VH>, SDM extends
            ISettData<List<Integer>, CLASS, SDM, VHM>,
            VHM extends RecyclerView.ViewHolder & ISettingsViewHolder<List<Integer>, CLASS, SDM, VHM>>
    boolean handleNumberChanged(SettingsFragment settingsFragment, final int id, Activity activity, Integer number, boolean global, CLASS customSettingsObject) {
        ISetting setting = Util.find(settingsFragment.getSettingsManager().getSettings(), sett -> sett.checkId(id));

        if (setting != null) {
            if (!setting.getValue(customSettingsObject, global).equals(number)) {
                setting.setValue(customSettingsObject, global, number);
                setting.onValueChanged(id, activity, global, customSettingsObject);
                settingsFragment.updateViews(id);
                return true;
            }
        }

        return false;
    }

    protected <CLASS,
            SD extends ISettData<String, CLASS, SD, VH>,
            VH extends RecyclerView.ViewHolder & ISettingsViewHolder<String, CLASS, SD, VH>>
    boolean handleTextChanged(SettingsFragment settingsFragment, final int id, Activity activity, String value, boolean global, CLASS customSettingsObject) {
        ISetting setting = Util.find(settingsFragment.getSettingsManager().getSettings(), sett -> sett.checkId(id));

        if (setting != null) {
            if (!setting.getValue(customSettingsObject, global).equals(value)) {
                setting.setValue(customSettingsObject, global, value);
                setting.onValueChanged(id, activity, global, customSettingsObject);
                settingsFragment.updateViews(id);
                return true;
            }
        }

        return false;
    }

    protected <CLASS,
            SD extends ISettData<Integer, CLASS, SD, VH>,
            VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Integer, CLASS, SD, VH>>
    boolean handleColorSelected(SettingsFragment settingsFragment, final int id, Activity activity, int color, boolean global, CLASS customSettingsObject) {
        ISetting setting = Util.find(settingsFragment.getSettingsManager().getSettings(), sett -> sett.checkId(id));

        if (setting != null) {
            ColorSetting<CLASS, SD, VH> s = (ColorSetting<CLASS, SD, VH>) setting;
            if (s.getValue(customSettingsObject, global) != color) {
                if (s.setValue(customSettingsObject, global, color)) {
                    s.onValueChanged(id, activity, global, customSettingsObject);
                }
                settingsFragment.updateViews(id);
                return true;
            }
        }

        return false;
    }

    public enum DialogType {
        Number,
        Text,
        Color,
        Custom
    }
}
