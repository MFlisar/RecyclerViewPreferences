package com.michaelflisar.recyclerviewpreferences.kt.classes


import android.widget.TextView

import com.michaelflisar.recyclerviewpreferences.SettingsManager

class SettingsText(
        val resText: Int = -1,
        val textString: String? = null
) {
    // TODO: entfernen! nur für java code
    constructor(resText: Int) : this(resText, null)
    constructor(text: String) : this(-1, text)


    val text: String?
        get() = if (resText != -1) {
            SettingsManager.get().context.getString(resText)
        } else {
            textString
        }

    fun display(tv: TextView) {
        if (resText != -1) {
            tv.setText(resText)
        } else {
            tv.text = textString
        }
    }
}
