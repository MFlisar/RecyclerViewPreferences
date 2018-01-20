package com.michaelflisar.recyclerviewpreferences.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.michaelflisar.bundlebuilder.Arg;
import com.michaelflisar.bundlebuilder.BundleBuilder;
import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;

import icepick.Icepick;
import icepick.State;

/**
 * Created by flisar on 22.08.2017.
 */

@BundleBuilder(setterPrefix = "with", useConstructorForMandatoryArgs = true)
public class NumberSettingsDialogFragment extends DialogFragment {

    public enum Type {
        Input,
        Seekbar
    }

    @Arg
    Type type;
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
    @Arg
    String title;
    @Arg(optional = true)
    Integer unitRes;

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

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title(title)
                .positiveText(android.R.string.ok)
                .onPositive((materialDialog, dialogAction) -> {
                    switch (type) {
                        case Input: {
                            Integer validInput = getValidInput(materialDialog.getInputEditText().getText());
                            if (validInput != null) {
                                int newValue = Integer.parseInt(lastValue);
                                SettingsManager.get().dispatchNumberChanged(id, getActivity(), newValue, global);
                                dismiss();
                            } else {
                                if (stepSize == 1) {
                                    Snackbar.make(materialDialog.getView(), getString(R.string.number_dialog_info_no_steps, min, max), Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(materialDialog.getView(), getString(R.string.number_dialog_info, min, max, stepSize), Snackbar.LENGTH_SHORT).show();
                                }
                            }
                            break;
                        }
                        case Seekbar: {
                            int newValue = min + Integer.parseInt(lastValue) * stepSize;
                            SettingsManager.get().dispatchNumberChanged(id, getActivity(), newValue, global);
                            dismiss();
                            break;
                        }
                    }
                })
                .autoDismiss(false);

        switch (type) {
            case Input:
                builder
                        //.content(getString(R.string.number_dialog_info, min, max, stepSize))
                        .input(null, String.valueOf(value), false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog materialDialog, CharSequence charSequence) {
                                getValidInput(charSequence);
                            }
                        })
                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .alwaysCallInputCallback();
                break;
            case Seekbar:
                builder.customView(R.layout.dialog_seekbar, false);
                break;
        }

        MaterialDialog dlg = builder.show();

        if (type == Type.Input) {
            dlg.getInputEditText().setSelectAllOnFocus(true);
        } else {
            SeekBar seekbar = dlg.getCustomView().findViewById(R.id.seekBar);
            seekbar.setMax((max - min) / stepSize);
            seekbar.setProgress((value - min) / stepSize);
            TextView textView = dlg.getCustomView().findViewById(R.id.textView);
            updateProgressDisplay(seekbar, textView);
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    lastValue = String.valueOf(progress);
                    updateProgressDisplay(seekBar, textView);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        return dlg;
    }

    private void updateProgressDisplay(SeekBar seekBar, TextView tv) {
        int value = min + seekBar.getProgress() * stepSize;
        if (unitRes != null) {
            tv.setText(getString(unitRes, value));
        } else {
            tv.setText(String.valueOf(value));
        }
    }

    private Integer getValidInput(CharSequence charSequence) {
        String val = charSequence.toString();
        try {
            int newValue = Integer.parseInt(val);
            lastValue = val;
            boolean valid = newValue >= min && newValue <= max && (newValue - min) % stepSize == 0;
            if (valid) {
                return newValue;
            }

        } catch (Exception e) {

        }
        return null;
    }
}
