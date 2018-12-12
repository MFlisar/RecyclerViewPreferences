package com.michaelflisar.recyclerviewpreferences.fastadapter;

import android.view.View;
import android.widget.ImageView;

import com.github.zagum.switchicon.SwitchIconView;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;

import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseSettingsViewHolder<
        DBTop extends ViewDataBinding,
        DBBottom extends ViewDataBinding,
        Value,
        CLASS,
        SettData extends ISettData<Value, CLASS, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Value, CLASS, SettData, VH>> extends
        BaseBaseSettingViewHolder<Value, CLASS, SettData, VH> {

    public BaseSettingsViewHolder(View view, boolean globalSetting, boolean compact, boolean callInit) {
        super(view, globalSetting, compact, callInit);
    }

    public abstract int getTopLayoutResource();

    public abstract int getBottomLayoutResource();

    @Override
    protected void bindView(View view) {
        super.bindView(view);
        if (binding.stubTop.getViewStub() != null && !binding.stubTop.isInflated()) {
            binding.stubTop.getViewStub().setLayoutResource(getTopLayoutResource());
            View v = binding.stubTop.getViewStub().inflate();
            topBinding = DataBindingUtil.bind(v);
        }
        if (binding.stubBottom.getViewStub() != null && !binding.stubBottom.isInflated()) {
            binding.stubBottom.getViewStub().setLayoutResource(getBottomLayoutResource());
            View v = binding.stubBottom.getViewStub().inflate();
            bottomBinding = DataBindingUtil.bind(v);
        }
    }

    @Override
    public CardView getCardView() {
        return binding.cardView;
    }

    @Override
    public void hideCustomSwitches() {
        binding.swEnable.setVisibility(View.GONE);
        binding.switchIconView.setVisibility(View.GONE);
    }

    @Override
    public View getUseCustomSwitch(boolean hasIcon) {
        return hasIcon ? binding.switchIconView : binding.swEnable;
    }

    @Override
    public void setUseCustomSwitchChecked(boolean hasIcon, boolean checked) {
        if (hasIcon) {
            binding.switchIconView.setIconEnabled(checked);
        } else  {
            binding.swEnable.setChecked(checked);
        }
    }

    @Override
    public boolean getIsUseCustomSwitchChecked(boolean hasIcon) {
        return hasIcon ? binding.switchIconView.isIconEnabled() : binding.swEnable.isChecked();
    }

    @Override
    public void setCustomSwitchListeners(ICustomSwitchListener listener) {
//        if (hasIcon) {
            binding.switchIconView.setOnClickListener(v -> {
                ((SwitchIconView)v).switchState();
                listener.onSwitchChanged(v, ((SwitchIconView)v).isIconEnabled());
            });
//        } else  {
            binding.swEnable.setOnCheckedChangeListener((v, b) -> listener.onSwitchChanged(v, b));
//        }
    }

//    @Override
//    public ImageView getIconView() {
//        return binding.ivIcon;
//    }

    @Override
    public ImageView getInnerIconView() {
        return binding.ivInnerIcon;
    }

    @Override
    public View getInfoButton() {
        return binding.btInfo;
    }

    @Override
    public View getInnerDivider() {
        return binding.vDividerRow;
    }

    @Override
    public View getRow1() {
        return binding.llRow1;
    }
}
