package com.michaelflisar.recyclerviewpreferences.settings;

import android.app.Activity;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.kt.classes.SettingsText;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.ColorSettingItem;
import com.michaelflisar.recyclerviewpreferences.fragments.ColorSettingsDialogFragment;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.iconics.typeface.IIcon;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by flisar on 16.05.2017.
 */

public class ColorSetting<
        CLASS,
        SettData extends ISettData<Integer, CLASS, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Integer, CLASS, SettData, VH>>
        extends BaseSetting<Integer, CLASS, SettData, VH> {

    public ColorSetting(Class<CLASS> clazz, SettData settData, int title, IIcon icon) {
        this(clazz, settData, new SettingsText(title), icon);
    }

    public ColorSetting(Class<CLASS> clazz, SettData settData, String title, IIcon icon) {
        this(clazz, settData, new SettingsText(title), icon);
    }

    private ColorSetting(Class<CLASS> clazz, SettData settData, SettingsText title, IIcon icon) {
        super(clazz, settData, title, icon);
    }

    @Override
    public void updateValueView(boolean topView, VH vh, View v, SettData settData, boolean global, ISettCallback callback) {
        int color = getValue((CLASS) callback.getCustomSettingsObject(), global);
        Util.setCircleColorBackground(v, color, true, SettingsManager.get().getState().isDarkTheme());
    }

    @Override
    public void bind(VH vh) {
    }

    @Override
    public void unbind(VH vh) {
    }

    @Override
    public void onShowChangeSetting(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject) {
        ColorSettingsDialogFragment.Companion.create(
                getSettingId(),
                global,
                getValue(customSettingsObject, global),
                getTitle().toString()
        )
                .show(((FragmentActivity) activity).getSupportFragmentManager(), null);
    }

    @Override
    public final int getLayoutTypeId() {
        return R.id.id_adapter_setting_color_item;
    }

    @Override
    public final int getLayout() {
        return R.layout.adapter_base_setting_item;
    }

    @Override
    public <P extends IItem & IExpandable> BaseSettingsItem<P, Integer, CLASS, SettData, ?> createItem(boolean global, boolean compact, ISettCallback settingsCallback,boolean flatStyle) {
        return new ColorSettingItem(global, compact, this, settingsCallback, flatStyle);
    }

    @Override
    public void updateView(int id, Activity activity, boolean global, Integer newValue, boolean dialogClosed, Object event) {
        if (dialogClosed) {
            SettingsManager.get().dispatchColorSelected(id, activity, newValue, global);
        }
    }
}
