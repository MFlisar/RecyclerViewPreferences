package com.michaelflisar.recyclerviewpreferences.fastadapter.settings;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.classes.SimpleSpinnerListener;
import com.michaelflisar.recyclerviewpreferences.fastadapter.hooks.BaseCustomEventHook;
import com.michaelflisar.recyclerviewpreferences.fastadapter.viewholders.SpinnerViewHolder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.settings.SpinnerSetting;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Michael on 20.05.2017.
 */

public class SpinnerSettingItem<
        Parent extends IItem & IExpandable,
        CLASS,
        SettData extends ISettData<Integer, CLASS, SettData, VH>,
        VH extends SpinnerViewHolder<Integer, CLASS, SettData, VH>>
        extends BaseSettingsItem<Parent, Integer, CLASS, SettData, VH> {

    private int mSpinnerMode;

    // ------------------
    // Factory
    // ------------------

    public SpinnerSettingItem(int spinnerMode, boolean globalSetting, boolean compact, BaseSetting<Integer, CLASS, SettData, VH> data, ISettCallback callback, boolean flatStyle) {
        super(globalSetting, compact, data, callback, flatStyle);
        mSpinnerMode = spinnerMode;
    }

    // ------------------
    // Constructor, ID, Layout
    // ------------------

    @Override
    public VH getViewHolder(View v) {
        VH vh = (VH) new SpinnerViewHolder<Integer, CLASS, SettData, VH>(v, mGlobalSetting, mCompact, mSpinnerMode);
        // prevent Spinner in Row2 from being editable
        vh.getValueBottomView().setOnTouchListener((view, motionEvent) -> true);
        return vh;
    }

    protected void onSpinnerChanged(VH viewHolder, Integer index, boolean topSwitch) {
        if (mGlobalSetting && !topSwitch) {
            return;
        }

        boolean global = mGlobalSetting || !topSwitch;
        int currentID = mData.getValue((CLASS) mCallback.getCustomSettingsObject(), global);
        int newId = ((SpinnerSetting<CLASS, SettData, VH>) mData).getListId(index);
        if (currentID != newId) {
            if (mData.setValue((CLASS) mCallback.getCustomSettingsObject(), global, newId)) {
                mData.onValueChanged(global ? mData.getDefaultId() : mData.getCustomId(), viewHolder.getActivity(), global, (CLASS) mCallback.getCustomSettingsObject());
            }
        }
    }

    @Override
    public void unbindView(VH holder) {
        ArrayAdapter adapter = (ArrayAdapter) ((Spinner) holder.getValueTopView()).getAdapter();
        if (adapter != null) {
            adapter.clear();
            ((Spinner) holder.getValueTopView()).setAdapter(adapter);
        }
        adapter = (ArrayAdapter) ((Spinner) holder.getValueBottomView()).getAdapter();
        if (adapter != null) {
            adapter.clear();
            ((Spinner) holder.getValueBottomView()).setAdapter(adapter);
        }
        super.unbindView(holder);
    }

    // ------------------
    // Event Hooks
    // ------------------

    public static class SettingsSpinnerTopEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof SpinnerViewHolder) {
                return ((SpinnerViewHolder) viewHolder).getValueTopView();
            }
            return null;
        }

        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            view.post(() -> {
                SimpleSpinnerListener listener = new SimpleSpinnerListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        onEventOccurred(parent, viewHolder, fastAdapter);
                    }
                };
                ((Spinner) view).setOnItemSelectedListener(listener);
            });
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            ((SpinnerSettingItem) item).onSpinnerChanged((SpinnerViewHolder) viewHolder, ((Spinner) view).getSelectedItemPosition(), true);
        }
    }

    public static class SettingsSpinnerBottomEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof SpinnerViewHolder) {
                return ((SpinnerViewHolder) viewHolder).getValueBottomView();
            }
            return null;
        }

        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            view.post(() -> {
                SimpleSpinnerListener listener = new SimpleSpinnerListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        onEventOccurred(parent, viewHolder, fastAdapter);
                    }
                };
                ((Spinner) view).setOnItemSelectedListener(listener);
            });
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            ((SpinnerSettingItem) item).onSpinnerChanged((SpinnerViewHolder) viewHolder, ((Spinner) view).getSelectedItemPosition(), false);
        }
    }
}
