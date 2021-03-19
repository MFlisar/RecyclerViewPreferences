package com.michaelflisar.recyclerviewpreferences.demo

import android.content.Context
import android.content.SharedPreferences
import com.michaelflisar.recyclerviewpreferences.kt.KtSettingsManager
import com.michaelflisar.recyclerviewpreferences.kt.classes.Group
import com.michaelflisar.recyclerviewpreferences.kt.implementations.SharedPreferenceGlobalDataHandler
import com.michaelflisar.recyclerviewpreferences.kt.implementations.SharedPreferenceSetting

object TestSettingDefinitions {

    enum class ID {
        StringId
    }

    enum class Types {
        Preference,
        PreferenceOrDatabase
    }

    lateinit var prefs: SharedPreferences
    val pseudoDatabaseMap = hashMapOf<String, Any>()

    fun init(context: Context, preferenceName: String) {

        // create a preference instance
        prefs = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

        // --------------------
        // 1) register data handlers
        // ---------------------

        KtSettingsManager.registerDataHandler(SharedPreferenceGlobalDataHandler(prefs))

        // --------------------
        // 2) register edit handlers
        // ---------------------

        // --------------------
        // 3) create settings
        // ---------------------

        // Group 1 - Desktop
        KtSettingsManager.registerSetting(
                Group("Desktop")
                        .add(Group("Layout")
                                .add(SharedPreferenceSetting(Int::class.java, "Rows", "desktop_layout_rows"))
                                .add(SharedPreferenceSetting(Int::class.java, "Cols", "desktop_layout_cols"))
                        )
                        .add(Group("Search")
                                .add(SharedPreferenceSetting(Boolean::class.java, "Enable search", "desktop_search_enabled"))
                        )
        )

        // No group
        KtSettingsManager.registerSetting(SharedPreferenceSetting(Int::class.java, "Icon Size", "icon_size"))

//        // create some shared preference settings
//        settings.add(SharedPreferenceSetting(String::class.java, "string_1", prefs))
//        settings.add(SharedPreferenceSetting(Int::class.java, "int_1", prefs, 25))
//        settings.add(SharedPreferenceSetting(Boolean::class.java, "bool_1", prefs))
//
//        // create some custom settings that use the pseudo database
//        settings.add(PseudoDBOnlySetting(String::class.java, "db_only_string_1", pseudoDatabaseMap, "default value"))
//        settings.add(PseudoDBOnlySetting(Int::class.java, "db_only_int_1", pseudoDatabaseMap, 30))
//
//        // create some custom class settnigs that also support per item settings
//        settings.add(PseudoDBSetting(String::class.java, "db_string_1", pseudoDatabaseMap, "default value"))
//        settings.add(PseudoDBSetting(Int::class.java, "db_int_1", pseudoDatabaseMap, 30))
    }

//    /*
//    * this class saves it's values into a hash map
//    */
//    class PseudoDBOnlySetting<Value>(
//            val valueClass: Class<Value>,
//            val key: String,
//            val dbMap: HashMap<String, Any>,
//            val defaultValue: Value
//    ) : GlobalDataHandler<Value> {
//
//        override fun getValue(): Value = dbMap.get(key) as Value? ?: defaultValue
//        override fun setValue(value: Value): Boolean {
//            dbMap.put(key, value as Any)
//            return true
//        }
//    }
//
//    /*
//     * this class saves global values into shared preferences and custom values into a hash map
//     */
//    class PseudoDBSetting<Value>(
//            val valueClass: Class<Value>,
//            val key: String,
//            val dbMap: HashMap<String, Any>,
//            val defaultValue: Value
//    ) : GlobalDataHandler<Value>, CustomDataHandler<Value, ExampleCustomData<Value>> {
//
//        override fun getValue(): Value = dbMap.get(key) as Value? ?: defaultValue
//        override fun setValue(value: Value): Boolean {
//            dbMap.put(key, value as Any)
//            return true
//        }
//
//        override fun getCustomValue(data: ExampleCustomData<Value>): Value = data.data
//
//        override fun setCustomValue(data: ExampleCustomData<Value>, value: Value): Boolean {
//            data.data = value
//            return true
//        }
//
//        override fun getCustomEnabled(data: ExampleCustomData<Value>): Boolean = data.enabled
//
//        override fun setCustomEnabled(data: ExampleCustomData<Value>, enabled: Boolean): Boolean {
//            data.enabled = enabled
//            return true
//        }
//    }

    class ExampleCustomData<Data>(
            var enabled: Boolean,
            var data: Data
    )
}