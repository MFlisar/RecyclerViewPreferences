package com.michaelflisar.recyclerviewpreferences.fastadapter.settings;

import android.app.Activity;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.classes.Dependency;
import com.michaelflisar.recyclerviewpreferences.fastadapter.BaseBaseSettingViewHolder;
import com.michaelflisar.recyclerviewpreferences.fastadapter.hooks.BaseCustomEventHook;
import com.michaelflisar.recyclerviewpreferences.fragments.InfoSettingsDialogFragment;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.ISubItem;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

/**
 * Created by flisar on 13.03.2017.
 */

public abstract class BaseSettingsItem<
        Parent extends IItem & IExpandable,
        Value,
        CLASS,
        SettData extends ISettData<Value, CLASS, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Value, CLASS, SettData, VH>>
        extends AbstractItem<BaseSettingsItem, VH> implements ISubItem<BaseSettingsItem<Parent, Value, CLASS, SettData, VH>, Parent>, ISettingsItem {

    // ------------------
    // Item
    // ------------------

    protected boolean mGlobalSetting;
    protected boolean mCompact;
    protected BaseSetting<Value, CLASS, SettData, VH> mData;
    protected ISettCallback mCallback;
    protected boolean mFlatStyle;

    private boolean mStateEnabled = true;
    private boolean mStateVisible = true;

    private Parent mParent;

    public BaseSettingsItem(boolean globalSetting, boolean compact, BaseSetting<Value, CLASS, SettData, VH> data, ISettCallback callback, boolean flatStyle) {
        mGlobalSetting = globalSetting;
        mCompact = compact;
        mData = data;
        mCallback = callback;
        withIdentifier(data.getViewHolderId());
        mFlatStyle = flatStyle;
    }

    public void setGlobal(boolean global) {
        mGlobalSetting = global;
    }

    @Override
    public void setCompactMode(boolean enabled) {
        mCompact = enabled;
    }

    @Override
    public void checkDependency(boolean global, Object customSettingsObject) {
        List<Dependency> dependencies = mData.getDependencies();
        Boolean newStateEnabled = null;
        if (dependencies != null) {
            for (Dependency d : dependencies) {
                boolean enabled = d.isEnabled(global, customSettingsObject);
                if (newStateEnabled == null) {
                    newStateEnabled = enabled;
                } else {
                    newStateEnabled &= enabled;
                }
            }
        }
        if (newStateEnabled != null) {
            mStateEnabled = newStateEnabled;
        }
    }

    @Override
    public void setDependencyState(boolean enabled, boolean visible) {
        mStateEnabled = enabled;
        mStateVisible = visible;
    }

    @Override
    public ISetting getSettings() {
        return mData;
    }

    @Override
    public int getType() {
        return mData.getLayoutTypeId();
    }

    @Override
    public int getLayoutRes() {
        return mData.getLayout();
    }

//    @Override
//    public View createView(Context ctx, @Nullable ViewGroup parent) {
//        View view = LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false);
//
//        return view;
//    }

    @Override
    public void bindView(VH viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);
        mData.onLayoutReady(viewHolder);
        viewHolder.updateCompactMode(mGlobalSetting, mCompact || mData.getSupportType() == BaseSetting.SupportType.CustomOnly || mData.getSettingType() == BaseSetting.SettingType.Info);
        viewHolder.updateIcon(mData, mGlobalSetting);

        // 1) Custom Value Switch + Info Global Value
        if (!mGlobalSetting) {
            if (mData.getSettingType() == BaseSetting.SettingType.Normal) {
                Util.setTextAppearance(viewHolder.getTitleTextView(), (mCompact || mData.getSupportType() == BaseSetting.SupportType.CustomOnly) ? R.style.SettTitleCompactTextAppearance : R.style.SettTitleTextAppearance);
                viewHolder.getUseCustomSwitch(mData.getIcon() != null).setVisibility(mData.getSupportType() == BaseSetting.SupportType.CustomOnly ? View.GONE : View.VISIBLE);

                boolean customEnabled = mData.getCustomEnabled((CLASS) mCallback.getCustomSettingsObject());
                viewHolder.setUseCustomSwitchChecked(mData.getIcon() != null, customEnabled);
                viewHolder.onUpdateCustomViewDependencies(mData.getIcon() != null, mGlobalSetting);
            } else {
                setGlobalTitelAndSwitchLook(viewHolder);
            }
        } else {
            setGlobalTitelAndSwitchLook(viewHolder);
        }

        // 2) Title + Sub Title
        mData.getTitle().display(viewHolder.getTitleTextView());
        if (mData.getSubTitle() != null) {
            viewHolder.getSubTitleTextView().setVisibility(View.VISIBLE);
            mData.getSubTitle().display(viewHolder.getSubTitleTextView());
        } else {
            viewHolder.getSubTitleTextView().setVisibility(View.GONE);
            viewHolder.getSubTitleTextView().setText("");
        }
        if (mData.getTitleTextColor() != null) {
            viewHolder.getTitleTextView().setTextColor(mData.getTitleTextColor());
        } else {
            viewHolder.getTitleTextView().setTextColor(Util.getSecondaryTextColor());
        }
        if (mData.getTextColor() != null) {
            viewHolder.getSubTitleTextView().setTextColor(mData.getTextColor());
        } else {
            viewHolder.getSubTitleTextView().setTextColor(Util.getTertiaryTextColor());
        }
        // 3) Wert
        viewHolder.updateValues(mData, mGlobalSetting, mCallback);

        // 4) Dividers
//        viewHolder.getInnerDivider().setVisibility(View.GONE);

        // 5) Info Button
        viewHolder.getInfoButton().setVisibility(mData.getSettingType() == BaseSetting.SettingType.Normal && mData.getInfo() != null ? View.VISIBLE : View.GONE);

        // 6) State updaten
        viewHolder.updateState(mStateEnabled, mStateVisible);

        viewHolder.bind(mData, mGlobalSetting);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (mData.getBackgroundTint() != null) {
//                viewHolder.getCardView().setBackgroundTintList(ColorStateList.valueOf(mData.getBackgroundTint()));
//            } else {
//                viewHolder.getCardView().setCardBackgroundColor(Util.getCardColor());
//            }
//        }

        if (mFlatStyle)
            ViewCompat.setElevation(viewHolder.getCardView(), 0);
    }

    private void setGlobalTitelAndSwitchLook(VH viewHolder) {
        Util.setTextAppearance(viewHolder.getTitleTextView(), R.style.SettTitleGlobalTextAppearance);
        viewHolder.getUseCustomSwitch(mData.getIcon() != null).setVisibility(View.GONE);
    }

    private void onUseCustomChanged(ISettingsViewHolder<Value, CLASS, SettData, VH> viewHolder) {
        if (mGlobalSetting) {
            return;
        }

        boolean b = viewHolder.getIsUseCustomSwitchChecked(mData.getIcon() != null);
        if (b != mData.getCustomEnabled((CLASS) mCallback.getCustomSettingsObject())) {
            mData.setCustomEnabled((CLASS) mCallback.getCustomSettingsObject(), b);
            mData.onValueChanged(mData.getUseCustomId(), (Activity) viewHolder.getBinding().getRoot().getContext(), mGlobalSetting, (CLASS) mCallback.getCustomSettingsObject());
            viewHolder.onUpdateCustomViewDependencies(mData.getIcon() != null, mGlobalSetting);
        }
    }

    private void onShowChangeSetting(VH viewHolder) {
        if (mData.getSettingType() == BaseSetting.SettingType.Normal) {
            if (!mGlobalSetting && !mData.getCustomEnabled((CLASS) mCallback.getCustomSettingsObject())) {
                Toast.makeText(viewHolder.getTopValueContainer().getContext(), R.string.info_enable_setting_to_change_it, Toast.LENGTH_LONG).show();
            } else {
                mData.onShowChangeSetting(viewHolder, mCallback.getParentActivity(), mCallback.getBinding(), mData.getSettData(), mGlobalSetting, (CLASS) mCallback.getCustomSettingsObject());
            }
        } else {
            onShowInfo(viewHolder);
        }
    }

    private void onShowInfo(VH viewHolder) {
        if (mData.getInfo() != null) {
            if (SettingsManager.get().getInfoHandler() != null) {
                if (SettingsManager.get().getInfoHandler().showInfo(mData, viewHolder))
                    return;
            }
            InfoSettingsDialogFragment.Companion.create(
                    mData.isInfoHtml(),
                    mData.getInfo().getText()
            )
                    .show(((FragmentActivity) mCallback.getParentActivity()).getSupportFragmentManager(), null);
        }
    }

    @Override
    public void unbindView(VH holder) {
        super.unbindView(holder);
        holder.unbind(mData);
    }

    // ------------------
    // SubItem
    // ------------------

    @Override
    public Parent getParent() {
        return mParent;
    }

    @Override
    public BaseSettingsItem<Parent, Value, CLASS, SettData, VH> withParent(Parent parent) {
        mParent = parent;
        return this;
    }

    // ------------------
    // Event Hooks
    // ------------------

    public static class EnableSettingsSwitchEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof ISettingsViewHolder) {
                // doesn't matter which view, we use the VH anyways
                return viewHolder.itemView;
            }
            return null;
        }

        /*
         * bind the desired event hook listener to the view
         * use it like following:
         * view.setOnClickListener(view -> { onHandleCustomEvent(...); });
         */
        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            ISettingsViewHolder vh = (ISettingsViewHolder) viewHolder;
            vh.setCustomSwitchListeners((v, b) -> onEventOccurred(v, viewHolder, fastAdapter));
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            // handle the event
            ((BaseSettingsItem) item).onUseCustomChanged((ISettingsViewHolder) viewHolder);
        }
    }

    public static class ShowSettingsEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof BaseBaseSettingViewHolder) {
                return ((ISettingsViewHolder) viewHolder).getRow1();
            }
            return null;
        }

        /*
         * bind the desired event hook listener to the view
         * use it like following:
         * view.setOnClickListener(view -> { onHandleCustomEvent(...); });
         */
        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            view.setOnClickListener(v -> onEventOccurred(v, viewHolder, fastAdapter));
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            // handle the event
            ((BaseSettingsItem) item).onShowChangeSetting(viewHolder);
        }
    }

    public static class ShowInfoEvent extends BaseCustomEventHook<IItem> {
        @Override
        public View onBind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof BaseBaseSettingViewHolder) {
                return ((ISettingsViewHolder) viewHolder).getInfoButton();
            }
            return null;
        }

        /*
         * bind the desired event hook listener to the view
         * use it like following:
         * view.setOnClickListener(view -> { onHandleCustomEvent(...); });
         */
        @Override
        public void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<IItem> fastAdapter) {
            // forward custom event to {@link BaseCustomEventHook#onEventOccurred}
            view.setOnClickListener(v -> onEventOccurred(v, viewHolder, fastAdapter));
        }

        @Override
        public void onEvent(View view, int pos, FastAdapter<IItem> fastAdapter, IItem item, RecyclerView.ViewHolder viewHolder) {
            // handle the event
            ((BaseSettingsItem) item).onShowInfo(viewHolder);
        }
    }
}
