package com.michaelflisar.recyclerviewpreferences.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.InputType;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.michaelflisar.bundlebuilder.Arg;
import com.michaelflisar.bundlebuilder.BundleBuilder;
import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.utils.SettingsFragmentInstanceManager;

import java.util.Arrays;

import icepick.Icepick;
import icepick.State;

/**
 * Created by flisar on 22.08.2017.
 */

@BundleBuilder(setterPrefix = "with", useConstructorForMandatoryArgs = true)
public class NumberSettingsDialogFragment extends DialogFragment {

    @Arg
    Integer id;
    @Arg
    Boolean global;
    @Arg
    Integer value;
    @Arg
    Integer min;
    @Arg
    Integer stepSize;
    @Arg
    Integer max;
    @Arg(optional = true)
    Integer unitRes;
    @Arg
    String title;
    @Arg
    Boolean dpSizeDialog;

    @State
    String lastValue = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        NumberSettingsDialogFragmentBundleBuilder.inject(getArguments(), this);

        if (lastValue != null) {
            value = Integer.parseInt(lastValue);
        } else {
            lastValue = value.toString();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        MaterialDialog dlg = new MaterialDialog.Builder(getActivity())
                .title(title)
                .content(getString(R.string.number_dialog_info, min, max, stepSize))
                .input(null, String.valueOf(value), false, (materialDialog, charSequence) -> {
                    String val = charSequence.toString();
                    boolean valid = false;
                    try {
                        int newValue = Integer.parseInt(val);
                        lastValue = val;
                        valid = newValue >= min && newValue <= max && (newValue - min) % stepSize == 0;

                    } catch (Exception e) {

                    }

                    materialDialog.getActionButton(DialogAction.POSITIVE).setEnabled(valid);
                })
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .alwaysCallInputCallback()
                .positiveText(android.R.string.ok)
                .onPositive((materialDialog, dialogAction) -> {
                    int newValue = Integer.parseInt(lastValue);
                    SettingsFragmentInstanceManager.get().dispatchHandleNumberChanged(id, getActivity(), Arrays.asList(newValue), global);
                })
                .show();

        return dlg;
    }
}
