package com.michaelflisar.recyclerviewpreferences.kt.implementations

import android.content.SharedPreferences
import com.michaelflisar.recyclerviewpreferences.kt.classes.GlobalDataHandler
import com.michaelflisar.recyclerviewpreferences.kt.classes.SettingsItem

class SharedPreferenceGlobalDataHandler(
        val prefs: SharedPreferences
) : GlobalDataHandler {

    override val handledSettingsClass: Class<*> = SharedPreferenceSetting::class.java

    override fun handles(setting: SettingsItem<*>): Boolean {
        val setting = setting as SharedPreferenceSetting<*>
        return setting.valueClass == Int::class.java ||
                setting.valueClass == Boolean::class.java ||
                setting.valueClass == String::class.java
    }

    override fun <Value> getValue(setting: SettingsItem<Value>): Value {
        val setting = setting as SharedPreferenceSetting<Value>
        return if (setting.valueClass == Int::class.java) {
            prefs.getInt(setting.key, (setting.defaultValue as Int?) ?: 0) as Value
        } else if (setting.valueClass == Boolean::class.java) {
            prefs.getBoolean(setting.key, (setting.defaultValue as Boolean?) ?: false) as Value
        } else if (setting.valueClass == String::class.java) {
            prefs.getString(setting.key, (setting.defaultValue as String?) ?: "") as Value
        } else {
            // never happens
            throw RuntimeException("Setting $setting not supported!")
        }
    }

    override fun <Value> setValue(setting: SettingsItem<Value>, value: Value): Boolean {
        val setting = setting as SharedPreferenceSetting<Value>
        val editor = if (setting.valueClass == Int::class.java) {
            prefs.edit().putInt(setting.key, value as Int)
        } else if (setting.valueClass == Boolean::class.java) {
            prefs.edit().putBoolean(setting.key, value as Boolean)
        } else if (setting.valueClass == String::class.java) {
            prefs.edit().putString(setting.key, value as String)
        } else {
            // never happens
            throw RuntimeException("Setting $setting not supported!")
        }
        return editor.commit()
    }
}