package com.michaelflisar.recyclerviewpreferences.kt.implementations

import com.michaelflisar.recyclerviewpreferences.kt.classes.SettingsItem

class SharedPreferenceSetting<Value>(
        override val valueClass: Class<Value>,
        override val label: String,
        val key: String,
        val defaultValue: Value? = null
) : SettingsItem<Value> {
}