package com.michaelflisar.recyclerviewpreferences.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.DialogFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.michaelflisar.recyclerviewpreferences.R
import com.michaelflisar.recyclerviewpreferences.SettingsManager

/**
 * Created by flisar on 22.08.2017.
 */

class InfoSettingsDialogFragment : DialogFragment() {

    companion object {

        fun create(isHtml: Boolean, text: String): InfoSettingsDialogFragment {
            val args = Bundle()
            args.putBoolean("isHtml", isHtml)
            args.putString("text", text)
            val dlg = InfoSettingsDialogFragment()
            dlg.arguments = args
            return dlg
        }
    }

    internal var isHtml: Boolean? = null
    internal var text: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        InfoSettingsDialogFragmentBundleBuilder.inject(arguments, this)
        isHtml = arguments!!.getBoolean("isHtml")
        text = arguments!!.getString("text")

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (isHtml!!) {
            val view = WebView(activity)
            var color = ""
            if (SettingsManager.get().state.isDarkTheme) {
                color = " color: white;"
            }
            var t = ("<html><head>" +
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
                    + "</head><body>")
            t += text
            t += "</body></html>"

            view.loadData(t, "text/html; charset=UTF-8", null)
            view.setBackgroundColor(Color.TRANSPARENT)

            val dlg = MaterialDialog(activity!!)
                    .title(R.string.info, null)
                    .customView(view = view, scrollable = false)
                    .positiveButton(android.R.string.ok)

            val horizontalMargin = activity!!.resources.getDimension(R.dimen.md_dialog_frame_margin_horizontal).toInt()
            (view.layoutParams as ViewGroup.MarginLayoutParams).leftMargin = horizontalMargin
            (view.layoutParams as ViewGroup.MarginLayoutParams).rightMargin = horizontalMargin

            return dlg
        } else {
            return MaterialDialog(activity!!)
                    .title(R.string.info, null)
                    .message(null, text)
                    .positiveButton(android.R.string.ok)
        }
    }
}
