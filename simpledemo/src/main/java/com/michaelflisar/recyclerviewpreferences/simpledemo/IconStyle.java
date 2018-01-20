package com.michaelflisar.recyclerviewpreferences.simpledemo;

import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsSpinnerEnumHelper;
import com.michaelflisar.recyclerviewpreferences.utils.Util;

import java.util.List;

/**
 * Created by flisar on 21.08.2017.
 */

public enum IconStyle {
    Normal(0, "Normal"),
    Round(1, "Round"),
    Rectangle(2, "Rectangle");

    private int mId;
    private String mName;

    IconStyle(int id, String name) {
        mId = id;
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public static class EnumHelper implements ISettingsSpinnerEnumHelper {
        @Override
        public List<String> getTitles() {
            return Util.convertList(IconStyle.values(), iconStyle -> iconStyle.getName());
        }

        @Override
        public int getListIndex(int listId) {
            return Util.indexOf(IconStyle.values(), iconStyle -> iconStyle.getId() == listId);
        }

        @Override
        public int getListId(int index) {
            return IconStyle.values()[index].getId();
        }
    }
}
