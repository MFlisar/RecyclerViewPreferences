package com.michaelflisar.recyclerviewpreferences.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color.parseColor
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver
import com.michaelflisar.recyclerviewpreferences.R
import com.michaelflisar.recyclerviewpreferences.SettingsManager

/**
 * Created by flisar on 22.08.2017.
 */

class ColorSettingsDialogFragment : DialogFragment() {

    companion object {

        fun create(id: Int, global: Boolean, color: Int, title: String): ColorSettingsDialogFragment {
            val args = Bundle()
            args.putInt("id", id)
            args.putBoolean("global", global)
            args.putInt("color", color)
            args.putString("title", title)
            val dlg = ColorSettingsDialogFragment()
            dlg.arguments = args
            return dlg
        }

        val PRIMARY_COLORS = intArrayOf(
                parseColor("#F44336"), parseColor("#E91E63"), parseColor("#9C27B0"),
                parseColor("#673AB7"), parseColor("#3F51B5"), parseColor("#2196F3"),
                parseColor("#03A9F4"), parseColor("#00BCD4"), parseColor("#009688"),
                parseColor("#4CAF50"), parseColor("#8BC34A"), parseColor("#CDDC39"),
                parseColor("#FFEB3B"), parseColor("#FFC107"), parseColor("#FF9800"),
                parseColor("#FF5722"), parseColor("#795548"), parseColor("#9E9E9E"),
                parseColor("#607D8B")
        )

        val PRIMARY_COLORS_SUB = arrayOf(
                intArrayOf(
                        parseColor("#FFEBEE"), parseColor("#FFCDD2"), parseColor("#EF9A9A"),
                        parseColor("#E57373"), parseColor("#EF5350"), parseColor("#F44336"),
                        parseColor("#E53935"), parseColor("#D32F2F"), parseColor("#C62828"),
                        parseColor("#B71C1C")
                ), intArrayOf(
                parseColor("#FCE4EC"), parseColor("#F8BBD0"), parseColor("#F48FB1"),
                parseColor("#F06292"), parseColor("#EC407A"), parseColor("#E91E63"),
                parseColor("#D81B60"), parseColor("#C2185B"), parseColor("#AD1457"),
                parseColor("#880E4F")
        ), intArrayOf(
                parseColor("#F3E5F5"), parseColor("#E1BEE7"), parseColor("#CE93D8"),
                parseColor("#BA68C8"), parseColor("#AB47BC"), parseColor("#9C27B0"),
                parseColor("#8E24AA"), parseColor("#7B1FA2"), parseColor("#6A1B9A"),
                parseColor("#4A148C")
        ), intArrayOf(
                parseColor("#EDE7F6"), parseColor("#D1C4E9"), parseColor("#B39DDB"),
                parseColor("#9575CD"), parseColor("#7E57C2"), parseColor("#673AB7"),
                parseColor("#5E35B1"), parseColor("#512DA8"), parseColor("#4527A0"),
                parseColor("#311B92")
        ), intArrayOf(
                parseColor("#E8EAF6"), parseColor("#C5CAE9"), parseColor("#9FA8DA"),
                parseColor("#7986CB"), parseColor("#5C6BC0"), parseColor("#3F51B5"),
                parseColor("#3949AB"), parseColor("#303F9F"), parseColor("#283593"),
                parseColor("#1A237E")
        ), intArrayOf(
                parseColor("#E3F2FD"), parseColor("#BBDEFB"), parseColor("#90CAF9"),
                parseColor("#64B5F6"), parseColor("#42A5F5"), parseColor("#2196F3"),
                parseColor("#1E88E5"), parseColor("#1976D2"), parseColor("#1565C0"),
                parseColor("#0D47A1")
        ), intArrayOf(
                parseColor("#E1F5FE"), parseColor("#B3E5FC"), parseColor("#81D4FA"),
                parseColor("#4FC3F7"), parseColor("#29B6F6"), parseColor("#03A9F4"),
                parseColor("#039BE5"), parseColor("#0288D1"), parseColor("#0277BD"),
                parseColor("#01579B")
        ), intArrayOf(
                parseColor("#E0F7FA"), parseColor("#B2EBF2"), parseColor("#80DEEA"),
                parseColor("#4DD0E1"), parseColor("#26C6DA"), parseColor("#00BCD4"),
                parseColor("#00ACC1"), parseColor("#0097A7"), parseColor("#00838F"),
                parseColor("#006064")
        ), intArrayOf(
                parseColor("#E0F2F1"), parseColor("#B2DFDB"), parseColor("#80CBC4"),
                parseColor("#4DB6AC"), parseColor("#26A69A"), parseColor("#009688"),
                parseColor("#00897B"), parseColor("#00796B"), parseColor("#00695C"),
                parseColor("#004D40")
        ), intArrayOf(
                parseColor("#E8F5E9"), parseColor("#C8E6C9"), parseColor("#A5D6A7"),
                parseColor("#81C784"), parseColor("#66BB6A"), parseColor("#4CAF50"),
                parseColor("#43A047"), parseColor("#388E3C"), parseColor("#2E7D32"),
                parseColor("#1B5E20")
        ), intArrayOf(
                parseColor("#F1F8E9"), parseColor("#DCEDC8"), parseColor("#C5E1A5"),
                parseColor("#AED581"), parseColor("#9CCC65"), parseColor("#8BC34A"),
                parseColor("#7CB342"), parseColor("#689F38"), parseColor("#558B2F"),
                parseColor("#33691E")
        ), intArrayOf(
                parseColor("#F9FBE7"), parseColor("#F0F4C3"), parseColor("#E6EE9C"),
                parseColor("#DCE775"), parseColor("#D4E157"), parseColor("#CDDC39"),
                parseColor("#C0CA33"), parseColor("#AFB42B"), parseColor("#9E9D24"),
                parseColor("#827717")
        ), intArrayOf(
                parseColor("#FFFDE7"), parseColor("#FFF9C4"), parseColor("#FFF59D"),
                parseColor("#FFF176"), parseColor("#FFEE58"), parseColor("#FFEB3B"),
                parseColor("#FDD835"), parseColor("#FBC02D"), parseColor("#F9A825"),
                parseColor("#F57F17")
        ), intArrayOf(
                parseColor("#FFF8E1"), parseColor("#FFECB3"), parseColor("#FFE082"),
                parseColor("#FFD54F"), parseColor("#FFCA28"), parseColor("#FFC107"),
                parseColor("#FFB300"), parseColor("#FFA000"), parseColor("#FF8F00"),
                parseColor("#FF6F00")
        ), intArrayOf(
                parseColor("#FFF3E0"), parseColor("#FFE0B2"), parseColor("#FFCC80"),
                parseColor("#FFB74D"), parseColor("#FFA726"), parseColor("#FF9800"),
                parseColor("#FB8C00"), parseColor("#F57C00"), parseColor("#EF6C00"),
                parseColor("#E65100")
        ), intArrayOf(
                parseColor("#FBE9E7"), parseColor("#FFCCBC"), parseColor("#FFAB91"),
                parseColor("#FF8A65"), parseColor("#FF7043"), parseColor("#FF5722"),
                parseColor("#F4511E"), parseColor("#E64A19"), parseColor("#D84315"),
                parseColor("#BF360C")
        ), intArrayOf(
                parseColor("#EFEBE9"), parseColor("#D7CCC8"), parseColor("#BCAAA4"),
                parseColor("#A1887F"), parseColor("#8D6E63"), parseColor("#795548"),
                parseColor("#6D4C41"), parseColor("#5D4037"), parseColor("#4E342E"),
                parseColor("#3E2723")
        ), intArrayOf(
                parseColor("#FAFAFA"), parseColor("#F5F5F5"), parseColor("#EEEEEE"),
                parseColor("#E0E0E0"), parseColor("#BDBDBD"), parseColor("#9E9E9E"),
                parseColor("#757575"), parseColor("#616161"), parseColor("#424242"),
                parseColor("#212121")
        ), intArrayOf(
                parseColor("#ECEFF1"), parseColor("#CFD8DC"), parseColor("#B0BEC5"),
                parseColor("#90A4AE"), parseColor("#78909C"), parseColor("#607D8B"),
                parseColor("#546E7A"), parseColor("#455A64"), parseColor("#37474F"),
                parseColor("#263238")
        )
        )
    }

    internal var id: Int? = null
    internal var global: Boolean? = null
    internal var color: Int? = null
    internal var title: String? = null

    @State
    var lastValue: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StateSaver.restoreInstanceState(this, savedInstanceState)
//        ColorSettingsDialogFragmentBundleBuilder.inject(arguments, this)

        id = arguments!!.getInt("id")
        global = arguments!!.getBoolean("global")
        color = arguments!!.getInt("color")
        title = arguments!!.getString("title")

        if (lastValue != null) {
            color = lastValue
        } else {
            lastValue = color
        }
    }

    @NonNull
    @Override
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Override builder in args
        val t = R.string.color // TODO: Title
        val dialog = MaterialDialog(activity!!)
                // looks better!
                .title(t)
                .colorChooser(
                        PRIMARY_COLORS,
                        PRIMARY_COLORS_SUB,
                        color,
                        allowCustomArgb = true,
                        showAlphaSelector = true) { _: MaterialDialog, selectedColor: Int ->
                    SettingsManager.get().dispatchColorSelected(id!!, activity, selectedColor, global!!)
                }
//                .allowUserColorInputAlpha(true)
//                .customButton(R.string.md_custom_label)
//                .presetsButton(R.string.md_presets_label)
//                .preselect(color)
//                .titleSub(t)
//        arguments!!.putSerializable("builder", builder)

        return dialog
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        StateSaver.saveInstanceState(this, outState)
    }

    override fun onAttach(context: Context?) {
        try {
            super.onAttach(context)
        } catch (e: IllegalStateException) {
            // expected, can be skipped
        }

//        mColorCallback = object : ColorCallback() {
//            fun onColorSelection(colorChooserDialog: ColorChooserDialog, @ColorInt color: Int) {
//                SettingsManager.get().dispatchColorSelected(id!!, activity, color, global!!)
//            }
//
//            fun onColorChooserDismissed(colorChooserDialog: ColorChooserDialog) {
//
//            }
//        }

//        // 1) Override callback via reflection
//        try {
//            val callbackField = ColorChooserDialog::class.java!!.getDeclaredField("callback")
//            callbackField.setAccessible(true)
//            callbackField.set(this, mColorCallback)
//        } catch (e: NoSuchFieldException) {
//            e.printStackTrace()
//            throw RuntimeException("ColorSettingsDialogFragment can't be created, because parent class callback field could not be set!")
//        } catch (e: IllegalAccessException) {
//            e.printStackTrace()
//            throw RuntimeException("ColorSettingsDialogFragment can't be created, because parent class callback field could not be set!")
//        }


    }

//    override fun onDetach() {
//        super.onDetach()
//        mColorCallback = null
//    }
}
