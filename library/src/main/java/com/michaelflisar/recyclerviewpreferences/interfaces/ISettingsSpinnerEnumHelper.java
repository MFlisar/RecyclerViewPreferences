package com.michaelflisar.recyclerviewpreferences.interfaces;


import android.view.View;

import java.util.List;

public interface ISettingsSpinnerEnumHelper {

    List<String> getTitles();

    int getListIndex(int listId);

    int getListId(int index);

    interface SpinnerLayoutProvider {

        int getLayoutResource(boolean topLayout, boolean dropdown);

        /*
       * return 0 if your layout is a TextView, otherwise the id of the TextView in your layout
       */
        int getTextViewResourceId(boolean topLayout, boolean dropdown);

        View updateLayout(boolean topLayout, int position, View view, boolean dropdown, boolean selected);
    }
}
