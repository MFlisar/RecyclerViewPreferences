package com.michaelflisar.recyclerviewpreferences.utils;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.SettingsGroup;
import com.michaelflisar.recyclerviewpreferences.classes.Dependency;
import com.michaelflisar.recyclerviewpreferences.classes.SettingsSpaceDecorator;
import com.michaelflisar.recyclerviewpreferences.fastadapter.header.SettingsAlternativeHeaderItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.header.SettingsHeaderItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.EditTextSettingItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.NumberSettingItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.SpinnerSettingItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.SwitchSettingItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsItem;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SettingsUtil {

    public static List<IItem> getAllSettingItems(boolean global, Object customSettingsObject, boolean compact, boolean viewPager, boolean expandableHeaders, ISettCallback settingsCallback, ArrayList<SettingsGroup> groups,
            Set<Integer> collapsedIds) {
        List<Integer> ids = Util.convertList(groups, group -> group.getGroupId());
        return getSettingItems(global, customSettingsObject, compact, viewPager, expandableHeaders, settingsCallback, groups, collapsedIds, ids.toArray(new Integer[ids.size()]));
    }

    public static List<IItem> getSettingItems(boolean global, Object customSettingsObject, boolean compact, boolean viewPager, boolean expandableHeaders, ISettCallback settingsCallback, Set<Integer> collapsedIds, Integer... settingGroupIds) {
        return getSettingItems(global, customSettingsObject, compact, viewPager, expandableHeaders, settingsCallback, SettingsManager.get().getTopGroup(), collapsedIds, settingGroupIds);
    }

    private static List<IItem> getSettingItems(boolean global, Object customSettingsObject, boolean compact, boolean viewPager, boolean expandableHeaders, ISettCallback settingsCallback, List<SettingsGroup> groups, Set<Integer> collapsedIds,
            Integer... settingGroupIds) {
        // Setup
        boolean useViewPager = viewPager;
        boolean addIfAnyParentIsInGroup = true;//global ? useViewPager : true;
        boolean addTopHeaders = /*!global && */settingGroupIds.length > 1;
        AtomicInteger headerId = new AtomicInteger(-2);

        // Function
        List<IItem> items = new ArrayList<>();
        for (Integer settingGroupId : settingGroupIds) {
            items.addAll(getSettingItems(global, customSettingsObject, compact, viewPager, expandableHeaders, settingsCallback, null, groups, settingGroupId, addIfAnyParentIsInGroup, addTopHeaders, headerId, collapsedIds,
                    expandableHeaders));
        }
        return items;
    }

    private static List<IItem> getSettingItems(boolean global, Object customSettingsObject, boolean compact, boolean viewPager, boolean expandableHeaders, ISettCallback settingsCallback, Boolean anyParentIsGroup, List<SettingsGroup> groups,
            Integer settingGroupId, boolean addIfAnyParentIsInGroup, boolean addTopHeaders, AtomicInteger headerId, Set<Integer> collapsedIds, boolean expandableSettings) {

        // Function
        List<IItem> items = new ArrayList<>();
        for (SettingsGroup group : groups) {
            boolean localAnyParentIsGroup = (anyParentIsGroup == null || !anyParentIsGroup) ? settingGroupId == group.getGroupId() : anyParentIsGroup;
            if (group.isGroupHolder()) {
                if (addTopHeaders && localAnyParentIsGroup) {
                    items.add(new SettingsAlternativeHeaderItem(group.getTitle(), headerId.getAndDecrement()));
                }
                items.addAll(getSettingItems(global, customSettingsObject, compact, viewPager, expandableHeaders, settingsCallback, localAnyParentIsGroup, group.getGroups(), settingGroupId, addIfAnyParentIsInGroup, addTopHeaders,
                        headerId, collapsedIds,
                        expandableSettings));
            } else {
                if ((addIfAnyParentIsInGroup && localAnyParentIsGroup) || group.check(settingGroupId)) {
                    SettingsHeaderItem header = new SettingsHeaderItem(expandableHeaders, group.getIcon(), group.getTitle(), headerId.getAndDecrement());
                    if (collapsedIds.contains(group.getTitle())) {
                        header.withIsExpanded(false);
                    }
                    items.add(header);
                    List<BaseSettingsItem> itemsOfHeader = group.getSettingItems(global, compact, settingsCallback, true);
                    if (itemsOfHeader.size() > 0) {
                        itemsOfHeader.get(itemsOfHeader.size() - 1).withBottomDivider(false);
                    }
                    if (expandableSettings && expandableHeaders) {
                        header.withSubItems(itemsOfHeader);
                    } else {
                        items.addAll(itemsOfHeader);
                    }

                    // Update state of all items
                    for (BaseSettingsItem item : itemsOfHeader) {
                        Dependency dependency = item.getSettings().getDependency();
                        if (dependency != null) {
                            boolean enabled = dependency.isEnabled(global, customSettingsObject);
                            boolean visible = dependency.isVisible(global, customSettingsObject);
                            // TODO: setDependencyState mit CheckDependency(global, customSettingsObject) ersetzen
                            item.setDependencyState(enabled, visible);
                        }
                    }
                }
            }
        }
        return items;
    }

    public static List<ISetting> getAllSettings(List<SettingsGroup> groups) {
        List<ISetting> settings = new ArrayList<>();
        for (SettingsGroup group : groups) {
            if (group.isGroupHolder()) {
                settings.addAll(getAllSettings(group.getGroups()));
            } else {
                settings.addAll(group.getSettings());
            }
        }
        return settings;
    }

    // ---------------------------
    // Hilfsfunktionen
    // ---------------------------

    public static void addDecorator(RecyclerView rv) {
        // TODO: Header in Cards + Space Decorator
        int dp1 = Util.convertDpToPixel(1, rv.getContext());
        rv.addItemDecoration(new SettingsSpaceDecorator(dp1 * 8, dp1 * 16, dp1 * 8));
    }

    public static FastItemAdapter<IItem> initAdapter(FastItemAdapter<IItem> adapter, List<IItem> items, Bundle savedInstanceState, String fastAdapterBundlePrefix) {
        adapter.withEventHook(new BaseSettingsItem.EnableSettingsSwitchEvent());
        adapter.withEventHook(new BaseSettingsItem.ShowSettingsEvent());
        adapter.withEventHook(new BaseSettingsItem.ShowInfoEvent());
        adapter.withEventHook(new SpinnerSettingItem.SettingsSpinnerTopEvent());
        adapter.withEventHook(new SpinnerSettingItem.SettingsSpinnerBottomEvent());
        adapter.withEventHook(new SwitchSettingItem.SettingsSwitchBottomEvent());
        adapter.withEventHook(new SwitchSettingItem.SettingsSwitchTopEvent());
        adapter.withEventHook(new EditTextSettingItem.SettingsEditTextTopEvent());
        adapter.withEventHook(new EditTextSettingItem.SettingsEditTextBottomEvent());
        adapter.withEventHook(new NumberSettingItem.SettingsSeekbarTopEvent());
        adapter.withPositionBasedStateManagement(false);
        setNewList(adapter, items, savedInstanceState, fastAdapterBundlePrefix);
        return adapter;
    }

    public static void setNewList(FastItemAdapter<IItem> adapter, List<IItem> items, Bundle savedInstanceState, String fastAdapterBundlePrefix) {
        Bundle expandedStates = getFastAdapterBundle(savedInstanceState, fastAdapterBundlePrefix);
        if (expandedStates == null) {
            expandedStates = getExpandedIdsBundleAndResetExpanded(items, fastAdapterBundlePrefix);
        } else {
            // collapse all items again!
            setExpandedState(items, false);
        }
        adapter.setNewList(items);
        adapter.withSavedInstanceState(expandedStates, fastAdapterBundlePrefix);
    }

    private static Bundle getFastAdapterBundle(Bundle bundle, String fastAdapterBundlePrefix) {
        if (bundle != null && bundle.containsKey("bundle_expanded" + fastAdapterBundlePrefix)) {
            return bundle;
        }
        return null;
    }

    private static Bundle getExpandedIdsBundleAndResetExpanded(List<IItem> items, String fastAdapterBundlePrefix) {
        Bundle b = new Bundle();
        ArrayList<String> ids = getExpandedIdsListAndResetExpanded(items);
        b.putStringArrayList("bundle_expanded" + fastAdapterBundlePrefix, ids);
        return b;
    }

    private static ArrayList<String> getExpandedIdsListAndResetExpanded(List<IItem> items) {
        ArrayList<String> ids = new ArrayList<>();
        if (items != null) {
            for (IItem item : items) {
                if (item instanceof IExpandable && ((IExpandable) item).isExpanded()) {
                    ids.add(String.valueOf(item.getIdentifier()));
                    ids.addAll(getExpandedIdsListAndResetExpanded(((IExpandable) item).getSubItems()));
                    ((IExpandable) item).withIsExpanded(false);
                }
            }
        }
        return ids;
    }

    public static ArrayList<String> setExpandedState(List<IItem> items, boolean expanded) {
        ArrayList<String> ids = new ArrayList<>();
        if (items != null) {
            for (IItem item : items) {
                if (item instanceof IExpandable) {
                    ((IExpandable) item).withIsExpanded(expanded);
                }
            }
        }
        return ids;
    }

    public static ArrayList<Long> updateExpandableHeaders(List<IItem> items, boolean expanded) {
        ArrayList<Long> ids = new ArrayList<>();
        if (items != null) {
            for (IItem item : items) {
                if (item instanceof SettingsHeaderItem) {
                    ((SettingsHeaderItem) item).setExpandable(expanded);
                    ids.add(item.getIdentifier());
                } else if (item instanceof IExpandable) {
                    updateExpandableHeaders(((IExpandable)item).getSubItems(), expanded);
                }
            }
        }
        return ids;
    }

    public static ArrayList<Long> updateCompactMode(List<IItem> items, boolean compact) {
        ArrayList<Long> ids = new ArrayList<>();
        if (items != null) {
            for (IItem item : items) {
                if (item instanceof IExpandable) {
                    updateCompactMode(((IExpandable)item).getSubItems(), compact);
                } else if (item instanceof ISettingsItem) {
                    ((ISettingsItem)item).setCompactMode(compact);
                }
            }
        }
        return ids;
    }

}
