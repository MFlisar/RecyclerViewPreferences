package com.michaelflisar.recyclerviewpreferences.utils;


import android.os.Bundle;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.SettingsGroup;
import com.michaelflisar.recyclerviewpreferences.classes.Dependency;
import com.michaelflisar.recyclerviewpreferences.classes.SettingsDividerDecorator;
import com.michaelflisar.recyclerviewpreferences.classes.SettingsSpaceDecorator;
import com.michaelflisar.recyclerviewpreferences.defaults.Setup;
import com.michaelflisar.recyclerviewpreferences.fastadapter.header.SettingsAlternativeHeaderItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.header.SettingsHeaderItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.header.SettingsMultilevelHeaderItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.BaseSettingsItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.EditTextSettingItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.NumberSettingItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.SpinnerSettingItem;
import com.michaelflisar.recyclerviewpreferences.fastadapter.settings.SwitchSettingItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetup;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.recyclerview.widget.RecyclerView;

public class SettingsUtil {

    public static List<IItem> getAllSettingItems(boolean global, Object customSettingsObject, ISetup setup, String filter, ISettCallback settingsCallback, ArrayList<SettingsGroup> groups,
            boolean forceNoHeaders, Set<Integer> collapsedIds) {
        List<Integer> ids = Util.convertList(groups, group -> group.getGroupId());
        return getSettingItems(global, customSettingsObject, setup, filter, settingsCallback, groups, collapsedIds, forceNoHeaders, ids.toArray(new Integer[ids.size()]));
    }

    public static List<IItem> getSettingItems(boolean global, Object customSettingsObject, ISetup setup, String filter, ISettCallback settingsCallback, Set<Integer> collapsedIds, boolean forceNoHeaders,
            Integer... settingGroupIds) {
        return getSettingItems(global, customSettingsObject, setup, filter, settingsCallback, SettingsManager.get().getTopGroup(), collapsedIds, forceNoHeaders, settingGroupIds);
    }

    private static List<IItem> getSettingItems(boolean global, Object customSettingsObject, ISetup setup, String filter, ISettCallback settingsCallback, List<SettingsGroup> groups, Set<Integer> collapsedIds,
            boolean forceNoHeaders, Integer... settingGroupIds) {
        // Setup
        boolean addIfAnyParentIsInGroup = true;//setup.getSettingsStyle() != Setup.SettingsStyle.MultiLevelList;//global ? useViewPager : true;
        boolean addTopHeaders = settingGroupIds.length > 1;
        boolean showHeadersAsButtons = setup.getSettingsStyle() == Setup.SettingsStyle.MultiLevelList || setup.getSettingsStyle() == Setup.SettingsStyle.MultiLevelGrid;
        AtomicInteger headerId = new AtomicInteger(-2);
        boolean grid = setup.getSettingsStyle() == Setup.SettingsStyle.MultiLevelGrid;
        boolean showTopGroupsInsideGrid = false; // TODO: make this a setup option

        // Function
        List<IItem> items = new ArrayList<>();
        for (Integer settingGroupId : settingGroupIds) {
            items.addAll(getSettingItems(global, customSettingsObject, setup, filter, settingsCallback, null, groups, settingGroupId, addIfAnyParentIsInGroup, addTopHeaders, showHeadersAsButtons, headerId,
                    forceNoHeaders, collapsedIds, grid, showTopGroupsInsideGrid));
        }

        if (setup.isHideSingleHeader()) {
            int countHeaders = 0;
            int countAltHeaders = 0;
            for (IItem item : items) {
                if (item instanceof SettingsHeaderItem) {
                    countHeaders++;
                }
                if (item instanceof SettingsAlternativeHeaderItem) {
                    countAltHeaders++;
                }
            }

            if (countHeaders == 1) {
                for (IItem item : items) {
                    if (item instanceof SettingsHeaderItem) {
                        items = ((SettingsHeaderItem) item).getSubItems();
                        break;
                    }
                }
            }
        }

        return items;
    }

    private static List<IItem> getSettingItems(boolean global, Object customSettingsObject, ISetup setup, String filter, ISettCallback settingsCallback, Boolean anyParentIsGroup, List<SettingsGroup> groups,
            Integer settingGroupId, boolean addIfAnyParentIsInGroup, boolean addTopHeaders, boolean showHeadersAsButtons, AtomicInteger headerId, boolean forceNoHeaders, Set<Integer> collapsedIds,
            boolean grid, boolean showTopGroupsInsideGrid) {

        List<IItem> items = new ArrayList<>();
        for (SettingsGroup group : groups) {
            // 1) check if any parent of this group is a group itself
            boolean localAnyParentIsGroup = (anyParentIsGroup == null || !anyParentIsGroup) ? settingGroupId == group.getGroupId() : anyParentIsGroup;
            // 2) check if this group holds sub groups
            boolean holdsSubGroups = group.isGroupHolder();
            // 3.1) Group holds sub groups
            if (holdsSubGroups) {
                boolean dontAddChildren = false;
                if (!forceNoHeaders && addTopHeaders && localAnyParentIsGroup) {
                    if (!SettingsManager.get().getState().isHideEmptyHeaders() || group.getGroups().size() > 0) {
                        if (showHeadersAsButtons) {
                            // we are at the very top level (group has no parent)
                            if (!showTopGroupsInsideGrid && group.getParent() == null) {
                                SettingsMultilevelHeaderItem header = new SettingsMultilevelHeaderItem(
                                        grid,
                                        group.getIcon(),
                                        group.getTitle(),
                                        headerId.getAndDecrement(),
                                        setup.getFlatStyle()
                                )
                                        .withOnItemClickListener((view, iAdapter, item, i) -> {
                                            settingsCallback.showMultiLevelSetting(group.getGroupId());
                                            return true;
                                        });
                                items.add(header);
                                dontAddChildren = true;
                            } else {
                                items.add(new SettingsHeaderItem(false, group.getTitle(), headerId.getAndDecrement()));
                            }
                        } else {
                            items.add(new SettingsAlternativeHeaderItem(group.getTitle(), headerId.getAndDecrement()));
                        }
                    }
                }
                if (!dontAddChildren) {
                    items.addAll(
                            getSettingItems(
                                    global,
                                    customSettingsObject,
                                    setup,
                                    filter,
                                    settingsCallback,
                                    localAnyParentIsGroup,
                                    group.getGroups(),
                                    settingGroupId,
                                    addIfAnyParentIsInGroup,
                                    addTopHeaders,
                                    showHeadersAsButtons,
                                    headerId,
                                    forceNoHeaders,
                                    collapsedIds,
                                    grid,
                                    showTopGroupsInsideGrid
                            )
                    );
                }
            }
            // 3.2) group does not hold sub groups but only settings
            else {
                if (((addIfAnyParentIsInGroup && localAnyParentIsGroup) || group.check(settingGroupId))) {
                    if (showHeadersAsButtons) {
                        if (!forceNoHeaders) {
                            List<BaseSettingsItem> itemsOfHeader = getItemsOfHeader(group, global, setup, settingsCallback, filter);
                            if (!SettingsManager.get().getState().isHideEmptyHeaders() || itemsOfHeader.size() > 0) {
                                SettingsMultilevelHeaderItem header = new SettingsMultilevelHeaderItem(
                                        grid,
                                        group.getIcon(),
                                        group.getTitle(),
                                        headerId.getAndDecrement(),
                                        setup.getFlatStyle()
                                )
                                        .withOnItemClickListener((view, iAdapter, item, i) -> {
                                            settingsCallback.showMultiLevelSetting(group.getGroupId());
                                            return true;
                                        });
                                items.add(header);
                            }
                        }
                    } else {
                        SettingsHeaderItem header = null;
                        List<BaseSettingsItem> itemsOfHeader = getItemsOfHeader(group, global, setup, settingsCallback, filter);
                        if (SettingsManager.get().getState().isHideEmptyHeaders() && itemsOfHeader.size() == 0) {
                            // skip, we don't need an empty header
                        } else {
                            if (!forceNoHeaders) {
                                header = new SettingsHeaderItem(setup.isUseExpandableHeaders(), group.getTitle(), headerId.getAndDecrement());
                                if ((filter == null || filter.length() == 0) && collapsedIds.contains(group.getGroupId())) {
                                    header.withIsExpanded(false);
                                }
                                if (!group.isHideInLayout()) {
                                    items.add(header);
                                }
                            }

                            if (itemsOfHeader.size() > 0) {
                                itemsOfHeader.get(itemsOfHeader.size() - 1);
                            }
                            if (forceNoHeaders) {
                                items.addAll(itemsOfHeader);
                            } else {
                                if (!group.isHideInLayout()) {
                                    // we always use sub items to be able to switch between expandable and not expandable headers
//                    if (setup.isUseExpandableHeaders()) {
                                    header.withSubItems(itemsOfHeader);
//                    } else {
//                        items.addAll(itemsOfHeader);
//                    }
                                    header.setExpandable(setup.isUseExpandableHeaders());
                                } else {
                                    items.addAll(itemsOfHeader);
                                }
                            }

                            // Update state of all items
                            for (BaseSettingsItem item : itemsOfHeader) {
                                List<Dependency> dependencies = item.getSettings().getDependencies();
                                if (dependencies != null) {
                                    Boolean allEnabled = null;
                                    Boolean allVisible = null;
                                    for (Dependency d : dependencies) {
                                        if (d != null) {
                                            boolean enabled = d.isEnabled(global, customSettingsObject);
                                            boolean visible = d.isVisible(global, customSettingsObject);
                                            if (allEnabled == null) {
                                                allEnabled = enabled;
                                            } else {
                                                allEnabled &= enabled;
                                            }
                                            if (allVisible == null) {
                                                allVisible = visible;
                                            } else {
                                                allVisible &= visible;
                                            }

                                        }
                                    }
                                    if (allEnabled != null && allVisible != null) {
                                        // TODO: setDependencyState mit CheckDependency(global, customSettingsObject) ersetzen
                                        item.setDependencyState(allEnabled, allVisible);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // remove empty headers (sub headers only, empty headers are not added anyways)
        if (SettingsManager.get().getState().isHideEmptyHeaders()) {
            if (items.size() == 1 && items.get(0) instanceof SettingsAlternativeHeaderItem) {
                items.clear();
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

    private static List<BaseSettingsItem> getItemsOfHeader(SettingsGroup group, boolean global, ISetup setup, ISettCallback settingsCallback, String filter) {
        List<BaseSettingsItem> itemsOfHeader = group.getSettingItems(
                global,
                setup.getLayoutStyle() == Setup.LayoutStyle.Compact,
                settingsCallback,
                setup.getFilter(),
                setup.getFlatStyle()
        );
        if (filter != null && filter.length() > 0) {
            String filterLowercase = filter.toLowerCase();
            Iterator<BaseSettingsItem> it = itemsOfHeader.iterator();
            BaseSettingsItem item;
            boolean valid;
            while (it.hasNext()) {
                item = it.next();
                valid = false;
                valid = valid || item.getSettings().getTitle().getText().toLowerCase().contains(filterLowercase);
                valid = valid || (item.getSettings().getSubTitle() != null && item.getSettings().getSubTitle().getText().toLowerCase().contains(filterLowercase));
                if (!valid)
                    it.remove();
            }
        }
        return itemsOfHeader;
    }

    public static SettingsDividerDecorator addDecorator(RecyclerView rv, Setup.LayoutStyle layoutStyle, Setup.DividerStyle dividerStyle) {
        int dp1 = Util.convertDpToPixel(1, rv.getContext());
        rv.addItemDecoration(new SettingsSpaceDecorator(0, dp1 * 0, 0, dp1 * 8)); // row padding is part of the layouts
        if (dividerStyle != Setup.DividerStyle.None) {
            SettingsDividerDecorator dividerItemDecoration = new SettingsDividerDecorator(rv.getContext(), dp1 * 16);
            if (layoutStyle == Setup.LayoutStyle.Normal || dividerStyle == Setup.DividerStyle.Always) {
                rv.addItemDecoration(dividerItemDecoration);
            }
            return dividerItemDecoration;
        } else {
            return null;
        }
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
                    updateExpandableHeaders(((IExpandable) item).getSubItems(), expanded);
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
                    updateCompactMode(((IExpandable) item).getSubItems(), compact);
                } else if (item instanceof ISettingsItem) {
                    ((ISettingsItem) item).setCompactMode(compact);
                }
            }
        }
        return ids;
    }
}