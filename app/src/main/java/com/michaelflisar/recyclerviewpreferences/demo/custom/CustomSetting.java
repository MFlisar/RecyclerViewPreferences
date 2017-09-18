package com.michaelflisar.recyclerviewpreferences.demo.custom;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.SettingsFragment;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.demo.R;
import com.michaelflisar.recyclerviewpreferences.demo.gridpicker.GridPreviewView;
import com.michaelflisar.recyclerviewpreferences.interfaces.IDialogHandler;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.settings.BaseDialogSetting;
import com.michaelflisar.recyclerviewpreferences.utils.DialogUtil;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.mikepenz.iconics.typeface.IIcon;
import com.shawnlin.numberpicker.NumberPicker;

/**
 * Created by flisar on 25.08.2017.
 */

public class CustomSetting {

    // ----------------
    // Data structure
    // ----------------

    public static class Data {
        public int rows;
        public int cols;

        public Data() {
            this.rows = -1;
            this.cols = -1;
        }

        public Data(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
        }

        public String getDisplayValue() {
            return "Grid: " + rows + "x" + cols;
        }
    }

    // ----------------
    // Setting
    // ----------------

    public static class Setting<CLASS, SettData extends ISettData<Data, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder &
            ISettingsViewHolder<Data, CLASS, SettData, VH>> extends
            BaseDialogSetting<CLASS, Data, SettData, VH> {

        public Setting(Class<CLASS> clazz, SettData settData, int title, IIcon icon) {
            super(clazz, settData, title, icon);
        }

        public Setting(Class<CLASS> clazz, SettData settData, String title, IIcon icon) {
            super(clazz, settData, title, icon);
        }

        @Override
        protected String getDisplayValue(SettData settData, boolean global, CLASS customSettingsObject) {
            // return a string that will be displayed in the settings text area
            Data data = getValue(customSettingsObject, global);
            return data.getDisplayValue();
        }

        @Override
        protected void showCustomDialog(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject) {
            // show a dialog; make sure to register a custom dialog handler in the settings setup once!!!
            // this one then is responsible to save a user changed value
            Data data = getValue(customSettingsObject, global);

            View v = LayoutInflater.from(activity).inflate(R.layout.grid_dialog_layout, null, false);
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);

            final NumberPicker column = v.findViewById(R.id.npCols);
            column.setMinValue(2);
            column.setMaxValue(50);
            column.setValue(data.cols);
            column.setWrapSelectorWheel(false);

            final NumberPicker row = v.findViewById(R.id.npRows);
            row.setMinValue(2);
            row.setMaxValue(50);
            row.setValue(data.rows);
            row.setWrapSelectorWheel(false);

            final GridPreviewView grid = v.findViewById(R.id.grid);
            grid.setGrid(data.rows, data.cols);
            column.setOnValueChangedListener((picker, oldVal, newVal) -> grid.setCols(newVal));
            row.setOnValueChangedListener((picker, oldVal, newVal) -> grid.setRows(newVal));

            alert.setTitle(getTitle().getText());
            alert.setView(v);

            alert.setPositiveButton("Ok", (dialog, whichButton) -> {
                Data newData = new Data(row.getValue(), column.getValue());
                SettingsManager.get().dispatchCustomDialogEvent(
                        getSettingId(),
                        activity,
                        newData,
                        global
                );
            });

            alert.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());
            alert.show();

//            new MaterialDialog.Builder(activity)
//                    .title("Input")
//                    .content("Input some custom data in the format \"rows x cols\"")
//                    .inputType(InputType.TYPE_CLASS_TEXT)
//                    .alwaysCallInputCallback()
//                    .input("rows x cols", toInput(data), (dialog, input) -> {
//                        boolean valid = false;
//                        try {
//                            parseInput(input);
//                            valid = true;
//                        } catch (Exception e) {
//                        }
//                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(valid);
//                    })
//                    .onPositive((materialDialog, dialogAction) -> {
//                        Data newData = parseInput(materialDialog.getInputEditText().getText());
//                        SettingsFragmentInstanceManager.get().dispatchHandleCustomDialogEvent(
//                                getSettingId(),
//                                activity,
//                                newData,
//                                global
//                        );
//                    }).show();
        }
    }

    // ----------------
    // Dialog handlers
    // ----------------

    public static class DialogHandler implements IDialogHandler<Data> {

        @Override
        public Class<Data> getHandledClass() {
            return Data.class;
        }

        @Override
        public <CLASS,
                SD extends ISettData<Data, CLASS, SD, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Data, CLASS, SD, VH>>
        boolean handleCustomEvent(com.michaelflisar.recyclerviewpreferences.implementations.DialogHandler.DialogType type, SettingsFragment settingsFragment, int id, Activity activity, Data value,
                boolean global, CLASS customSettingsObject) {

            // implement your own method, for the demo we use the default one... this one update the value and assumes that the event is equal to the data type which is the case here
            return DialogUtil.handleCustomEvent(type, settingsFragment, id, activity, value, global, customSettingsObject);
        }
    }
}
