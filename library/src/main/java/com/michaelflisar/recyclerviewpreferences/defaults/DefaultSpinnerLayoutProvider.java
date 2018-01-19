package com.michaelflisar.recyclerviewpreferences.defaults;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsSpinnerEnumHelper;

/**
 * Created by flisar on 18.01.2018.
 */

public class DefaultSpinnerLayoutProvider implements ISettingsSpinnerEnumHelper.SpinnerLayoutProvider {

    private final boolean highlightSelected;

    public DefaultSpinnerLayoutProvider() {
        highlightSelected = SettingsManager.get().getState().supportSpinnerDropDownHighlighting();
    }

    @Override
    public int getLayoutResource(boolean topLayout, boolean dropdown) {
        if (dropdown) {
            return R.layout.support_simple_spinner_dropdown_item;
        } else {
            return R.layout.spinner_main_view;
        }
    }

    @Override
    public int getTextViewResourceId(boolean topLayout, boolean dropdown) {
        // the adapter will handle the view as TextView (that's how ArrayAdapter handles this) if we return 0
        return 0;
    }

    @Override
    public View updateLayout(boolean topLayout, int position, View view, boolean dropdown, boolean selected) {
        TextView tv = (TextView) view;
        if (topLayout && !dropdown) {
            tv.setTypeface(null, Typeface.BOLD);
        } else {
            if (highlightSelected) {
                tv.setTypeface(null, selected ? Typeface.BOLD : Typeface.NORMAL);
            }
        }
        return view;
    }
}