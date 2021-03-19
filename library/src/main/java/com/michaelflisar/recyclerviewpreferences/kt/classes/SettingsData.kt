package com.michaelflisar.recyclerviewpreferences.kt.classes

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting
import com.mikepenz.iconics.typeface.IIcon

class SettingsData(
        var title: Int,
        var icon: IIcon? = null,
        var subTitle: Int = -1,
        var iconPadding: Int = 0,
        var iconColor: Int? = null,
        var titleColor: Int? = null,
        var textColor: Int? = null,
        var infoText: Int = -1,
        var isInfoHtml: Boolean = false,
        var supportType: BaseSetting.SupportType = BaseSetting.SupportType.Normal,
        var settingType: BaseSetting.SettingType = BaseSetting.SettingType.Normal
) {

}