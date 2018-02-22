package com.michaelflisar.recyclerviewpreferences.utils;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.expandable.ExpandableExtension;

/**
 * Created by flisar on 09.01.2018.
 */

public class FastAdapterUtil {

    public static <T extends IItem> void expand(FastItemAdapter<T> adapter) {
        if (adapter.getExtensions() == null || adapter.getExtensions().size() == 0) {
            return;
        }
        ExpandableExtension expandableExtension = getExpandableExtension(adapter);
        if (expandableExtension != null) {
            expandableExtension.expand();
        }
    }

    public static <T extends IItem> ExpandableExtension<T> getExpandableExtension(FastItemAdapter<T> adapter) {
        if (adapter.getExtensions() == null || adapter.getExtensions().size() == 0) {
            return null;
        }
        return adapter.getExtension(ExpandableExtension.class);
    }
}
