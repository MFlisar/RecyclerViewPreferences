package com.michaelflisar.recyclerviewpreferences.filters;

import android.os.Parcel;

import com.michaelflisar.recyclerviewpreferences.base.BaseSetting;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flisar on 19.02.2018.
 */

public class SupportTypeFilter implements ISetup.IFilter {

    private ArrayList<BaseSetting.SupportType> mTypes;

    public SupportTypeFilter() {
        mTypes = new ArrayList<>();
    }

    protected SupportTypeFilter(Parcel in) {
        List<Integer> types = new ArrayList<>();
        in.readList(types, null);
        mTypes = new ArrayList<>();
        for (Integer type : types) {
            mTypes.add(BaseSetting.SupportType.values()[type]);
        }
    }

    public SupportTypeFilter add(BaseSetting.SupportType type) {
        if (!mTypes.contains(type)) {
            mTypes.add(type);
        }
        return this;
    }

    @Override
    public boolean isEnabled(BaseSettingsItem item) {
        return mTypes.contains(item.getSettings().getSupportType());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        List<Integer> types = new ArrayList<>();
        for (BaseSetting.SupportType type : mTypes) {
            types.add(type.ordinal());
        }
        dest.writeList(mTypes);
    }

    public static final Creator<SupportTypeFilter> CREATOR = new Creator<SupportTypeFilter>() {
        @Override
        public SupportTypeFilter createFromParcel(Parcel in) {
            return new SupportTypeFilter(in);
        }

        @Override
        public SupportTypeFilter[] newArray(int size) {
            return new SupportTypeFilter[size];
        }
    };
}
