package com.michaelflisar.recyclerviewpreferences.fastadapter.viewholders;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewSpinnerBottomBinding;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewSpinnerBottomDialogBinding;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewSpinnerTopBinding;
import com.michaelflisar.recyclerviewpreferences.databinding.ViewSpinnerTopDialogBinding;
import com.michaelflisar.recyclerviewpreferences.fastadapter.BaseSettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;

import androidx.databinding.ViewDataBinding;

public class SpinnerViewHolder<
        Data,
        CLASS,
        SettData extends ISettData<Data, CLASS, SettData, VH>,
        VH extends SpinnerViewHolder<Data, CLASS, SettData, VH>> extends
        BaseSettingsViewHolder<ViewDataBinding, ViewDataBinding, Data, CLASS, SettData, VH> {

    private int mSpinnerMode;

    public SpinnerViewHolder(View view, boolean globalSetting, boolean compact, int spinnerMode) {
        super(view, globalSetting, compact,false);
        mSpinnerMode = spinnerMode;
        init(view, globalSetting, compact);
    }

    @Override
    public int getTopLayoutResource() {
        return mSpinnerMode == Spinner.MODE_DROPDOWN ? R.layout.view_spinner_top : R.layout.view_spinner_top_dialog;
    }

    @Override
    public int getBottomLayoutResource() {
        return mSpinnerMode == Spinner.MODE_DROPDOWN ? R.layout.view_spinner_bottom : R.layout.view_spinner_bottom_dialog;
    }

    @Override
    public void onUpdateCustomViewDependencies(boolean hasIcon, boolean globalSetting) {
        super.onUpdateCustomViewDependencies(hasIcon, globalSetting);
        // make sure, that spinner does not consume touches if disabled so that the click listener of row1 works
        if (globalSetting) {
            getValueTopView().setClickable(true);
        } else {
            boolean b = getIsUseCustomSwitchChecked(hasIcon);
            getValueTopView().setClickable(b);
        }
    }

    @Override
    public void onShowChangeSetting(VH vh, BaseSetting<Data, CLASS, SettData, VH> data, Activity activity, ViewDataBinding binding,
                                    SettData settData, boolean global, CLASS customSettingsObject) {
        data.onShowChangeSetting(vh, activity, binding, settData, global, customSettingsObject);
    }

    @Override
    public TextView getTitleTextView() {
        if (topBinding instanceof ViewSpinnerTopBinding) {
            return ((ViewSpinnerTopBinding) topBinding).tvTitle;
        } else {
            return ((ViewSpinnerTopDialogBinding) topBinding).tvTitle;
        }
    }

    @Override
    public TextView getSubTitleTextView() {
        if (topBinding instanceof ViewSpinnerTopBinding) {
            return ((ViewSpinnerTopBinding) topBinding).tvSubTitle;
        } else {
            return ((ViewSpinnerTopDialogBinding) topBinding).tvSubTitle;
        }
    }

    @Override
    public LinearLayout getTopValueContainer() {
        if (topBinding instanceof ViewSpinnerTopBinding) {
            return ((ViewSpinnerTopBinding) topBinding).llCustomValueContainer;
        } else {
            return ((ViewSpinnerTopDialogBinding) topBinding).llCustomValueContainer;
        }
    }

    @Override
    public TextView getIsUsingGlobalTextView() {
        if (bottomBinding instanceof ViewSpinnerBottomBinding) {
            return ((ViewSpinnerBottomBinding) bottomBinding).tvIsUsingDefault;
        } else {
            return ((ViewSpinnerBottomDialogBinding) bottomBinding).tvIsUsingDefault;
        }
    }

    @Override
    public View getValueTopView() {
        if (topBinding instanceof ViewSpinnerTopBinding) {
            return ((ViewSpinnerTopBinding) topBinding).spValueTop;
        } else {
            return ((ViewSpinnerTopDialogBinding) topBinding).spValueTop;
        }
    }

    @Override
    public View getValueBottomView() {
        if (bottomBinding instanceof ViewSpinnerBottomBinding) {
            return ((ViewSpinnerBottomBinding) bottomBinding).spValueBottom;
        } else {
            return ((ViewSpinnerBottomDialogBinding) bottomBinding).spValueBottom;
        }
    }

    @Override
    public View getRow2() {
        if (bottomBinding instanceof ViewSpinnerBottomBinding) {
            return ((ViewSpinnerBottomBinding) bottomBinding).llRow2;
        } else {
            return ((ViewSpinnerBottomDialogBinding) bottomBinding).llRow2;
        }
    }
}
