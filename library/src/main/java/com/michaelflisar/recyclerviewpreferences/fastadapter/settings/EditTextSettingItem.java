package com.michaelflisar.recyclerviewpreferences.fastadapter.settings;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.fastadapter.hooks.BaseCustomEventHook;
import com.michaelflisar.recyclerviewpreferences.fastadapter.viewholders.EditTextViewHolder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Michael on 20.05.2017.
 */

public class EditTextSettingItem<
        Parent extends IItem & IExpandable,
        String,
        CLASS,
        SettData extends ISettData<String, CLASS, SettData, VH>,
        VH extends EditTextViewHolder<String, CLASS, SettData, VH>>
        extends BaseSettingsItem<Parent, String, CLASS, SettData, VH> {

    // ------------------
    // Factory
    // ------------------

    public EditTextSettingItem(boolean globalSetting, boolean compact, BaseSetting<String, CLASS, SettData, VH> data, ISettCallback callback, boolean flatStyle) {
        super(globalSetting, compact, data, callback, flatStyle);
    }

    // ------------------
    // Constructor, ID, Layout
    // ------------------

    @Override
    public VH getViewHolder(View v) {
        VH vh = (VH) new EditTextViewHolder(v, mGlobalSetting, mCompact);
        // prevent EditText in Row2 from being editable
        vh.getValueBottomView().setOnTouchListener((view, motionEvent) -> true);
        return vh;
    }

    protected void onTextChanged(EditTextViewHolder viewHolder, String value, boolean topSwitch) {
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

    public static class SettingsEditTextTopEvent extends BaseCustomEventHook<IItem> {
        private TextWatcher mTextWatcher = null;

        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof EditTextViewHolder) {
                return ((EditTextViewHolder) viewHolder).getValueTopView();
            }
            return null;
        }

        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            if (mTextWatcher == null) {
                ((EditText) view).removeTextChangedListener(mTextWatcher);
            }
            mTextWatcher = new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    onEventOccurred(view, viewHolder, fastAdapter);
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }

                @Override
                public void afterTextChanged(Editable arg0) {
                }
            };
            ((EditText) view).addTextChangedListener(mTextWatcher);
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            ((EditTextSettingItem) item).onTextChanged((EditTextViewHolder) viewHolder, ((EditText) view).getText().toString(), true);
        }
    }

    public static class SettingsEditTextBottomEvent extends BaseCustomEventHook<IItem> {
        private TextWatcher mTextWatcher = null;

        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof EditTextViewHolder) {
                return ((EditTextViewHolder) viewHolder).getValueBottomView();
            }
            return null;
        }

        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            if (mTextWatcher == null) {
                ((EditText) view).removeTextChangedListener(mTextWatcher);
            }
            mTextWatcher = new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    onEventOccurred(view, viewHolder, fastAdapter);
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }

                @Override
                public void afterTextChanged(Editable arg0) {
                }
            };
            ((EditText) view).addTextChangedListener(mTextWatcher);
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            ((EditTextSettingItem) item).onTextChanged((EditTextViewHolder) viewHolder, ((EditText) view).getText().toString(), false);
        }
    }
}
