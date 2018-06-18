package com.michaelflisar.recyclerviewpreferences.settings;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.base.SettingsText;
import com.michaelflisar.recyclerviewpreferences.defaults.DefaultSpinnerLayoutProvider;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.SpinnerSettingItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsSpinnerEnumHelper;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.iconics.typeface.IIcon;

import java.util.ArrayList;

/**
 * Created by flisar on 16.05.2017.
 */

public class SpinnerSetting<CLASS, SettData extends ISettData<Integer, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder &
        ISettingsViewHolder<Integer, CLASS, SettData, VH>> extends BaseSetting<Integer, CLASS, SettData, VH> {
    private ISettingsSpinnerEnumHelper mSpinnerHelper;
    private Runnable mRunnableTop = null;
    private Runnable mRunnableBottom = null;
    private ISettingsSpinnerEnumHelper.SpinnerLayoutProvider mLayoutProvider;
    private int mSpinnerMode;
    private Handler mHandler;

    public SpinnerSetting(Class<CLASS> clazz, SettData settData, int title, IIcon icon, ISettingsSpinnerEnumHelper spinnerHelper) {
        this(clazz, settData, new SettingsText(title), icon, spinnerHelper);
    }

    public SpinnerSetting(Class<CLASS> clazz, SettData settData, String title, IIcon icon, ISettingsSpinnerEnumHelper spinnerHelper) {
        this(clazz, settData, new SettingsText(title), icon, spinnerHelper);
    }

    private SpinnerSetting(Class<CLASS> clazz, SettData settData, SettingsText title, IIcon icon, ISettingsSpinnerEnumHelper spinnerHelper) {
        super(clazz, settData, title, icon);
        mSpinnerHelper = spinnerHelper;

        mHandler = new Handler(Looper.getMainLooper());

        if (mSpinnerHelper instanceof ISettingsSpinnerEnumHelper.SpinnerLayoutProvider) {
            mLayoutProvider = (ISettingsSpinnerEnumHelper.SpinnerLayoutProvider) mSpinnerHelper;
        } else {
            mLayoutProvider = new DefaultSpinnerLayoutProvider();
        }

        mSpinnerMode = Spinner.MODE_DROPDOWN;
    }

    public SpinnerSetting withSpinnerMode(int mode) {
        mSpinnerMode = mode;
        return this;
    }

    @Override
    public void updateValueView(boolean topView, VH vh, View v, SettData settData, boolean global, ISettCallback callback) {
        Runnable runnable = null;
        if (topView) {
            runnable = mRunnableTop;
        } else {
            runnable = mRunnableBottom;
        }
        removeCallbacks(v, runnable);

        runnable = () -> {
            AdapterView.OnItemSelectedListener listener = ((Spinner) v).getOnItemSelectedListener();
            ((Spinner) v).setOnItemSelectedListener(null);

            if (((Spinner) v).getAdapter() == null) {
                final boolean isTopLayout = v.getId() == R.id.spValueTop;
                int layout = mLayoutProvider.getLayoutResource(isTopLayout, false);
                int layoutDropdown = mLayoutProvider.getLayoutResource(isTopLayout, true);
                int textViewId = mLayoutProvider.getTextViewResourceId(isTopLayout, true);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), layout, textViewId, new ArrayList<String>()) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        int selectedIndex = getListIndex(getValue((CLASS) callback.getCustomSettingsObject(), global));
                        view = mLayoutProvider.updateLayout(isTopLayout, position, view, false, selectedIndex == position);
                        return view;
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        int selectedIndex = getListIndex(getValue((CLASS) callback.getCustomSettingsObject(), global));
                        view = mLayoutProvider.updateLayout(isTopLayout, position, view, true, selectedIndex == position);
                        return view;
                    }
                };
                adapter.setDropDownViewResource(layoutDropdown);
                adapter.addAll(mSpinnerHelper.getTitles());
                ((Spinner) v).setAdapter(adapter);
            }

            final int selectedIndex = getListIndex(getValue((CLASS) callback.getCustomSettingsObject(), global));
            ((Spinner) v).setSelection(selectedIndex, false);

            ((Spinner) v).setOnItemSelectedListener(listener);
//                v.post(() -> ((Spinner)v).setOnItemSelectedListener(listener));
        };

        post(v, runnable);

        if (topView) {
            mRunnableTop = runnable;
        } else {
            mRunnableBottom = runnable;
        }
    }

    @Override
    public void bind(VH vh) {

    }

    @Override
    public void unbind(VH vh) {
        if (mRunnableTop != null) {
            removeCallbacks(vh.getValueTopView(), mRunnableTop);
        }
        if (mRunnableBottom != null) {
            removeCallbacks(vh.getValueBottomView(), mRunnableBottom);
        }
        ((Spinner) vh.getValueTopView()).setAdapter(null);
        ((Spinner) vh.getValueBottomView()).setAdapter(null);
        mRunnableTop = null;
        mRunnableBottom = null;
    }

    @Override
    public void onShowChangeSetting(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject) {
        // Spinner geht mit EventHook
        // Falls Container geklickt wird, leiten wir das Event an den Spinner weiter
        ((Spinner) vh.getValueTopView()).performClick();

    }

    @Override
    public final int getLayoutTypeId() {
        if (mSpinnerMode == Spinner.MODE_DROPDOWN) {
            return R.id.id_adapter_setting_spinner_item;
        } else {
            return R.id.id_adapter_setting_spinner_dialog_item;
        }
    }

    @Override
    public final int getLayout() {
        if (mSpinnerMode == Spinner.MODE_DROPDOWN) {
            return R.layout.adapter_setting_item_spinner;
        } else {
            return R.layout.adapter_setting_item_spinner_dialog;
        }
    }

    @Override
    public <P extends IItem & IExpandable> BaseSettingsItem<P, Integer, CLASS, SettData, ?> createItem(boolean global, boolean compact, ISettCallback settingsCallback,
            boolean withBottomDivider) {
        return new SpinnerSettingItem(global, compact, this, settingsCallback, withBottomDivider);
    }

//    public final List<String> getItems() {
//        return mItems;
//    }

    public final int getValueAsIndex(CLASS customSettingsObject, boolean global) {
        return getListIndex(getValue(customSettingsObject, global));
    }

    public final boolean setValueByIndex(CLASS customSettingsObject, boolean global, Integer listIndex) {
        return setValue(customSettingsObject, global, getListId(listIndex));
    }

    public final int getListIndex(int listId) {
        return mSpinnerHelper.getListIndex(listId);
    }

    public final int getListId(int index) {
        return mSpinnerHelper.getListId(index);
    }

    // --------------------
    // Callback helpers
    // --------------------

    private final void post(View v, Runnable runnable) {
        if (runnable == null)
            return;
//        v.post(runnable);
        mHandler.post(runnable);
    }

    private final void removeCallbacks(View v, Runnable runnable) {
        if (runnable == null)
            return;
//        v.removeCallbacks(runnable);
        mHandler.removeCallbacks(runnable);
    }
}
