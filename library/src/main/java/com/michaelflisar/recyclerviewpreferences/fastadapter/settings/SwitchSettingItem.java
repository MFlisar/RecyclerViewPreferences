package com.michaelflisar.recyclerviewpreferences.fastadapter.settings;

import android.view.View;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.fastadapter.hooks.BaseCustomEventHook;
import com.michaelflisar.recyclerviewpreferences.fastadapter.viewholders.SwitchViewHolder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.views.FixedSwitch;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Michael on 20.05.2017.
 */

public class SwitchSettingItem<
        Parent extends IItem & IExpandable,
        Boolean,
        CLASS,
        SettData extends ISettData<Boolean, CLASS, SettData, VH>,
        VH extends SwitchViewHolder<Boolean, CLASS, SettData, VH>>
        extends BaseSettingsItem<Parent, Boolean, CLASS, SettData, VH> {

    // ------------------
    // Factory
    // ------------------

    public SwitchSettingItem(boolean globalSetting, boolean compact, BaseSetting<Boolean, CLASS, SettData, VH> data, ISettCallback callback, boolean flatStyle) {
        super(globalSetting, compact, data, callback, flatStyle);
    }

    // ------------------
    // Constructor, ID, Layout
    // ------------------

    @Override
    public VH getViewHolder(View v) {
        VH vh = (VH) new SwitchViewHolder(v, mGlobalSetting, mCompact);
        // prevent EditText in Row2 from being editable
        vh.getValueBottomView().setOnTouchListener((view, motionEvent) -> true);
        return vh;
    }

    protected void onSwitchChanged(VH viewHolder, Boolean value, boolean topSwitch) {
        if (mGlobalSetting && !topSwitch) {
            return;
        }

        boolean global = mGlobalSetting || !topSwitch;
        if (!mData.getValue((CLASS) mCallback.getCustomSettingsObject(), global).equals(value)) {
            if (mData.setValue((CLASS) mCallback.getCustomSettingsObject(), global, value)) {
                mData.onValueChanged(global ? mData.getDefaultId() : mData.getCustomId(), viewHolder.getActivity(), global, (CLASS) mCallback.getCustomSettingsObject());
            }
        }
    }

    // ------------------
    // Event Hooks
    // ------------------

    public static class SettingsSwitchTopEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof SwitchViewHolder) {
                return ((SwitchViewHolder) viewHolder).getValueTopView();
            }
            return null;
        }

        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            ((FixedSwitch) view).setOnCheckedChangeListener((v, b) -> onEventOccurred(v, viewHolder, fastAdapter));
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            ((SwitchSettingItem) item).onSwitchChanged((SwitchViewHolder) viewHolder, ((FixedSwitch) view).isChecked(), true);
        }
    }

    public static class SettingsSwitchBottomEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof SwitchViewHolder) {
                return ((SwitchViewHolder) viewHolder).getValueBottomView();
            }
            return null;
        }

        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            ((FixedSwitch) view).setOnCheckedChangeListener((v, b) -> onEventOccurred(v, viewHolder, fastAdapter));
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            ((SwitchSettingItem) item).onSwitchChanged((SwitchViewHolder) viewHolder, ((FixedSwitch) view).isChecked(), false);
        }
    }
}
