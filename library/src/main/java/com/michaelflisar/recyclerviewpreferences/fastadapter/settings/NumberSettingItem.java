package com.michaelflisar.recyclerviewpreferences.fastadapter.settings;

import android.view.View;
import android.widget.SeekBar;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewNumberTopBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.hooks.BaseCustomEventHook;
import com.michaelflisar.recyclerviewpreferences.fastadapter.viewholders.NumberViewHolder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.settings.NumberSetting;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Michael on 20.05.2017.
 */

public class NumberSettingItem<
        Parent extends IItem & IExpandable,
        CLASS,
        SettData extends ISettData<Integer, CLASS, SettData, VH>,
        VH extends NumberViewHolder<Integer, CLASS, SettData, VH>>
        extends BaseSettingsItem<Parent, Integer, CLASS, SettData, VH> {

    // ------------------
    // Factory
    // ------------------

    public NumberSettingItem(boolean globalSetting, boolean compact, BaseSetting<Integer, CLASS, SettData, VH> data, ISettCallback callback, boolean flatStyle) {
        super(globalSetting, compact, data, callback, flatStyle);
    }

    // ------------------
    // Constructor, ID, Layout
    // ------------------

    @Override
    public VH getViewHolder(View v) {
        return (VH) new NumberViewHolder(v, mGlobalSetting, mCompact);
    }

    @Override
    public void bindView(VH viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);
        ViewNumberTopBinding topBinding = viewHolder.getTopBinding();
        NumberSetting sett = (NumberSetting) mData;
        topBinding.sbTop.setVisibility(((NumberSetting) mData).getMode().getSeekbarVisibility());
        topBinding.sbTop.setMax((sett.getMax() - sett.getMin()) / sett.getStepSize());
        topBinding.sbTop.setProgress((mData.getValue((CLASS) mCallback.getCustomSettingsObject(), mGlobalSetting) - ((NumberSetting<CLASS, SettData, VH>) mData).getMin())
                / ((NumberSetting<CLASS, SettData, VH>) mData).getStepSize());
    }

    protected void onSeekbarChanged(VH viewHolder, int progress, boolean topSwitch) {
        if (mGlobalSetting && !topSwitch) {
            return;
        }

        boolean global = mGlobalSetting || !topSwitch;
        int currentInt = mData.getValue((CLASS) mCallback.getCustomSettingsObject(), mGlobalSetting);
        int newInt = ((NumberSetting<CLASS, SettData, VH>) mData).getMin() + progress * ((NumberSetting<CLASS, SettData, VH>) mData).getStepSize();
        if (currentInt != newInt) {
            if (mData.setValue((CLASS) mCallback.getCustomSettingsObject(), global, newInt)) {
                mData.updateValueView(true, viewHolder, viewHolder.getValueTopView(), mData.getSettData(), global, mCallback);
                mData.onValueChanged(global ? mData.getDefaultId() : mData.getCustomId(), viewHolder.getActivity(), global, (CLASS) mCallback.getCustomSettingsObject());
            }
        }
    }

    // ------------------
    // Event Hooks
    // ------------------

    public static class SettingsSeekbarTopEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof NumberViewHolder) {
                return ((ViewNumberTopBinding) ((NumberViewHolder) viewHolder).getTopBinding()).sbTop;
            }
            return null;
        }

        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            view.post(() -> {
                SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            onEventOccurred(seekBar, viewHolder, fastAdapter);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                };
                ((SeekBar) view).setOnSeekBarChangeListener(listener);
            });
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            ((NumberSettingItem) item).onSeekbarChanged((NumberViewHolder) viewHolder, ((SeekBar) view).getProgress(), true);
        }
    }
}
