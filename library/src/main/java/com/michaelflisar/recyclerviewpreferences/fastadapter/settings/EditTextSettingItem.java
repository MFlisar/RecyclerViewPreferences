package com.michaelflisar.recyclerviewpreferences.fastadapter.settings;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.method.MovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.databinding.AdapterSettingItemEdittextBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.BaseSettingViewHolder;
import com.michaelflisar.recyclerviewpreferences.fastadapter.hooks.BaseCustomEventHook;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.views.FixedSwitch;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;

/**
 * Created by Michael on 20.05.2017.
 */

public class EditTextSettingItem<Parent extends IItem & IExpandable, String, CLASS, SettData extends ISettData<String, CLASS, SettData, VH>, VH extends EditTextSettingItem
        .EditTextViewHolder<String, CLASS, SettData, VH>> extends
        BaseSettingsItem<Parent, String, CLASS, SettData, VH> {

    // ------------------
    // Factory
    // ------------------

    public EditTextSettingItem(boolean globalSetting, boolean compact, BaseSetting<String, CLASS, SettData, VH> data, ISettCallback callback, boolean withBottomDivider) {
        super(globalSetting, compact, data, callback, withBottomDivider);
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
    // ViewHolder
    // ------------------

    public static class EditTextViewHolder<String, CLASS, SettData extends ISettData<String, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<String, CLASS, SettData,
            VH>> extends
            BaseSettingViewHolder<AdapterSettingItemEdittextBinding, String, CLASS, SettData, VH> {
        public EditTextViewHolder(View view, boolean globalSetting, boolean compact) {
            super(view, globalSetting, compact);
        }

        private MovementMethod mMovementMethod = null;
        private KeyListener mKeyListener = null;

        @Override
        public void unbind(BaseSetting<String, CLASS, SettData, VH> data) {
            super.unbind(data);
            mMovementMethod = null;
            mKeyListener = null;
        }

        @Override
        public void onUpdateCustomViewDependencies(boolean globalSetting) {
            super.onUpdateCustomViewDependencies(globalSetting);
            if (mMovementMethod == null) {
                mMovementMethod = ((EditText) getValueTopView()).getMovementMethod();
                mKeyListener = ((EditText) getValueTopView()).getKeyListener();
            }
            // avoid the EditText catches touches if disabled so that the click listener of row1 works
            boolean b = globalSetting || getUseCustomSwitch().isChecked();
            getValueTopView().setClickable(b);
            getValueTopView().setFocusable(b);
            getValueTopView().setFocusableInTouchMode(b);
            ((EditText) getValueTopView()).setMovementMethod(b ? mMovementMethod : null);
            ((EditText) getValueTopView()).setKeyListener(b ? mKeyListener : null);

        }

        @Override
        public void onShowChangeSetting(VH vh, BaseSetting<String, CLASS, SettData, VH> data, Activity activity, ViewDataBinding binding,
                SettData settData, boolean global, CLASS customSettingsObject) {
            data.onShowChangeSetting(vh, activity, binding, settData, global, customSettingsObject);
        }

        @Override
        public AdapterSettingItemEdittextBinding getBinding() {
            return binding;
        }

        @Override
        public FixedSwitch getUseCustomSwitch() {
            return binding.swEnable;
        }

        @Override
        public TextView getTitleTextView() {
            return binding.tvTitle;
        }

        @Override
        public TextView getSubTitleTextView() {
            return binding.tvSubTitle;
        }

        @Override
        public LinearLayout getTopValueContainer() {
            return binding.llCustomValueContainer;
        }

        @Override
        public TextView getIsUsingGlobalTextView() {
            return binding.tvIsUsingDefault;
        }

        @Override
        public ImageView getIconView() {
            return binding.ivIcon;
        }

        @Override
        public View getInfoButton() {
            return binding.btInfo;
        }

        @Override
        public View getValueTopView() {
            return binding.etValueTop;
        }

        @Override
        public View getValueBottomView() {
            return binding.etValueBottom;
        }

        @Override
        public View getInnerDivider() {
            return binding.vDividerRow;
        }

        @Override
        public View getRow1() {
            return binding.llRow1;
        }

        @Override
        public View getRow2() {
            return binding.llRow2;
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
                return (((EditTextViewHolder) viewHolder).getBinding()).etValueTop;
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
                return (((EditTextViewHolder) viewHolder).getBinding()).etValueBottom;
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
