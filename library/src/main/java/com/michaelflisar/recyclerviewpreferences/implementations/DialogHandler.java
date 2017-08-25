package com.michaelflisar.recyclerviewpreferences.implementations;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.michaelflisar.recyclerviewpreferences.fragments.SettingsFragment;
import com.michaelflisar.recyclerviewpreferences.interfaces.IDialogHandler;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.settings.ColorSetting;
import com.michaelflisar.recyclerviewpreferences.settings.NumberSetting;
import com.michaelflisar.recyclerviewpreferences.settings.todo.MultiNumberSetting;
import com.michaelflisar.recyclerviewpreferences.utils.Util;

import java.util.HashMap;
import java.util.List;

/**
 * Created by flisar on 25.08.2017.
 */

public class DialogHandler {

    public enum DialogType {
        Number,
        Text,
        Color,
        Custom
    }

    private HashMap<Class, IDialogHandler> mCustomDialogHandlers = new HashMap<>();

    public DialogHandler() {

    }

    public <T> void registerDialogHandler(Class<T> clazz, IDialogHandler<T> handler) {
        mCustomDialogHandlers.put(clazz, handler);
    }

    public <CLASS,
            SD extends ISettData<Integer, CLASS, SD, VH>,
            VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Integer, CLASS, SD, VH>, SDM extends ISettData<List<Integer>, CLASS, SDM, VHM>,
            VHM extends RecyclerView.ViewHolder & ISettingsViewHolder<List<Integer>, CLASS, SDM, VHM>>
    boolean handleDialogResult(SettingsFragment settingsFragment, DialogType type, final int id, Activity activity, Object data, boolean global, CLASS customSettingsObject) {
        switch (type) {
            case Number:
                // TODO: multi number soll als Custom Dialog gehandelt werden, library überall anpassen
                return handleNumberChanged(settingsFragment, id, activity, (List<Integer>)data, global, customSettingsObject);
            case Text:
                return handleTextChanged(settingsFragment, id, activity, (String) data, global, customSettingsObject);
            case Color:
                return handleColorSelected(settingsFragment, id, activity, (Integer) data, global, customSettingsObject);
            case Custom:
                Class clazz = data.getClass();
                IDialogHandler handler = mCustomDialogHandlers.get(clazz);
                if (handler != null){
                    return handler.handleCustomEvent(type, settingsFragment, id, activity, data, global, customSettingsObject);
                }
                break;
        }
        return false;
    }

    protected <CLASS,
            SD extends ISettData<Integer, CLASS, SD, VH>,
            VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Integer, CLASS, SD, VH>, SDM extends ISettData<List<Integer>, CLASS, SDM, VHM>,
            VHM extends RecyclerView.ViewHolder & ISettingsViewHolder<List<Integer>, CLASS, SDM, VHM>>
    boolean handleNumberChanged(SettingsFragment settingsFragment, final int id, Activity activity, List<Integer> numbers, boolean global, CLASS customSettingsObject) {
        ISetting setting = Util.find(settingsFragment.getSettingsManager().getSettings(), sett -> sett.checkId(id));

        if (setting != null) {
            if (setting instanceof NumberSetting) {
                if (!setting.getValue(customSettingsObject, global).equals(numbers.get(0))) {
                    setting.setValue(customSettingsObject, global, numbers.get(0));
                    setting.onValueChanged(id, activity, global, customSettingsObject);
                    settingsFragment.updateViews(id);
                    return true;
                }
            } else if (setting instanceof MultiNumberSetting) {
                MultiNumberSetting<CLASS, SDM, VHM> s = (MultiNumberSetting<CLASS, SDM, VHM>) setting;
                for (int i = 0; i < s.getMultiSettingCount(); i++) {
                    if (!s.getValue(customSettingsObject, global).get(i).equals(numbers.get(i))) {
                        s.setValue(customSettingsObject, global, numbers);
                        s.onValueChanged(id, activity, global, customSettingsObject);
                        settingsFragment.updateViews(id);
                        return true;
                    }
                }
            } else {
                throw new RuntimeException("Wrong setting type!");
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
}
