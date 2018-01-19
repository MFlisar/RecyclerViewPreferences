package com.michaelflisar.recyclerviewpreferences.utils;

import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.expandable.ExpandableExtension;

/**
 * Created by flisar on 09.01.2018.
 */

public class FastAdapterUtil {

    public static void expand(FastItemAdapter adapter) {
        if (adapter.getExtensions() == null || adapter.getExtensions().size() == 0) {
            return;
        }
        ExpandableExtension expandableExtension = (ExpandableExtension) adapter.getExtensions().iterator().next();
        if (expandableExtension != null) {
            expandableExtension.expand();
        }
    }

    public static ExpandableExtension getExpandableExtension(FastItemAdapter adapter) {
        if (adapter.getExtensions() == null || adapter.getExtensions().size() == 0) {
            return null;
        }
        ExpandableExtension expandableExtension = (ExpandableExtension) adapter.getExtensions().iterator().next();
        return expandableExtension;
    }
}
