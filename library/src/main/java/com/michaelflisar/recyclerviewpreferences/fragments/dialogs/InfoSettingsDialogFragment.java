package com.michaelflisar.recyclerviewpreferences.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Html;

import com.afollestad.materialdialogs.MaterialDialog;
import com.michaelflisar.bundlebuilder.Arg;
import com.michaelflisar.bundlebuilder.BundleBuilder;
import com.michaelflisar.recyclerviewpreferences.R;

/**
 * Created by flisar on 22.08.2017.
 */

@BundleBuilder(setterPrefix = "with", useConstructorForMandatoryArgs = true)
public class InfoSettingsDialogFragment extends DialogFragment {

    @Arg
    Boolean isHtml;
    @Arg
    String text;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        InfoSettingsDialogFragmentBundleBuilder.inject(getArguments(), this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String content = text;
        if (isHtml) {
            content = Html.fromHtml(text).toString().replace("\n", "<br/>");
        }
        MaterialDialog dlg = new MaterialDialog.Builder(getActivity())
                .title(R.string.info)
                .content(content)
                .positiveText(android.R.string.ok)
                .show();
        return dlg;
    }
}
