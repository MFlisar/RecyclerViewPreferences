package com.michaelflisar.recyclerviewpreferences.fastadapter.settings;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.classes.Dependency;
import com.michaelflisar.recyclerviewpreferences.fastadapter.BaseSettingViewHolder;
import com.michaelflisar.recyclerviewpreferences.fastadapter.hooks.BaseCustomEventHook;
import com.michaelflisar.recyclerviewpreferences.fragments.InfoSettingsDialogFragmentBundleBuilder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.michaelflisar.recyclerviewpreferences.views.FixedSwitch;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.ISubItem;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

/**
 * Created by flisar on 13.03.2017.
 */

public abstract class BaseSettingsItem<Parent extends IItem & IExpandable, Value, CLASS, SettData extends ISettData<Value, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder &
        ISettingsViewHolder<Value, CLASS, SettData, VH>> extends
        AbstractItem<BaseSettingsItem, VH> implements ISubItem<BaseSettingsItem<Parent, Value, CLASS, SettData, VH>, Parent>, ISettingsItem {
    // ------------------
    // Item
    // ------------------

    protected boolean mGlobalSetting;
    protected boolean mCompact;
    protected BaseSetting<Value, CLASS, SettData, VH> mData;
    protected ISettCallback mCallback;
    protected boolean mWithBottomDivider;

    private boolean mStateEnabled = true;
    private boolean mStateVisible = true;

    private Parent mParent;

    public BaseSettingsItem(boolean globalSetting, boolean compact, BaseSetting<Value, CLASS, SettData, VH> data, ISettCallback callback, boolean withBottomDivider) {
        mGlobalSetting = globalSetting;
        mCompact = compact;
        mData = data;
        mCallback = callback;
        mWithBottomDivider = withBottomDivider;
        withIdentifier(data.getViewHolderId());
    }

    public void setGlobal(boolean global) {
        mGlobalSetting = global;
    }

    @Override
    public void setCompactMode(boolean enabled) {
        mCompact = enabled;
    }

    @Override
    public void checkDependency(boolean global, Object customSettingsObject) {
        Dependency dependency = mData.getDependency();
        if (dependency != null) {
            mStateEnabled = dependency.isEnabled(global, customSettingsObject);

        }
    }

    @Override
    public void setDependencyState(boolean enabled, boolean visible) {
        mStateEnabled = enabled;
        mStateVisible = visible;
    }

    public void withBottomDivider(boolean withBottomDivider) {
        mWithBottomDivider = withBottomDivider;
    }

    @Override
    public ISetting getSettings() {
        return mData;
    }

    @Override
    public int getType() {
        return mData.getLayoutTypeId();
    }

    @Override
    public int getLayoutRes() {
        return mData.getLayout();
    }

    @Override
    public void bindView(VH viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);
        viewHolder.updateCompactMode(mGlobalSetting, mCompact || mData.supportsCustomOnly());
        viewHolder.updateIcon(mData, mGlobalSetting);

        // 1) Custom Value Switch + Info Global Value
        if (!mGlobalSetting) {
            Util.setTextAppearance(viewHolder.getTitleTextView(), (mCompact || mData.supportsCustomOnly()) ? R.style.SettTitleCompactTextAppearance : R.style.SettTitleTextAppearance);
            viewHolder.getUseCustomSwitch().setVisibility(mData.supportsCustomOnly() ? View.GONE : View.VISIBLE);

            boolean customEnabled = mData.getCustomEnabled((CLASS) mCallback.getCustomSettingsObject());
            viewHolder.getUseCustomSwitch().setChecked(customEnabled);
            viewHolder.onUpdateCustomViewDependencies(mGlobalSetting);
        } else {
            Util.setTextAppearance(viewHolder.getTitleTextView(), R.style.SettTitleGlobalTextAppearance);
            viewHolder.getUseCustomSwitch().setVisibility(View.GONE);
        }

        // 2) Title + Sub Title
        mData.getTitle().display(viewHolder.getTitleTextView());
        if (mData.getSubTitle() != null) {
            viewHolder.getSubTitleTextView().setVisibility(View.VISIBLE);
            mData.getSubTitle().display(viewHolder.getSubTitleTextView());
        } else {
            viewHolder.getSubTitleTextView().setVisibility(View.GONE);
            viewHolder.getSubTitleTextView().setText("");
        }

        // 3) Wert
        viewHolder.updateValues(mData, mGlobalSetting, mCallback);

        // 4) Dividers
//        viewHolder.getInnerDivider().setVisibility(View.GONE);

        // 5) Info Button
        viewHolder.getInfoButton().setVisibility(mData.getInfo() != null ? View.VISIBLE : View.GONE);

        // 6) State updaten
        viewHolder.updateState(mStateEnabled, mStateVisible);

        viewHolder.bind(mData);
    }

    private void onUseCustomChanged(ISettingsViewHolder<Value, CLASS, SettData, VH> viewHolder) {
        if (mGlobalSetting) {
            return;
        }

        boolean b = viewHolder.getUseCustomSwitch().isChecked();
        if (b != mData.getCustomEnabled((CLASS) mCallback.getCustomSettingsObject())) {
            mData.setCustomEnabled((CLASS) mCallback.getCustomSettingsObject(), b);
            mData.onValueChanged(mData.getUseCustomId(), (Activity) viewHolder.getBinding().getRoot().getContext(), mGlobalSetting, (CLASS) mCallback.getCustomSettingsObject());
            viewHolder.onUpdateCustomViewDependencies(mGlobalSetting);
        }
    }

    private void onShowChangeSetting(VH viewHolder) {
        if (!mGlobalSetting && !mData.getCustomEnabled((CLASS) mCallback.getCustomSettingsObject())) {
            Toast.makeText(viewHolder.getTopValueContainer().getContext(), R.string.info_enable_setting_to_change_it, Toast.LENGTH_LONG).show();
        } else {
            mData.onShowChangeSetting(viewHolder, mCallback.getParentActivity(), mCallback.getBinding(), mData.getSettData(), mGlobalSetting, (CLASS) mCallback.getCustomSettingsObject());
        }
    }

    private void onShowInfo(ISettingsViewHolder<Value, CLASS, SettData, VH> viewHolder) {
        if (mData.getInfo() != null) {
            new InfoSettingsDialogFragmentBundleBuilder(
                    mData.isInfoHtml(),
                    mData.getInfo().getText()
            )
                    .createFragment()
                    .show(((FragmentActivity) mCallback.getParentActivity()).getSupportFragmentManager(), null);
        }
    }

    @Override
    public void unbindView(VH holder) {
        super.unbindView(holder);
        holder.unbind(mData);
    }

    // ------------------
    // SubItem
    // ------------------

    @Override
    public Parent getParent() {
        return mParent;
    }

    @Override
    public BaseSettingsItem<Parent, Value, CLASS, SettData, VH> withParent(Parent parent) {
        mParent = parent;
        return this;
    }

    // ------------------
    // Event Hooks
    // ------------------

    public static class EnableSettingsSwitchEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof ISettingsViewHolder) {
                return ((ISettingsViewHolder) viewHolder).getUseCustomSwitch();
            }
            return null;
        }

        /*
         * bind the desired event hook listener to the view
         * use it like following:
         * view.setOnClickListener(view -> { onHandleCustomEvent(...); });
         */
        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            ((FixedSwitch) view).setOnCheckedChangeListener((v, b) -> onEventOccurred(v, viewHolder, fastAdapter));
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            // handle the event
            ((BaseSettingsItem) item).onUseCustomChanged((ISettingsViewHolder) viewHolder);
        }
    }

    public static class ShowSettingsEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof BaseSettingViewHolder) {
                return ((ISettingsViewHolder) viewHolder).getRow1();
            }
            return null;
        }

        /*
         * bind the desired event hook listener to the view
         * use it like following:
         * view.setOnClickListener(view -> { onHandleCustomEvent(...); });
         */
        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            view.setOnClickListener(v -> onEventOccurred(v, viewHolder, fastAdapter));
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            // handle the event
            ((BaseSettingsItem) item).onShowChangeSetting(viewHolder);
        }
    }

    public static class ShowInfoEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof BaseSettingViewHolder) {
                return ((ISettingsViewHolder) viewHolder).getInfoButton();
            }
            return null;
        }

        /*
         * bind the desired event hook listener to the view
         * use it like following:
         * view.setOnClickListener(view -> { onHandleCustomEvent(...); });
         */
        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            view.setOnClickListener(v -> onEventOccurred(v, viewHolder, fastAdapter));
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            // handle the event
            ((BaseSettingsItem) item).onShowInfo((ISettingsViewHolder) viewHolder);
        }
    }
}
