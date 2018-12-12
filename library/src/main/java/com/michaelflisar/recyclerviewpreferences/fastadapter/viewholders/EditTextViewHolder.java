package com.michaelflisar.recyclerviewpreferences.fastadapter.viewholders;

import android.app.Activity;
import android.text.method.KeyListener;
import android.text.method.MovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewEdittextBottomBinding;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewEdittextTopBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.BaseSettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class EditTextViewHolder<
        String,
        CLASS,
        SettData extends ISettData<String, CLASS, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<String, CLASS, SettData, VH>>
        extends BaseSettingsViewHolder<ViewEdittextTopBinding, ViewEdittextBottomBinding, String, CLASS, SettData, VH> {

    private MovementMethod mMovementMethod = null;
    private KeyListener mKeyListener = null;

    public EditTextViewHolder(View view, boolean globalSetting, boolean compact) {
        super(view, globalSetting, compact, true);
    }

    @Override
    public int getTopLayoutResource() {
        return R.layout.view_edittext_top;
    }

    @Override
    public int getBottomLayoutResource() {
        return R.layout.view_edittext_bottom;
    }

    @Override
    public void unbind(BaseSetting<String, CLASS, SettData, VH> data) {
        super.unbind(data);
        mMovementMethod = null;
        mKeyListener = null;
    }

    @Override
    public void onUpdateCustomViewDependencies(boolean hasIcon, boolean globalSetting) {
        super.onUpdateCustomViewDependencies(hasIcon, globalSetting);
        if (mMovementMethod == null) {
            mMovementMethod = ((EditText) getValueTopView()).getMovementMethod();
            mKeyListener = ((EditText) getValueTopView()).getKeyListener();
        }
        // avoid the EditText catches touches if disabled so that the click listener of row1 works
        boolean b = globalSetting || getIsUseCustomSwitchChecked(hasIcon);
        getValueTopView().setClickable(b);
        getValueTopView().setFocusable(b);
        getValueTopView().setFocusableInTouchMode(b);
        ((EditText) getValueTopView()).setMovementMethod(b ? mMovementMethod : null);
        ((EditText) getValueTopView()).setKeyListener(b ? mKeyListener : null);

    }

    @Override
    public void onShowChangeSetting(VH vh, BaseSetting<String, CLASS, SettData, VH> data, Activity activity,
                                    ViewDataBinding binding,
                                    SettData settData, boolean global, CLASS customSettingsObject) {
        data.onShowChangeSetting(vh, activity, binding, settData, global, customSettingsObject);
    }

//        @Override
//        public AdapterSettingItemEdittextBinding getBinding() {
//            return binding;
//        }

    @Override
    public TextView getTitleTextView() {
        return ((ViewEdittextTopBinding) topBinding).tvTitle;
    }

    @Override
    public TextView getSubTitleTextView() {
        return ((ViewEdittextTopBinding) topBinding).tvSubTitle;
    }

    @Override
    public LinearLayout getTopValueContainer() {
        return ((ViewEdittextTopBinding) topBinding).llCustomValueContainer;
    }

    @Override
    public TextView getIsUsingGlobalTextView() {
        return ((ViewEdittextBottomBinding) bottomBinding).tvIsUsingDefault;
    }

    @Override
    public View getValueTopView() {
        return ((ViewEdittextTopBinding) topBinding).etValueTop;
    }

    @Override
    public View getValueBottomView() {
        return ((ViewEdittextBottomBinding) bottomBinding).etValueBottom;
    }

    @Override
    public View getRow2() {
        return ((ViewEdittextBottomBinding) bottomBinding).llRow2;
    }
}
