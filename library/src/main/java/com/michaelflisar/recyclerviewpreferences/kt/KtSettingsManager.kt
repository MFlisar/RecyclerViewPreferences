package com.michaelflisar.recyclerviewpreferences.kt

import com.michaelflisar.recyclerviewpreferences.kt.classes.DataHandler
import com.michaelflisar.recyclerviewpreferences.kt.classes.GlobalDataHandler
import com.michaelflisar.recyclerviewpreferences.kt.classes.SettingsItem
import java.lang.RuntimeException

object KtSettingsManager {
    private val dataHandlers = hashMapOf<Class<*>, DataHandler>()
    private val settings = arrayListOf<SettingsItem<*>>()

    fun registerDataHandler(dataHandler: DataHandler) {
        dataHandlers.put(dataHandler.handledSettingsClass, dataHandler)
    }

    fun registerSetting(setting: SettingsItem<*>) {
        settings.add(setting)
    }

    fun <Value> getGlobalValue(setting: SettingsItem<Value>) : Value{
        val handler =  dataHandlers.get(setting::class.java)
        if (handler is GlobalDataHandler && handler.handles(setting)) {
            return handler.getValue(setting)
        }
        throw RuntimeException("No handler for setting $setting found")
    }

    fun <Value> setGlobalValue(setting: SettingsItem<Value>, value: Value) : Boolean{
        val handler =  dataHandlers.get(setting::class.java)
        if (handler is GlobalDataHandler && handler.handles(setting)) {
            return handler.setValue(setting, value)
        }
        throw RuntimeException("No handler for setting $setting found")
    }
}