package com.michaelflisar.recyclerviewpreferences.simpledemo;

import android.view.View;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.defaults.DefaultSpinnerLayoutProvider;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsSpinnerEnumHelper;
import com.michaelflisar.recyclerviewpreferences.simpledemo.R;
import com.michaelflisar.recyclerviewpreferences.utils.Util;

import java.util.List;

/**
 * Created by flisar on 21.08.2017.
 */

public enum IconStyleWithIcon {
    Normal(0, "Normal"),
    Round(1, "Round"),
    Rectangle(2, "Rectangle"),
    Test1(3, "Test1"),
    Test2(4, "Test2"),
    Test3(5, "Test3"),
    Test4(6, "Test4"),
    Test5(7, "Test5"),
    Test6(8, "Test6"),
    Test7(9, "Test7"),
    Test8(10, "Test8"),
    Test9(11, "Test9"),
    Test10(12, "Test10"),
    Test11(13, "Test11"),
    Test12(14, "Test12"),
    Test13(15, "Test13"),
    Test14(16, "Test14"),
    Test15(17, "Test15");

    private int mId;
    private String mName;

    IconStyleWithIcon(int id, String name) {
        mId = id;
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public static class EnumHelper extends DefaultSpinnerLayoutProvider implements ISettingsSpinnerEnumHelper, ISettingsSpinnerEnumHelper.SpinnerLayoutProvider /* optional interface */ {
        @Override
        public List<String> getTitles() {
            return Util.convertList(IconStyleWithIcon.values(), iconStyle -> iconStyle.getName());
        }

        @Override
        public int getListIndex(int listId) {
            return Util.indexOf(IconStyleWithIcon.values(), iconStyle -> iconStyle.getId() == listId);
        }

        @Override
        public int getListId(int index) {
            return IconStyleWithIcon.values()[index].getId();
        }

        // OPTIONAL SpinnerLayoutProvider interface => we use it to provide an icon

        // here we add a icon to the TextView
        @Override
        public View updateLayout(boolean topLayout, int position, View view, boolean dropdown, boolean selected) {
            // the default layout is an TextView
            TextView tv = (TextView) view;
            tv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_launcher, 0, 0, 0);
            return view;
        }
    }
}
