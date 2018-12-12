package com.michaelflisar.recyclerviewpreferences.settings;

import android.app.Activity;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.base.SettingsText;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.TextSettingItem;
import com.michaelflisar.recyclerviewpreferences.fragments.TextSettingsDialogFragment;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.iconics.typeface.IIcon;

/**
 * Created by flisar on 16.05.2017.
 */

public class TextDialogSetting<
        CLASS,
        SettData extends ISettData<String, CLASS, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<String, CLASS, SettData, VH>>
        extends BaseSetting<String, CLASS, SettData, VH> {

    private boolean mAllowEmptyInput = false;

    public TextDialogSetting(Class<CLASS> clazz, SettData settData, int title, IIcon icon) {
        this(clazz, settData, new SettingsText(title), icon);
    }

    public TextDialogSetting(Class<CLASS> clazz, SettData settData, String title, IIcon icon) {
        this(clazz, settData, new SettingsText(title), icon);
    }

    private TextDialogSetting(Class<CLASS> clazz, SettData settData, SettingsText title, IIcon icon) {
        super(clazz, settData, title, icon);
    }

    public TextDialogSetting withAllowEmptyInput(boolean allowEmptyText) {
        mAllowEmptyInput = allowEmptyText;
        return this;
    }

    @Override
    public void updateValueView(boolean topView, VH vh, View v, SettData settData, boolean global, ISettCallback callback) {
        String text = getValue((CLASS)callback.getCustomSettingsObject(), global);
        ((TextView) v).setText(text);
    }

    @Override
    public void bind(VH vh) {
    }

    @Override
    public void unbind(VH vh) {
    }

    @Override
    public void onShowChangeSetting(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject) {
        TextSettingsDialogFragment.Companion.create(
                getSettingId(),
                global,
                getValue(customSettingsObject, global),
                getTitle().getText(),
                mAllowEmptyInput
        )
                .show(((FragmentActivity) activity).getSupportFragmentManager(), null);
    }

    @Override
    public final int getLayoutTypeId() {
        return R.id.id_adapter_setting_text_item;
    }

    @Override
    public final int getLayout() {
        return R.layout.adapter_base_setting_item;
    }

    @Override
    public <P extends IItem & IExpandable> BaseSettingsItem<P, String, CLASS, SettData, ?> createItem(boolean global, boolean compact, ISettCallback settingsCallback, boolean flatStyle) {
        return new TextSettingItem(global, compact, this, settingsCallback, flatStyle);
    }

    @Override
    public void updateView(int id, Activity activity, boolean global, String newValue, boolean dialogClosed, Object event) {
        if (dialogClosed) {
            SettingsManager.get().dispatchTextChanged(id, activity, newValue, global);
        }
    }

}
