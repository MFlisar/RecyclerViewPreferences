package com.michaelflisar.recyclerviewpreferences.kt.classes

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.ViewDataBinding

import androidx.recyclerview.widget.RecyclerView
import com.michaelflisar.recyclerviewpreferences.classes.Dependency
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback
import com.michaelflisar.recyclerviewpreferences.kt.enums.SettingType
import com.michaelflisar.recyclerviewpreferences.kt.enums.SupportType
import com.michaelflisar.recyclerviewpreferences.kt.implementations.SharedPreferenceSetting
import com.mikepenz.iconics.typeface.IIcon

interface DataHandler {
    val handledSettingsClass: Class<*>
    fun handles(setting: SettingsItem<*>): Boolean
}

interface GlobalDataHandler : DataHandler {
    fun <Value> getValue(setting: SettingsItem<Value>): Value
    fun <Value> setValue(setting: SettingsItem<Value>, value: Value): Boolean

}

interface CustomDataHandler<Value, Data> : DataHandler {
    fun getCustomValue(data: Data): Value
    fun setCustomValue(data: Data, value: Value): Boolean
    fun getCustomEnabled(data: Data): Boolean
    fun setCustomEnabled(data: Data, enabled: Boolean): Boolean
}

interface SettingsItem<Value> {
    val label: String
    val valueClass: Class<Value>
}

interface SettingsGroup : SettingsItem<Any> {

}

// es sollte ein SettingsObject pro Typ geben, dass:
// - DataHandler implementiert
// - ein CreateView() Funktion bietet
// - alle View Update Funktionen bietet

// es sollte Instanzen von Settings geben, die dann von SettingsObjekten gehandelt werden


// --------------------------------
// umgewandelte alte Interfaces
// --------------------------------

//abstract class Setting<Data> {
//    inner abstract class Setting : BaseSetting<Data, Setting, SettingsData, VH>()
//    inner abstract class SettingsData : ISettData<Data, Setting, SettingsData, VH>
//    inner abstract class VH(itemView: View) : ISettingsViewHolder<Data, Setting, SettingsData, VH>, RecyclerView.ViewHolder(itemView)
//
//    // Idea: Implementierungen der Settings als Objekte kann man dann "Factory" mäßig nutzen????
//    abstract fun createSetting() : BaseSetting<Data, Setting, SettingsData, VH>
//}

abstract class BaseSetting<
        Value,
        CLASS,
        SettData : ISettData<Value, CLASS, SettData, VH>,
        VH : RecyclerView.ViewHolder> :
        ISetting<Value, CLASS, SettData, VH> where VH : ISettingsViewHolder<Value, CLASS, SettData, VH> {
}

interface ISettData<Value, CLASS, SettData : ISettData<Value, CLASS, SettData, VH>, VH : RecyclerView.ViewHolder> where VH : ISettingsViewHolder<Value, CLASS, SettData, VH> {

    val settingsClass: Class<CLASS>
    fun getValue(`object`: CLASS, global: Boolean): Value
    fun setValue(`object`: CLASS, global: Boolean, value: Value): Boolean
    fun getCustomEnabled(`object`: CLASS): Boolean
    fun setCustomEnabled(`object`: CLASS, enabled: Boolean)

    interface IValueChangedListener<CLASS> {
        fun onValueChanged(id: Int, activity: Activity, global: Boolean, customSettingsObject: CLASS)
    }
}

interface ISetting<Value, CLASS, SettData : ISettData<Value, CLASS, SettData, VH>, VH : RecyclerView.ViewHolder> where VH : ISettingsViewHolder<Value, CLASS, SettData, VH> {
    var settingId: Int
    val settData: SettData

    // IDs
    val parentId: Int
    val defaultId: Int
    val customId: Int
    val useCustomId: Int
    val viewHolderId: Int

    // FastAdapter
    val layoutTypeId: Int
    val layout: Int

    // Icon
    val icon: IIcon
    val iconPaddingDp: Int
    val iconColor: Int?

    // Backgorund
    //    Integer getBackgroundTint();

    // Titel
    val title: SettingsText
    val subTitle: SettingsText
    val textColor: Int?
    val titleTextColor: Int?

    // Info
    val info: SettingsText
    val isInfoHtml: Boolean

    // Type
    val supportType: SupportType
    val settingType: SettingType
    val dependencies: List<Dependency>
//    fun <P : IItem<*, *>> createItem(global: Boolean, compact: Boolean, settingsCallback: ISettCallback, flatStyle: Boolean): BaseSettingsItem<P, *, *, SettData, *> where P : IExpandable<*, *>

    // Values
    fun getValue(`object`: CLASS, global: Boolean): Value

    fun setValue(`object`: CLASS, global: Boolean, value: Value): Boolean
    fun getCustomEnabled(`object`: CLASS): Boolean
    fun setCustomEnabled(`object`: CLASS, enabled: Boolean)

    // Events
    fun onValueChanged(id: Int, activity: Activity, global: Boolean, customSettingsObject: CLASS)

    fun updateView(id: Int, activity: Activity, global: Boolean, newValue: Value, dialogClosed: Boolean, event: Any)
    fun handleDialogEvent(id: Int, activity: Activity, global: Boolean, customSettingsObject: CLASS, event: Any)

    // Dependencies
    fun clearDependencies()

    fun addDependency(dependency: Dependency)

    // Functions
    fun checkId(id: Int): Boolean
}

interface ISettingsViewHolder<Value, CLASS, SettData : ISettData<Value, CLASS, SettData, VH>, VH : RecyclerView.ViewHolder> where VH : ISettingsViewHolder<Value, CLASS, SettData, VH> {
    //val binding: AdapterSettingItemBinding

    val cardView: CardView
    val titleTextView: TextView
    val subTitleTextView: TextView
    val topValueContainer: View
    val isUsingGlobalTextView: TextView
    //    ImageView getIconView();
    val innerIconView: ImageView
    val infoButton: View

    val valueTopView: View
    val valueBottomView: View

    val innerDivider: View
    val row1: View
    val row2: View

    fun onShowChangeSetting(vh: VH, data: BaseSetting<Value, CLASS, SettData, VH>, activity: Activity, binding: ViewDataBinding, settData: SettData, global: Boolean, customSettingsObject: CLASS)
    fun updateValues(data: BaseSetting<Value, CLASS, SettData, VH>, globalSetting: Boolean, callback: ISettCallback)
    fun onUpdateCustomViewDependencies(hasIcon: Boolean, globalSetting: Boolean)
    fun <T : ViewDataBinding> getTopBinding(): T
    fun <T : ViewDataBinding> getBottomBinding(): T
    fun updateCompactMode(globalSetting: Boolean, compact: Boolean)
    fun updateIcon(data: BaseSetting<Value, CLASS, SettData, VH>, globalSetting: Boolean)
    fun updateState(enabled: Boolean, visible: Boolean)
    fun bind(data: BaseSetting<Value, CLASS, SettData, VH>, globalSetting: Boolean)
    fun unbind(data: BaseSetting<Value, CLASS, SettData, VH>)

    fun hideCustomSwitches()
    fun getUseCustomSwitch(hasIcon: Boolean): View
    fun setUseCustomSwitchChecked(hasIcon: Boolean, checked: Boolean)
    fun getIsUseCustomSwitchChecked(hasIcon: Boolean): Boolean
    fun setCustomSwitchListeners(listener: ICustomSwitchListener)

    interface ICustomSwitchListener {
        fun onSwitchChanged(view: View, checked: Boolean)
    }
}