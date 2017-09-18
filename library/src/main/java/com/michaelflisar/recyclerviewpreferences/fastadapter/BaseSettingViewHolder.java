package com.michaelflisar.recyclerviewpreferences.fastadapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.michaelflisar.recyclerviewpreferences.views.SettingsRootView;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.view.IconicsImageView;

/**
 * Created by Michael on 21.05.2017.
 */

public abstract class BaseSettingViewHolder<DB extends ViewDataBinding, Value, CLASS, SettData extends ISettData<Value, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder &
        ISettingsViewHolder<Value, CLASS, SettData, VH>> extends
        RecyclerView.ViewHolder implements
        ISettingsViewHolder<Value, CLASS, SettData, VH> {
    protected DB binding;

    public BaseSettingViewHolder(View view, boolean globalSetting, boolean compact) {
        super(view);
        binding = DataBindingUtil.bind(view);

        updateCompactMode(globalSetting, compact);

        if (globalSetting) {
            getUseCustomSwitch().setVisibility(View.GONE);
            getInnerDivider().setVisibility(View.GONE);
            getRow2().setVisibility(View.GONE);
        }

//        if (BaseDef.REMOVE_CARD_SETTINGS)
//        {
//            ((CardView)binding.getRoot()).setCardBackgroundColor(Color.TRANSPARENT);
//            ((CardView)binding.getRoot()).setCardElevation(0);
//        }
    }

    public BaseSettingViewHolder hideSecondRow() {
        getInnerDivider().setVisibility(View.GONE);
        getRow2().setVisibility(View.GONE);
        return this;
    }

    public Activity getActivity() {
        return (Activity) getBinding().getRoot().getContext();
    }

    @Override
    public void updateValues(BaseSetting<Value, CLASS, SettData, VH> data, boolean globalSetting, ISettCallback callback) {
        if (globalSetting) {
            data.updateValueView(true, (VH) this, getValueTopView(), data.getSettData(), globalSetting, callback);
        } else {
            data.updateValueView(true, (VH) this, getValueTopView(), data.getSettData(), false, callback);
            if (!data.supportsCustomOnly()) {
                data.updateValueView(false, (VH) this, getValueBottomView(), data.getSettData(), true, callback);
            }
        }
    }

    @Override
    public void onUpdateCustomViewDependencies(boolean globalSetting) {
        if (globalSetting) {
            getValueTopView().setEnabled(true);
            getTopValueContainer().setEnabled(true);
        } else {
            boolean b = getUseCustomSwitch().isChecked();
            getIsUsingGlobalTextView().setEnabled(!b);

            getValueTopView().setEnabled(b);
            getValueBottomView().setEnabled(!b);
            getTopValueContainer().setEnabled(true);//b); // Klick zeigt Info für User!

            getRow2().setEnabled(!b);
        }
    }

    @Override
    public void bind(BaseSetting<Value, CLASS, SettData, VH> data) {
        data.bind((VH) this);
    }

    @Override
    public void unbind(BaseSetting<Value, CLASS, SettData, VH> data) {
        data.unbind((VH) this);
        getBinding().unbind();
    }

    @Override
    public void updateState(boolean enabled, boolean visible) {
        // TODO: filter anstatt dieser Lösung, dies funktioniert auch mit Dekorator nicht gut zusammen
        SettingsRootView root = (SettingsRootView) getBinding().getRoot();
        root.setState(enabled, visible);
//        if (enabled)
//            getBinding().getRoot().setOnTouchListener(null);
//        else
//            getBinding().getRoot().setOnTouchListener((view, motionEvent) -> true);
    }

    @Override
    public void updateIcon(BaseSetting<Value, CLASS, SettData, VH> data, boolean globalSetting) {
        IIcon icon = data.getIcon();
        if (icon != null) {
            getIconView().setVisibility(View.VISIBLE);
            IconicsImageView iv = (IconicsImageView) getIconView();
            iv.setIcon(icon);
            iv.setPaddingDp(data.getIconPaddingDp());
            iv.setColor(Util.getTextColor());
//            IconicsDrawable d = ((IconicsDrawable) getIconView().getDrawable());
//            d.icon(icon).color(Util.getTextColor()).paddingDp(data.getIconPaddingDp());
        } else {
            getIconView().setVisibility(View.GONE);
        }

        if (!globalSetting) {
            int dividerMarginLeft = (int) getInnerDivider().getContext().getResources().getDimension(icon == null ? R.dimen.divider_left_margin_no_image : R.dimen.divider_left_margin_with_image);
            int row2PaddingLeft = (int) getInnerDivider().getContext().getResources().getDimension(icon == null ? R.dimen.row2_left_padding_no_image : R.dimen.row2_left_padding_with_image);
            ((ViewGroup.MarginLayoutParams) getInnerDivider().getLayoutParams()).leftMargin = dividerMarginLeft;
            getRow2().setPadding(row2PaddingLeft, getRow2().getPaddingTop(), getRow2().getPaddingRight(), getRow2().getPaddingBottom());
        }
    }


    public void updateCompactMode(boolean globalSetting, boolean compact) {
        // globale setting is always "compact"
        if (globalSetting) {
            return;
        }

        if (compact) {
            getInnerDivider().setVisibility(View.GONE);
            getRow2().setVisibility(View.GONE);
        } else {
            getInnerDivider().setVisibility(View.VISIBLE);
            getRow2().setVisibility(View.VISIBLE);
        }
    }
}
