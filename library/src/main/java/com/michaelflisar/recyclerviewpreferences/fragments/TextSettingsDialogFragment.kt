package com.michaelflisar.recyclerviewpreferences.fragments

import android.R
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.DialogFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.input
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver
import com.michaelflisar.recyclerviewpreferences.SettingsManager

/**
 * Created by flisar on 22.08.2017.
 */

class TextSettingsDialogFragment : DialogFragment() {

    companion object {
        fun create(id: Int, global: Boolean, value: String, title: String, allowEmptyInput: Boolean = true): TextSettingsDialogFragment {
            val args = Bundle()
            args.putInt("id", id)
            args.putBoolean("global", global)
            args.putString("value", value)
            args.putString("title", title)
            args.putBoolean("allowEmptyInput", allowEmptyInput)
            val dlg = TextSettingsDialogFragment()
            dlg.arguments = args
            return dlg

        }
    }

    internal var id: Int? = null
    internal var global: Boolean = false
    internal lateinit var value: String
    internal lateinit var title: String
    internal var allowEmptyInput: Boolean = true

    @State
    var lastValue: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StateSaver.restoreInstanceState(this, savedInstanceState)

        id = arguments!!.getInt("id")
        global = arguments!!.getBoolean("global")
        value = arguments!!.getString("value")!!
        title = arguments!!.getString("title")!!
        allowEmptyInput = arguments!!.getBoolean("allowEmptyInput")

        if (lastValue != null) {
            value = lastValue!!
        } else {
            lastValue = value
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        StateSaver.saveInstanceState(this, outState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialDialog(activity!!)
                .title(text = title)
                .input(waitForPositiveButton = false, prefill = value, inputType = InputType.TYPE_CLASS_TEXT) { materialDialog, charSequence ->
                    lastValue = charSequence.toString()
                    val valid = !allowEmptyInput || lastValue!!.length > 0
                    materialDialog.setActionButtonEnabled(WhichButton.POSITIVE, valid)
                }
                .positiveButton(R.string.ok) {
                    SettingsManager.get().dispatchTextChanged(id!!, activity, lastValue, global)
                }
    }
}
