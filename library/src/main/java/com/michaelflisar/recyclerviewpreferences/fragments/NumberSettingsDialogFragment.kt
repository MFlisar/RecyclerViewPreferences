package com.michaelflisar.recyclerviewpreferences.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver
import com.google.android.material.snackbar.Snackbar
import com.michaelflisar.recyclerviewpreferences.R
import com.michaelflisar.recyclerviewpreferences.SettingsManager

/**
 * Created by flisar on 22.08.2017.
 */

class NumberSettingsDialogFragment : DialogFragment() {

    companion object {
        fun create(type: NumberSettingsDialogFragment.Type, id: Int, global: Boolean, value: Int, min: Int, stepSize: Int, max: Int, title: String, unitRes: Int? = null): NumberSettingsDialogFragment {
            val args = Bundle()
            args.putInt("type", type.ordinal)
            args.putInt("id", id)
            args.putBoolean("global", global)
            args.putInt("value", value)
            args.putInt("min", min)
            args.putInt("stepSize", stepSize)
            args.putInt("max", max)
            args.putString("title", title)
            if (unitRes != null)
                args.putInt("unitRes", unitRes)
            val dlg = NumberSettingsDialogFragment()
            dlg.arguments = args
            return dlg

        }
    }

    internal lateinit var type: Type
    internal var id: Int = -1
    internal var global: Boolean = false
    internal var value: Int = 0
    internal var min: Int = 0
    internal var stepSize: Int = 1
    internal var max: Int = 0
    internal lateinit var title: String

    internal var unitRes: Int? = null

    @State
    var lastValue: String? = null

    enum class Type {
        Input,
        Seekbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StateSaver.restoreInstanceState(this, savedInstanceState)

        type = Type.values()[arguments!!.getInt("type")]
        id = arguments!!.getInt("id")
        global = arguments!!.getBoolean("global")
        value = arguments!!.getInt("value")
        min = arguments!!.getInt("min")
        stepSize = arguments!!.getInt("stepSize")
        max = arguments!!.getInt("max")
        title = arguments!!.getString("title")!!
        if (arguments!!.containsKey("unitRes"))
            unitRes = arguments!!.getInt("unitRes")

        if (lastValue != null) {
            value = Integer.parseInt(lastValue!!)
        } else {
            lastValue = value.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        StateSaver.saveInstanceState(this, outState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = MaterialDialog(activity!!)
                .title(text = title)
                .positiveButton(android.R.string.ok) {
                    when (type) {
                        NumberSettingsDialogFragment.Type.Input -> {
                            val validInput = getValidInput(it.getInputField()!!.getText())
                            if (validInput != null) {
                                val newValue = Integer.parseInt(lastValue!!)
                                SettingsManager.get().dispatchNumberChanged(id, activity, newValue, global)
                                dismiss()
                            } else {
                                if (stepSize == 1) {
                                    Snackbar.make(it.getInputField()!!.parent as View, getString(R.string.number_dialog_info_no_steps, min, max), Snackbar.LENGTH_SHORT).show()
                                } else {
                                    Snackbar.make(it.getInputField()!!.parent as View, getString(R.string.number_dialog_info, min, max, stepSize), Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        }
                        NumberSettingsDialogFragment.Type.Seekbar -> {
                            val newValue = min + Integer.parseInt(lastValue!!) * stepSize
                            SettingsManager.get().dispatchNumberChanged(id, activity, newValue, global)
                            dismiss()
                        }
                    }
                }
                .noAutoDismiss()

        when (type) {
            NumberSettingsDialogFragment.Type.Input -> dialog
                    //.content(getString(R.string.number_dialog_info, min, max, stepSize))
                    .input(waitForPositiveButton = false, prefill = value.toString(), inputType = InputType.TYPE_CLASS_NUMBER) { materialDialog: MaterialDialog, charSequence: CharSequence ->
                        val valid = charSequence.toString().length > 0
                        materialDialog.setActionButtonEnabled(WhichButton.POSITIVE, valid)
                        getValidInput(charSequence)
                    }
            NumberSettingsDialogFragment.Type.Seekbar -> dialog.customView(R.layout.dialog_seekbar, scrollable = false)
        }

        if (type == Type.Input) {
            dialog.getInputField()!!.setSelectAllOnFocus(true)
        } else {
            val seekbar: SeekBar = dialog.getCustomView()!!.findViewById(R.id.seekBar)
            seekbar.setMax((max - min) / stepSize)
            seekbar.setProgress((value - min) / stepSize)
            val textView: TextView = dialog.getCustomView()!!.findViewById(R.id.textView)
            updateProgressDisplay(seekbar, textView)
            seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    lastValue = progress.toString()
                    updateProgressDisplay(seekBar, textView)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {

                }
            })
        }
        return dialog
    }

    private fun updateProgressDisplay(seekBar: SeekBar, tv: TextView) {
        val value = min + seekBar.progress * stepSize
        if (unitRes != null) {
            tv.text = getString(unitRes!!, value)
        } else {
            tv.text = value.toString()
        }
    }

    private fun getValidInput(charSequence: CharSequence): Int? {
        val v = charSequence.toString()
        try {
            val newValue = Integer.parseInt(v)
            lastValue = v
            val valid = newValue >= min && newValue <= max && (newValue - min) % stepSize == 0
            if (valid) {
                return newValue
            }

        } catch (e: Exception) {

        }

        return null
    }
}
