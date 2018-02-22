package com.michaelflisar.recyclerviewpreferences.settings;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.databinding.ViewStubProxy;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.base.SettingsText;
import com.michaelflisar.recyclerviewpreferences.databinding.AdapterSettingItemCustomViewBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.CustomViewSettingItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.iconics.typeface.IIcon;

/**
 * Created by flisar on 16.05.2017.
 */

public abstract class BaseCustomViewSetting<CLASS, Value, SettData extends ISettData<Value, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder &
        ISettingsViewHolder<Value, CLASS, SettData, VH>> extends BaseSetting<Value, CLASS, SettData, VH> {

    public BaseCustomViewSetting(Class<CLASS> clazz, SettData settData, int title, IIcon icon) {
        this(clazz, settData, new SettingsText(title), icon);
    }

    public BaseCustomViewSetting(Class<CLASS> clazz, SettData settData, String title, IIcon icon) {
        this(clazz, settData, new SettingsText(title), icon);
    }

    private BaseCustomViewSetting(Class<CLASS> clazz, SettData settData, SettingsText title, IIcon icon) {
        super(clazz, settData, title, icon);
    }

    @Override
    public void updateValueView(boolean topView, VH vh, View v, SettData settData, boolean global, ISettCallback callback) {
        setDisplayedValue(topView, v, settData, global, (CLASS) callback.getCustomSettingsObject());
    }

    protected abstract void setDisplayedValue(boolean topView, View v, SettData settData, boolean global, CLASS customSettingsObject);

    protected abstract int getCustomView(boolean topValue);

    @Override
    public void bind(VH vh) {
    }

    @Override
    public void unbind(VH vh) {
    }

    @Override
    public final void onShowChangeSetting(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject) {
        showCustomDialog(vh, activity, binding, settData, global, customSettingsObject);
    }

    protected abstract void showCustomDialog(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject);

//    @Override
//    public final int getLayoutTypeId() {
//        return R.id.id_adapter_setting_text_item;
//    }

    @Override
    public final int getLayout() {
        return R.layout.adapter_setting_item_custom_view;
    }

    @Override
    public final void onLayoutReady(VH vh) {
        ViewStubProxy proxyTop = ((AdapterSettingItemCustomViewBinding) vh.getBinding()).vValueTop;
        ViewStubProxy proxyBottom = ((AdapterSettingItemCustomViewBinding) vh.getBinding()).vValueBottom;

        if (proxyTop.getViewStub() != null && !proxyTop.isInflated()) {
            proxyTop.getViewStub().setLayoutResource(getCustomView(true));
            View view = proxyTop.getViewStub().inflate();
            onLayoutStubReady(view, true);
        } else {
            onLayoutStubReady(proxyTop.getRoot(), true);
        }
        if (proxyBottom.getViewStub() != null && !proxyBottom.isInflated()) {
            proxyBottom.getViewStub().setLayoutResource(getCustomView(false));
            View view = proxyBottom.getViewStub().inflate();
            onLayoutStubReady(view, false);
        } else {
            onLayoutStubReady(proxyBottom.getRoot(), false);
        }
    }

    protected void onLayoutStubReady(View view, boolean top) {

    }

    @Override
    public <P extends IItem & IExpandable> BaseSettingsItem<P, Value, CLASS, SettData, ?> createItem(boolean global, boolean compact, ISettCallback settingsCallback,
            boolean withBottomDivider) {
        return new CustomViewSettingItem(global, compact, this, settingsCallback, withBottomDivider);
    }

    @Override
    public void updateView(int id, Activity activity, boolean global, Value newValue, boolean dialogClosed, Object event) {
        if (dialogClosed) {
            SettingsManager.get().dispatchCustomDialogEvent(id, activity, newValue, global);
        }
    }

    public interface BaseCustomViewHolder<VDB extends ViewDataBinding> {

    }

}
