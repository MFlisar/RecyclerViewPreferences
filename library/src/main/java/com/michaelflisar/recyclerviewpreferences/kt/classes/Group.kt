package com.michaelflisar.recyclerviewpreferences.kt.classes

class Group(
        override val label: String
) : SettingsGroup {
    override val valueClass: Class<Any> = Any::class.java
    val items: ArrayList<SettingsItem<*>> = arrayListOf()

    fun add(item: SettingsItem<*>): Group {
        items.add(item)
        return this
    }
}