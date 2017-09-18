package com.michaelflisar.recyclerviewpreferences.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.michaelflisar.bundlebuilder.Arg;
import com.michaelflisar.bundlebuilder.BundleBuilder;
import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;

import java.lang.reflect.Field;

import icepick.Icepick;
import icepick.State;

/**
 * Created by flisar on 22.08.2017.
 */

@BundleBuilder(setterPrefix = "with", useConstructorForMandatoryArgs = true, generateGetters = true)
public class ColorSettingsDialogFragment extends ColorChooserDialog {

    @Arg
    Integer id;
    @Arg
    Boolean global;
    @Arg
    Integer color;
    @Arg
    String title;

    @State
    Integer lastValue;

    private ColorChooserDialog.ColorCallback mColorCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        ColorSettingsDialogFragmentBundleBuilder.inject(getArguments(), this);

        if (lastValue != null) {
            color = lastValue;
        } else {
            lastValue = color;
        }

        // Override builder in args
        int t = R.string.color; // TODO: Title
        ColorChooserDialog.Builder builder = new ColorChooserDialog.Builder(getActivity(), t)
                .allowUserColorInputAlpha(true)
                .customButton(R.string.md_custom_label)
                .presetsButton(R.string.md_presets_label)
                .preselect(color)
                .titleSub(t);
        getArguments().putSerializable("builder", builder);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onAttach(Context context) {
        try {
            super.onAttach(context);
        } catch (IllegalStateException e) {
            // expected, can be skipped
        }
        mColorCallback = new ColorCallback() {
            @Override
            public void onColorSelection(@NonNull ColorChooserDialog colorChooserDialog, @ColorInt int color) {
                SettingsManager.get().dispatchColorSelected(id, getActivity(), color, global);
            }

            @Override
            public void onColorChooserDismissed(@NonNull ColorChooserDialog colorChooserDialog) {

            }
        };

        // 1) Override callback via reflection
        try {
            Field callbackField = ColorChooserDialog.class.getDeclaredField("callback");
            callbackField.setAccessible(true);
            callbackField.set(this, mColorCallback);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException("ColorSettingsDialogFragment can't be created, because parent class callback field could not be set!");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("ColorSettingsDialogFragment can't be created, because parent class callback field could not be set!");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mColorCallback = null;
    }
}
