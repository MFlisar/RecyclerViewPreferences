package com.michaelflisar.recyclerviewpreferences.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.michaelflisar.bundlebuilder.Arg;
import com.michaelflisar.bundlebuilder.BundleBuilder;
import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InfoSettingsDialogFragmentBundleBuilder.inject(getArguments(), this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (isHtml) {
            WebView view = new WebView(getActivity());
            String color = "";
            if (SettingsManager.get().getState().isDarkTheme()) {
                color = " color: white;";
            }
            String t = "<html><head>" +
                    "<style type=\"text/css\">"
                    + "body { font-size: 12pt;" + color + " margin: 0px; background-color: transparent; }"
                    + "h1 { margin-left: 0px; font-size: 14pt; text-decoration: underline; font-weight: bold; }"
                    + "h2 { margin-left: 0px; font-size: 13pt; font-weight: bold; }"
                    + "p { font-size: 12pt; }"
                    + "h3 { font-size: 11pt; font-weight: normal;}"
                    + "h4 { font-size: 10pt; font-weight: normal;}"
                    + "code { font-size: 10pt; }"
                    + "li { margin-left: 0px; font-size: 12pt;}"
                    + "ul { padding-left: 30px;}"
                    + "ol { padding-left: 30px;}"
                    + "</style>"
                    + "</head><body>";
            t += text;
            t += "</body></html>";

            view.loadData(t, "text/html; charset=UTF-8", null);
            view.setBackgroundColor(Color.TRANSPARENT);

            MaterialDialog dlg = new MaterialDialog.Builder(getActivity())
                    .title(R.string.info)
                    .customView(view, false)
                    .positiveText(android.R.string.ok)
                    .show();

            int horizontalMargin = (int)getActivity().getResources().getDimension(R.dimen.md_dialog_frame_margin);
            ((ViewGroup.MarginLayoutParams)view.getLayoutParams()).leftMargin = horizontalMargin;
            ((ViewGroup.MarginLayoutParams)view.getLayoutParams()).rightMargin = horizontalMargin;

            return dlg;
        } else {
            MaterialDialog dlg = new MaterialDialog.Builder(getActivity())
                    .title(R.string.info)
                    .content(text)
                    .positiveText(android.R.string.ok)
                    .show();
            return dlg;
        }
    }
}
