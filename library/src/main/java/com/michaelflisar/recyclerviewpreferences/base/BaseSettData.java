package com.michaelflisar.recyclerviewpreferences.base;

import android.app.Activity;
import android.widget.Spinner;

import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsSpinnerEnumHelper;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.settings.BooleanSetting;
import com.michaelflisar.recyclerviewpreferences.settings.ColorSetting;
import com.michaelflisar.recyclerviewpreferences.settings.NumberSetting;
import com.michaelflisar.recyclerviewpreferences.settings.SpinnerSetting;
import com.michaelflisar.recyclerviewpreferences.settings.TextDialogSetting;
import com.mikepenz.iconics.typeface.IIcon;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseSettData<
        Data,
        Value,
        SettData extends ISettData<Value, Data, SettData, VH>,
        VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Value, Data, SettData, VH>>
        implements ISettData<Value, Data, SettData, VH>, ISettData.IValueChangedListener<Data> {

    protected NumberSetting.Mode mNumberSettingMode = NumberSetting.Mode.DialogSeekbar;

    public int mTitle;
    public IIcon mIIcon;
    public int mSubTitle = -1;
    public int mIconPadding = 0;
    public Integer mIconColor = null;
    public Integer mTitleColor = null;
    public Integer mTextColor = null;
    //    public Integer mBackgroundTint = null;
    private Type mType;
    private IsEnabled mIsEnabled;
    private SetEnabled mSetEnabled;
    private GetValue<Value, Data> mGetValue;
    private SetValue<Value, Data> mSetValue;
    private GetGlobalValue<Value> mGetGlobalValue;
    private SetGlobalValue<Value> mSetGlobalValue;
    private ValueChanged<Data> mValueChanged;
    public int mInfoText = -1;
    public boolean mIsInfoHtml = false;
    public BaseSetting.SupportType mSupportType = BaseSetting.SupportType.Normal;
    public BaseSetting.SettingType mSettingType = BaseSetting.SettingType.Normal;
    // Text
    private boolean mAllowEmptyText = false;
    // Number
    private Integer mMin;
    private Integer mMax;
    private Integer mStepSize;
    private Integer mUnit;
    // List
    protected ISettingsSpinnerEnumHelper mSpinnerEnumHelper;
    protected int mSpinnerMode;

    public BaseSettData() {

    }

    protected void init(Type type, int prefId, int title, IIcon icon, IsEnabled isEnabled, SetEnabled setEnabled, GetValue<Value, Data> getValue, SetValue<Value, Data> setValue, ValueChanged<Data> valueChanged) {
        mType = type;
//        mPrefId = prefId;
        mTitle = title;
        mIIcon = icon;
        mIsEnabled = isEnabled;
        mSetEnabled = setEnabled;
        mGetValue = getValue;
        mSetValue = setValue;
        mValueChanged = valueChanged;
    }

    public void createIntData(int prefId, int title, IIcon icon, ValueChanged<Data> valueChanged, int min, int max, int steps, Integer unit) {
        init(Type.Number, prefId, title, icon, null, null, null, null, valueChanged);
        mMin = min;
        mMax = max;
        mStepSize = steps;
        mUnit = unit;
    }

    public void createColorData(int prefId, int title, IIcon icon, ValueChanged<Data> valueChanged) {
        init(Type.Color, prefId, title, icon, null, null, null, null, valueChanged);
    }

    public void createBoolData(int prefId, int title, IIcon icon, ValueChanged<Data> valueChanged) {
        init(Type.Boolean, prefId, title, icon, null, null, null, null, valueChanged);
    }

    public void createStringData(int prefId, int title, IIcon icon, ValueChanged<Data> valueChanged) {
        init(Type.Text, prefId, title, icon, null, null, null, null, valueChanged);
    }

    public void createListData(int prefId, int title, IIcon icon, ValueChanged<Data> valueChanged, ISettingsSpinnerEnumHelper spinnerEnumHelper, boolean spinnerAsDialog) {
        init(Type.List, prefId, title, icon, null, null, null, null, valueChanged);
        mSpinnerEnumHelper = spinnerEnumHelper;
        mSpinnerMode = spinnerAsDialog ? Spinner.MODE_DIALOG : Spinner.MODE_DROPDOWN;
    }

    public BaseSettData<Data, Value, SettData, VH> withGlobal(GetGlobalValue<Value> getValue, SetGlobalValue<Value> setValue) {
        mGetGlobalValue = getValue;
        mSetGlobalValue = setValue;
        return this;
    }

    public BaseSettData<Data, Value, SettData, VH> withSupportType(BaseSetting.SupportType supportType) {
        mSupportType = supportType;
        return this;
    }

    public BaseSettData<Data, Value, SettData, VH> withSettingType(BaseSetting.SettingType settingType) {
        mSettingType = settingType;
        return this;
    }

    public BaseSettData<Data, Value, SettData, VH> withCustom(GetValue<Value, Data> getValue, SetValue<Value, Data> setValue) {
        mGetValue = getValue;
        mSetValue = setValue;
        return this;
    }

    public BaseSettData<Data, Value, SettData, VH> withCustomEnabled(IsEnabled isEnabled, SetEnabled setEnabled) {
        mIsEnabled = isEnabled;
        mSetEnabled = setEnabled;
        return this;
    }

    public BaseSettData<Data, Value, SettData, VH> withCustomAll(GetValue<Value, Data> getValue, SetValue<Value, Data> setValue, IsEnabled isEnabled, SetEnabled setEnabled) {
        withCustom(getValue, setValue);
        withCustomEnabled(isEnabled, setEnabled);
        return this;
    }


    public BaseSettData<Data, Value, SettData, VH> withCustomAlwaysEnabled() {
        mIsEnabled = data -> true;
        mSetEnabled = (data, enabled) -> {
        };
        return this;
    }

    public BaseSettData withInfo(int info, boolean isHtml) {
        mInfoText = info;
        mIsInfoHtml = isHtml;
        return this;
    }

    public BaseSettData withSubTitle(int subTitle) {
        mSubTitle = subTitle;
        return this;
    }

    public BaseSettData withIconPaddingDp(int iconPadding) {
        mIconPadding = iconPadding;
        return this;
    }

    public BaseSettData withIconTinting(int iconColor) {
        mIconColor = iconColor;
        return this;
    }

//    public BaseSettData withBackgroundTint(int color) {
//        mBackgroundTint = color;
//        return this;
//    }

    public BaseSettData withTitleTextColor(int color) {
        mTitleColor = color;
        return this;
    }

    public BaseSettData withTextColor(int color) {
        mTextColor = color;
        return this;
    }

    public BaseSettData withAllowEmptyText(boolean allowEmptyText) {
        mAllowEmptyText = allowEmptyText;
        return this;
    }

    public BaseSetting createSetting() {
        BaseSetting setting;
        switch (mType) {
            case Boolean:
                setting = new BooleanSetting(BaseSettData.class, this, mTitle, mIIcon);
                break;
            case Number:
                setting = new NumberSetting(BaseSettData.class, this, mTitle, mIIcon, mNumberSettingMode, mMin, mMax, mStepSize, mUnit);
                break;
            case List:
                setting = new SpinnerSetting(BaseSettData.class, this, mTitle, mIIcon, mSpinnerEnumHelper).withSpinnerMode(mSpinnerMode);
                break;
            case Text:
                setting = new TextDialogSetting(BaseSettData.class, this, mTitle, mIIcon).withAllowEmptyInput(mAllowEmptyText);
                break;
            case Color:
                setting = new ColorSetting(BaseSettData.class, this, mTitle, mIIcon);
                break;
            default:
                throw new RuntimeException("Type not handled!");
        }
        setting.withIconSetup(mIconPadding != -1 ? mIconPadding : 0, mIconColor);
        if (mSubTitle != -1) {
            setting.withSubTitle(mSubTitle);
        }
        if (mInfoText != -1) {
            setting.withInfo(mInfoText, mIsInfoHtml);
        }
        if (mTitleColor != null) {
            setting.withTitleTextColor(mTitleColor);
        }
        if (mTextColor != null) {
            setting.withTextColor(mTextColor);
        }
//        if (mBackgroundTint != null) {
//            setting.withBackgroundTint(mBackgroundTint);
//        }
        setting.withSupportType(mSupportType);
        setting.withSettingType(mSettingType);
        return setting;
    }

    // -----------------
    // BlacklistSetting Create Funktionen
    // -----------------

    @Override
    public boolean getCustomEnabled(Data data) {
        if (mIsEnabled == null) {
            return false;
        } else {
            return mIsEnabled.isCustomEnabled(data);
        }
    }

    // -----------------
    // functions
    // -----------------

    @Override
    public void setCustomEnabled(Data data, boolean enabled) {
        if (mSetEnabled != null) {
            mSetEnabled.setCustomEnabled(data, enabled);
        }
    }

    @Override
    public Value getValue(Data data, boolean global) {
        if (global) {
            return mGetGlobalValue.getValue();
        } else {
            return mGetValue.getValue(data);
        }
    }

    @Override
    public boolean setValue(Data data, boolean global, Value value) {
        if (global) {
            mSetGlobalValue.setValue(value);
        } else {
            mSetValue.setValue(data, value);
        }
        return true;
    }

    @Override
    public void onValueChanged(int id, Activity activity, boolean global, Data customSettingsObject) {
        if (mValueChanged != null) {
            mValueChanged.onValueChanged(id, activity, global, customSettingsObject);
        }
    }

    public enum Type {
        Boolean,
        Number,
        List,
        Text,
        Color
    }

    public interface IsEnabled<Data> {
        boolean isCustomEnabled(Data data);
    }

    public interface SetEnabled<Data> {
        void setCustomEnabled(Data data, boolean enabled);
    }

    public interface GetValue<Value, Data> {
        Value getValue(Data data);
    }

    public interface SetValue<Value, Data> {
        void setValue(Data data, Value value);
    }

    public interface GetGlobalValue<Value> {
        Value getValue();
    }

    public interface SetGlobalValue<Value> {
        boolean setValue(Value value);
    }

    public interface ValueChanged<Data> {
        void onValueChanged(int id, Activity activity, boolean global, Data customSettingsObject);
    }
}

