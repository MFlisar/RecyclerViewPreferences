package com.michaelflisar.recyclerviewpreferences.classes;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.settings.ColorSetting;
import com.michaelflisar.recyclerviewpreferences.settings.NumberSetting;
import com.michaelflisar.recyclerviewpreferences.settings.todo.MultiNumberSetting;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.List;

public class SettingsFragmentManager {

    private Activity mActivity;
    private FastItemAdapter mAdapter;
    private List<ISetting> mSettings;

    public SettingsFragmentManager(Activity activity, FastItemAdapter adapter, List<ISetting> settings) {
        mActivity = activity;
        mAdapter = adapter;
        mSettings = settings;
    }

    public List<ISetting> getSettings() {
        return mSettings;
    }

//    public boolean updateViews(final Integer id) {
//        if (id != null) {
//            if (mAdapter != null) {
//                int index = Util.indexOf(mAdapter.getAdapterItems(), item -> item instanceof ISettingsItem && ((ISettingsItem) item).getSettings().checkId(id));
//                if (index != -1) {
//                    mAdapter.notifyAdapterItemChanged(index);
//                    return true;
//                }
//            }
//        } else {
////            for (ISetting setting : mSettings)
////                setting.init(mViews.get(setting.getParentId()), settData, global, this, this, this);
//        }
//        return false;
//    }

//    public void handleSettingsChanged(final int id, Activity activity, boolean global, Object customSettingsObject) {
//        ISetting setting = Util.find(mSettings, type -> type.checkId(id));
//        if (setting != null) {
//            setting.onValueChanged(id, activity, global, customSettingsObject);
//        }
//    }

//    public <CLASS, SD extends ISettData<Integer, CLASS, SD, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Integer, CLASS, SD, VH>, SDM extends ISettData<List<Integer>, CLASS, SDM,
//            VHM>, VHM extends RecyclerView.ViewHolder & ISettingsViewHolder<List<Integer>, CLASS, SDM, VHM>> boolean handleNumberChanged(
//            final int id, Activity activity, List<Integer> numbers, boolean global, CLASS customSettingsObject) {
//        ISetting setting = Util.find(mSettings, type -> type.checkId(id));
//
//        if (setting != null) {
//            if (setting instanceof NumberSetting) {
//                if (!setting.getValue(customSettingsObject, global).equals(numbers.get(0))) {
//                    setting.setValue(customSettingsObject, global, numbers.get(0));
//                    setting.onValueChanged(id, activity, global, customSettingsObject);
//                    updateViews(id);
//                    return true;
//                }
//            } else if (setting instanceof MultiNumberSetting) {
//                MultiNumberSetting<CLASS, SDM, VHM> s = (MultiNumberSetting<CLASS, SDM, VHM>) setting;
//                for (int i = 0; i < s.getMultiSettingCount(); i++) {
//                    if (!s.getValue(customSettingsObject, global).get(i).equals(numbers.get(i))) {
//                        s.setValue(customSettingsObject, global, numbers);
//                        s.onValueChanged(id, activity, global, customSettingsObject);
//                        updateViews(id);
//                        return true;
//                    }
//                }
//            } else {
//                throw new RuntimeException("Wrong setting type!");
//            }
//        }
//
//        return false;
//    }
//
//    public <CLASS, SD extends ISettData<String, CLASS, SD, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<String, CLASS, SD, VH>> boolean handleTextChanged(final int id,
//            Activity activity, String value, boolean global, CLASS customSettingsObject) {
//        ISetting setting = Util.find(mSettings, type -> type.checkId(id));
//
//        if (setting != null) {
//            if (!setting.getValue(customSettingsObject, global).equals(value)) {
//                setting.setValue(customSettingsObject, global, value);
//                setting.onValueChanged(id, activity, global, customSettingsObject);
//                updateViews(id);
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public <CLASS, SD extends ISettData<Integer, CLASS, SD, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Integer, CLASS, SD, VH>> boolean handleColorSelected(final int id,
//            Activity activity, int color, boolean global, CLASS customSettingsObject) {
//        ISetting setting = Util.find(mSettings, type -> type.checkId(id));
//
//        if (setting != null) {
//            ColorSetting<CLASS, SD, VH> s = (ColorSetting<CLASS, SD, VH>) setting;
//            if (s.getValue(customSettingsObject, global) != color) {
//                if (s.setValue(customSettingsObject, global, color)) {
//                    s.onValueChanged(id, activity, global, customSettingsObject);
//                }
//                updateViews(id);
//                return true;
//            }
//        }
//
//        return false;
//    }

//    public void handleDialogResult(BaseDialogEvent event, Activity activity) {
//        if (event instanceof ColorChooserDialog.DialogColorEvent) {
//            handleColorSelected(event.id, activity, ((ColorChooserDialog.DialogColorEvent) event).color);
//        } else if (event instanceof DialogNumberIntegerPicker.DialogNumberIntegerPickerEvent) {
//            DialogNumberIntegerPicker.DialogNumberIntegerPickerEvent e = (DialogNumberIntegerPicker.DialogNumberIntegerPickerEvent) event;
//            handleNumberSelected(e.id, activity, e.getValues());
//        } else {
//            if (event.extras != null && event.extras.containsKey(DIALOG_EXTRA)) {
//                int id = event.extras.getInt(DIALOG_EXTRA);
//                ISetting setting = Util.find(mSettings, type -> type.checkId(id));
//                if (setting != null) {
//                    // dzt. nur für Globale Settings, daher kein handleId, sidebarId, folderId
//                    if (setting.updateView(id, activity, event, getSettDataFromParent())) {
//                        updateViews(id);
//                    }
//                }
//            }
//        }
//    }

//    protected boolean handleNumberSelected(int reference, Activity activity, final List<Integer> numbers, boolean global, Object customSettingsObject) {
//        if (NumberPickerSettingUtil.getSidebar(reference) != -1 /*mSidebarIndex*/) {
//            return false;
//        }
//
//        int baseRef = NumberPickerSettingUtil.getBaseRef(reference);
//        final int id = NumberPickerSettingUtil.getIdFromRef(baseRef);
//
//        ISetting setting = Util.find(mSettings, type -> type.checkId(id));
//
//        if (setting != null) {
//            if (handleNumberChanged(id, activity, numbers, global, customSettingsObject)) {
//                updateViews(id);
//                return true;
//            }
//        }
//
//        return false;
//    }

    public <CLASS, SD extends ISettData<?, CLASS, SD, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<?, CLASS, SD, VH>> void dispatchDependencyChanged(final int id,  boolean global, Object customSettingsObject)//, ISetting<?, CLASS, SD, VH> dependencySetting, SD settData, boolean global, CLASS customSettingsObject) {
    {
        for (ISetting setting : mSettings) {
            Dependency dependency = setting.getDependency();
            if (dependency != null && dependency.dependsOn(id))
            {
                boolean enabled = dependency.isEnabled(global, customSettingsObject);
                boolean visible = dependency.isVisible(global, customSettingsObject);
                if (updateViewsDependency(setting.getSettingId(), enabled, visible)) {
                    Log.d(SettingsFragmentManager.class.getSimpleName(), "Setting \"" + setting.getTitle().getText() + "\" dependency changed: enabled = " + enabled + ", visible = " + visible);
                }
            }
        }

    }

    public boolean updateViewsDependency(final Integer id, boolean enabled, boolean visible) {
        if (id != null) {
            if (mAdapter != null) {
                int index = Util.indexOf(mAdapter.getAdapterItems(), item -> item instanceof ISettingsItem && ((ISettingsItem) item).getSettings().checkId(id));
                if (index != -1) {
                    ((ISettingsItem)mAdapter.getAdapterItems().get(index)).setDependencyState(enabled, visible);
                    // TODO: update filter state!!!!
                    mAdapter.notifyAdapterItemChanged(index);
                    return true;
                }
            }
        } else {
//            for (ISetting setting : mSettings)
//                setting.init(mViews.get(setting.getParentId()), settData, global, this, this, this);
        }
        return false;
    }
}
