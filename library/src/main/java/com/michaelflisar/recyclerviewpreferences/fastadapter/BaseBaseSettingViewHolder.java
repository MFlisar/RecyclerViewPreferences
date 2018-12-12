package com.michaelflisar.recyclerviewpreferences.fastadapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.databinding.AdapterBaseSettingItemBinding;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.michaelflisar.recyclerviewpreferences.views.SettingsRootView;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.view.IconicsImageView;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Michael on 21.05.2017.
 */

public abstract class BaseBaseSettingViewHolder<
        Value, CLASS, SettData extends ISettData<Value, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder &
        ISettingsViewHolder<Value, CLASS, SettData, VH>> extends
        RecyclerView.ViewHolder implements
        ISettingsViewHolder<Value, CLASS, SettData, VH> {

    protected boolean compact;
    protected AdapterBaseSettingItemBinding binding;
    protected ViewDataBinding topBinding = null;
    protected ViewDataBinding bottomBinding = null;

    public BaseBaseSettingViewHolder(View view, boolean globalSetting, boolean compact, boolean callInit) {
        super(view);
        this.compact = compact;
        if (callInit) {
            init(view, globalSetting, compact);
        }
    }

    protected void init(View view, boolean globalSetting, boolean compact) {
        bindView(view);

        updateCompactMode(globalSetting, compact);

        if (globalSetting) {
            hideCustomSwitches();
            getInnerDivider().setVisibility(View.GONE);
            getRow2().setVisibility(View.GONE);
        }

//        if (BaseDef.REMOVE_CARD_SETTINGS)
//        {
//            ((CardView)binding.getRoot()).setCardBackgroundColor(Color.TRANSPARENT);
//            ((CardView)binding.getRoot()).setCardElevation(0);
//        }
    }

    protected void bindView(View view) {
        binding = DataBindingUtil.bind(view);
        onBindindReady();
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
            if (data.getSupportType() != BaseSetting.SupportType.CustomOnly) {
                data.updateValueView(false, (VH) this, getValueBottomView(), data.getSettData(), true, callback);
            }
        }
    }

    @Override
    public void onUpdateCustomViewDependencies(boolean hasIcon, boolean globalSetting) {
        if (globalSetting) {
            getValueTopView().setEnabled(true);
            getTopValueContainer().setEnabled(true);
        } else {
            boolean b = getIsUseCustomSwitchChecked(hasIcon);
            getIsUsingGlobalTextView().setEnabled(!b);

            getValueTopView().setEnabled(b);
            getValueBottomView().setEnabled(!b);
            getTopValueContainer().setEnabled(true);//b); // Klick zeigt Info für User!

            getRow2().setEnabled(!b);
        }
    }

    @Override
    public void bind(BaseSetting<Value, CLASS, SettData, VH> data, boolean globalSetting) {
        data.bind((VH) this);
        updateSwitchesVisibilities(globalSetting, data);
    }

    @Override
    public void unbind(BaseSetting<Value, CLASS, SettData, VH> data) {
        data.unbind((VH) this);
        getBinding().unbind();
        if (topBinding != null) {
            topBinding.unbind();
        }
        if (bottomBinding != null) {
            bottomBinding.unbind();
        }
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
            IconicsDrawable drawable = new IconicsDrawable(binding.getRoot().getContext(), icon);
            drawable.paddingDp(data.getIconPaddingDp());
            if (data.getIconColor() != null) {
                drawable.color(data.getIconColor());
            } else {
                drawable.color(Util.getSecondaryTextColor());
            }

//            if (data.getSettingType() == BaseSetting.SettingType.Normal) {
                binding.switchIconView.setVisibility(View.VISIBLE);
                binding.switchIconView.setImageDrawable(drawable.sizeDp(24).clone());
//            } else {
//                binding.switchIconView.setVisibility(View.GONE);
//            }

//            if (showIcon(globalSetting, compact)) {
//                getIconView().setVisibility(View.VISIBLE);
//                IconicsImageView iv = (IconicsImageView) getIconView();
//                iv.setIcon(drawable);
//            }

            if (showInnerIcon(globalSetting, compact, data)) {
                getInnerIconView().setVisibility(View.VISIBLE);
                ((IconicsImageView) getInnerIconView()).setIcon(drawable);
            }
        } else {
//            getIconView().setVisibility(View.GONE);
            if (getInnerIconView() != null) {
                getInnerIconView().setVisibility(View.GONE);
            }
            binding.switchIconView.setVisibility(View.GONE);
        }
    }

    private int getDimen(int dimen) {
        return (int) binding.getRoot().getResources().getDimension(dimen);
    }

    private void updatePaddingLeft(View view, int paddingLeft) {
        view.setPadding(paddingLeft, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    private void updateMarginLeft(View view, int marginLeft) {
        ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).leftMargin = marginLeft;
    }

    public void updateCompactMode(boolean globalSetting, boolean compact) {
        this.compact = compact;

        // globale setting is always "compact"
        if (globalSetting) {
            hideCustomSwitches();
//                getIconView().setVisibility(View.GONE);
            getInnerIconView().setVisibility(View.VISIBLE);
            return;
        }

        if (compact) {
            getInnerDivider().setVisibility(View.GONE);
            getRow2().setVisibility(View.GONE);
//                getIconView().setVisibility(View.GONE);
            getInnerIconView().setVisibility(View.GONE);
        } else {
            getInnerDivider().setVisibility(View.VISIBLE);
            getRow2().setVisibility(View.VISIBLE);
//                getIconView().setVisibility(View.VISIBLE);
            getInnerIconView().setVisibility(View.GONE);
        }
    }

    private void updateSwitchesVisibilities(boolean globalSetting, BaseSetting<Value, CLASS, SettData, VH> data) {
        BaseSetting.SupportType type = data.getSupportType();
        BaseSetting.SettingType settingType = data.getSettingType();
        if (globalSetting || type == BaseSetting.SupportType.CustomOnly || settingType == BaseSetting.SettingType.Info) {
            binding.flSwitches.setVisibility(View.GONE);
        } else {
            binding.flSwitches.setVisibility(View.VISIBLE);
            if (data.getIcon() != null) {
                binding.swEnable.setVisibility(View.GONE);
                binding.switchIconView.setVisibility(View.VISIBLE);
            } else {
                binding.swEnable.setVisibility(View.VISIBLE);
                binding.switchIconView.setVisibility(View.GONE);
            }
        }
    }

//    private boolean showIcon(boolean globalSetting, boolean compact) {
//        // globale setting is always "compact"
//        if (globalSetting) {
//            return false;
//        }
//
//        if (compact) {
//            return true;
//        } else {
//            return true;
//        }
//    }

    public boolean showInnerIcon(boolean globalSetting, boolean compact, BaseSetting<Value, CLASS, SettData, VH> data) {
        // globale setting is always "compact"
        if (globalSetting || data.getSettingType() == BaseSetting.SettingType.Info) {
            return true;
        }

        BaseSetting.SupportType type = data.getSupportType();
        if (type == BaseSetting.SupportType.CustomOnly) {
            return true;
        } else {
            return false;
        }
    }

    protected void onBindindReady() {

    }

    public final AdapterBaseSettingItemBinding getBinding() {
        return binding;
    }

    @Override
    public <T extends ViewDataBinding> T getTopBinding() {
        return (T)topBinding;
    }

    @Override
    public <T extends ViewDataBinding> T getBottomBinding() {
        return (T)bottomBinding;
    }
}
