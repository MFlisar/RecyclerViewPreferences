package com.michaelflisar.recyclerviewpreferences.settings;

import android.app.Activity;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.kt.classes.SettingsText;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewCustomBottomBinding;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewCustomTopBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.CustomViewSettingItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.iconics.typeface.IIcon;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by flisar on 16.05.2017.
 */

public abstract class CustomSetting<
        CLASS,
        Value,
        SettData extends ISettData<Value, CLASS, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Value, CLASS, SettData, VH>>
        extends BaseSetting<Value, CLASS, SettData, VH> {

    public CustomSetting(Class<CLASS> clazz, SettData settData, int title, IIcon icon) {
        this(clazz, settData, new SettingsText(title), icon);
    }

    public CustomSetting(Class<CLASS> clazz, SettData settData, String title, IIcon icon) {
        this(clazz, settData, new SettingsText(title), icon);
    }

    private CustomSetting(Class<CLASS> clazz, SettData settData, SettingsText title, IIcon icon) {
        super(clazz, settData, title, icon);
    }

    @Override
    public void updateValueView(boolean topView, VH vh, View v, SettData settData, boolean global, ISettCallback callback) {
        setDisplayedValue(topView, vh, v, settData, global, (CLASS) callback.getCustomSettingsObject());
    }

    protected abstract void setDisplayedValue(boolean topView, VH vh, View v, SettData settData, boolean global, CLASS customSettingsObject);

    protected abstract int getCustomView(boolean topValue);

    protected abstract void showCustomDialog(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject);

    @Override
    public void bind(VH vh) {

    }

    protected void onCustomViewReady(boolean top) {

    }

    @Override
    public void unbind(VH vh) {
//        if (customBindingTop != null) {
//            customBindingTop.unbind();
//        }
//        if (customBindingBottom != null) {
//            customBindingBottom.unbind();
//        }
    }

    @Override
    public void onShowChangeSetting(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject) {
        showCustomDialog(vh, activity, binding, settData, global, customSettingsObject);
    }

    @Override
    public final int getLayout() {
        return R.layout.adapter_base_setting_item;
    }

    @Override
    public <P extends IItem & IExpandable> BaseSettingsItem<P, Value, CLASS, SettData, ?> createItem(boolean global, boolean compact, ISettCallback settingsCallback, boolean flatStyle) {
        return new CustomViewSettingItem(global, compact, this, settingsCallback, flatStyle);
    }

    @Override
    public void updateView(int id, Activity activity, boolean global, Value newValue, boolean dialogClosed, Object event) {
        if (dialogClosed) {
            SettingsManager.get().dispatchCustomDialogEvent(id, activity, newValue, global);
        }
    }

    @Override
    public void onLayoutReady(VH viewHolder) {
        // we must check our custo view binding every time, but we only inflate them once!
        ViewCustomTopBinding topBinding = viewHolder.getTopBinding();
        ViewCustomBottomBinding bottomBinding = viewHolder.getBottomBinding();

        if (topBinding.vValueTop != null && !topBinding.vValueTop.isInflated()) {
            topBinding.vValueTop.getViewStub().setLayoutResource(getCustomView(true));
            View view = topBinding.vValueTop.getViewStub().inflate();
            onCustomViewReady(true);
        } else {
            onCustomViewReady(true);
        }
        if (bottomBinding.vValueBottom.getViewStub() != null && !bottomBinding.vValueBottom.isInflated()) {
            bottomBinding.vValueBottom.getViewStub().setLayoutResource(getCustomView(false));
            View view = bottomBinding.vValueBottom.getViewStub().inflate();
            onCustomViewReady(false);
        } else {
            onCustomViewReady(false);
        }
    }
}
