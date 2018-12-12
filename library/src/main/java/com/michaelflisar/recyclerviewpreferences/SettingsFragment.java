package com.michaelflisar.recyclerviewpreferences;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.michaelflisar.recyclerviewpreferences.activity.MultiLevelSingleSettingsGroupActivity;
import com.michaelflisar.recyclerviewpreferences.base.SettingsGroup;
import com.michaelflisar.recyclerviewpreferences.classes.AdvancedFragmentStatePagerAdapter;
import com.michaelflisar.recyclerviewpreferences.classes.SettingsDividerDecorator;
import com.michaelflisar.recyclerviewpreferences.classes.SettingsFragmentManager;
import com.michaelflisar.recyclerviewpreferences.databinding.SettingsFragmentBinding;
import com.michaelflisar.recyclerviewpreferences.databinding.SettingsViewpagerFragmentBinding;
import com.michaelflisar.recyclerviewpreferences.defaults.Setup;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsFragment;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsFragmentParent;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsItem;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetup;
import com.michaelflisar.recyclerviewpreferences.utils.FastAdapterUtil;
import com.michaelflisar.recyclerviewpreferences.utils.SettingsUtil;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.expandable.ExpandableExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SettingsFragment extends Fragment implements ISettCallback, ISettingsFragment {

    private static final SettingsFragment internalCreate(ISetup setup, boolean globalSetting, String fastAdapterPrefix, boolean isPage, boolean isMultiLevelPage, ArrayList<Integer> groupIds) {
        SettingsFragment f = new SettingsFragment();
        Bundle args = new Bundle();
        args.putParcelable("setup", setup);
        args.putBoolean("globalSetting", globalSetting);
        args.putString("fastAdapterPrefix", fastAdapterPrefix);
        args.putBoolean("globalSetting", globalSetting);
        args.putBoolean("isPage", isPage);
        args.putBoolean("isMultiLevelPage", isMultiLevelPage);
        args.putIntegerArrayList("groupIds", groupIds);
        f.setArguments(args);
        return f;
    }

    ISetup setup;
    boolean globalSetting;
    ArrayList<Integer> groupIds;
    String fastAdapterPrefix;
    Boolean isPage;
    Boolean isMultiLevelPage;

    private PagerAdapter mPagerAdapter;
    private List<SettingsGroup> mGroups;
    protected List<ISetting> mSettings;
    private ViewDataBinding mBinding;
    protected FastItemAdapter mFastItemAdapter;
    protected List<IItem> mItems;
    private SettingsFragmentManager mManager;
    private SettingsDividerDecorator mDividerDecorator;

    public static SettingsFragment create(ISetup setup, boolean globalSetting, ArrayList<Integer> groupIds) {
        return create(setup, globalSetting, false, groupIds);
    }

    public static SettingsFragment create(ISetup setup, boolean globalSetting, Integer... groupId) {
        return create(setup, globalSetting, new ArrayList<>(Arrays.asList(groupId)));
    }

    public static SettingsFragment createMultiLevelPage(ISetup setup, boolean globalSetting, Integer groupId) {
        return create(setup, globalSetting, true, new ArrayList<>(Arrays.asList(groupId)));
    }

    static SettingsFragment create(ISetup setup, boolean globalSetting, boolean isMultiLevelPage, ArrayList<Integer> groupIds) {
        return internalCreate(setup, globalSetting, UUID.randomUUID().toString(), false, isMultiLevelPage, groupIds);
    }

    static SettingsFragment createPage(ISetup setup, boolean globalSetting, int groupId) {
        return internalCreate(setup, globalSetting, UUID.randomUUID().toString(), true, false, new ArrayList<>(Arrays.asList(groupId)));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setup = getArguments().getParcelable("setup");
        globalSetting = getArguments().getBoolean("globalSetting");
        fastAdapterPrefix = getArguments().getString("fastAdapterPrefix");
        globalSetting = getArguments().getBoolean("globalSetting");
        isPage = getArguments().getBoolean("isPage");
        isMultiLevelPage = getArguments().getBoolean("isMultiLevelPage");
        groupIds = getArguments().getIntegerArrayList("groupIds");

        mGroups = new ArrayList<>();
        mSettings = new ArrayList<>();
        for (Integer id : groupIds) {
            mGroups.add(SettingsManager.get().getGroupById(id));
            mSettings.addAll(SettingsManager.get().getSettingsByGroupId(id));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (isViewPager()) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.settings_viewpager_fragment, container, false);
        } else {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false);
        }
        recreateView(savedInstanceState, true);
        SettingsFragmentInstanceManager.get().register(this);
        return mBinding.getRoot();
    }

    private void recreateView(Bundle savedInstanceState, boolean init) {
        if (isViewPager()) {
            SettingsViewpagerFragmentBinding binding = (SettingsViewpagerFragmentBinding) mBinding;
            if (init) {
                mPagerAdapter = new PagerAdapter(getChildFragmentManager());
                if (SettingsManager.get().getState().hasScrollablePagerTabs()) {
                    binding.tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
                }
                binding.vpSettings.setAdapter(mPagerAdapter);
                binding.tabs.setupWithViewPager(binding.vpSettings);
            }
        } else {
            SettingsFragmentBinding binding = (SettingsFragmentBinding) mBinding;
            ISetup pageSetup = setup;
            // no view pager, pages always use a list!
            if (pageSetup.getSettingsStyle() == Setup.SettingsStyle.ViewPager) {
                pageSetup = setup.copy();
                pageSetup.setSettingsStyle(pageSetup.getSettingsStyle().getPageStyle());
            }

            if (init) {
                mFastItemAdapter = new FastItemAdapter<>();
                if (pageSetup.isUseExpandableHeaders()) {
                    ExpandableExtension expandableExtension = new ExpandableExtension<>();
                    mFastItemAdapter.addExtension(expandableExtension);
                }
                mManager = new SettingsFragmentManager(getActivity(), mFastItemAdapter, mSettings);
            }

            mItems = SettingsUtil.getSettingItems(
                    globalSetting,
                    getCustomSettingsObject(),
                    pageSetup,
                    this,
                    SettingsManager.get().getCollapsedSettingIds(),
                    isMultiLevelPage,
                    groupIds.toArray(new Integer[groupIds.size()])
            );

            if (init) {
                SettingsUtil.initAdapter(mFastItemAdapter, mItems, savedInstanceState, fastAdapterPrefix);
                binding.rvSettings.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), RecyclerView.VERTICAL, false));
                binding.rvSettings.setAdapter(mFastItemAdapter);
                mDividerDecorator = SettingsUtil.addDecorator(binding.rvSettings, pageSetup.getLayoutStyle(), pageSetup.getDividerStyle());
            } else {
                SettingsUtil.setNewList(mFastItemAdapter, mItems, savedInstanceState, fastAdapterPrefix);
            }
        }
    }

    public void notifyCustomObjectChanged() {
        recreateView(null, false);
    }

    public void setUseExpandableHeaders(boolean enabled) {
        if (isViewPager()) {
            Collection<Fragment> fragments = mPagerAdapter.getAllCachedFragments();
            for (Fragment f : fragments) {
                ((SettingsFragment) f).setUseExpandableHeaders(enabled);
            }
        } else {
            setup.setUseExpandableHeaders(enabled);
            getArguments().putParcelable("setup", setup);
            if (mItems != null) {
                if (!enabled) {
                    FastAdapterUtil.expand(mFastItemAdapter);
                }
                SettingsUtil.updateExpandableHeaders(mItems, enabled);
                SettingsUtil.setExpandedState(mItems, true);
                mFastItemAdapter.notifyAdapterDataSetChanged();
            }
        }
    }

    public void setUseCompactSettings(boolean enabled) {
        if (isViewPager()) {
            Collection<Fragment> fragments = mPagerAdapter.getAllCachedFragments();
            for (Fragment f : fragments) {
                ((SettingsFragment) f).setUseCompactSettings(enabled);
            }
        } else {
            setup.setLayoutStyle(enabled ? Setup.LayoutStyle.Compact : Setup.LayoutStyle.Normal);
            getArguments().putParcelable("setup", setup);
            if (mDividerDecorator != null) {
                if (setup.getDividerStyle() == Setup.DividerStyle.NotForSimpleOrCompactMode) {
                    RecyclerView rv = getRecyclerView();
                    if (rv != null) {
                        if (enabled) {
                            rv.removeItemDecoration(mDividerDecorator);
                        } else {
                            rv.addItemDecoration(mDividerDecorator);
                        }
                    }
                }
            }
            if (mItems != null) {
                SettingsUtil.updateCompactMode(mItems, enabled);
                mFastItemAdapter.notifyAdapterDataSetChanged();
            }
        }
    }

//    public void setSettingsStyle(Setup.SettingsStyle style) {
//        setup.setSettingsStyle(style);
//        // recreate fragment view
////        SettingsFragment newFragment = new SettingsFragment();
////        newFragment.setArguments(getArguments());
////        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
////        int containerId = getId();
////        getActivity().getSupportFragmentManager().beginTransaction().replace(containerId, newFragment).commit();
//    }

    public boolean updateViews(final Integer id) {
        if (id != null) {
            if (mFastItemAdapter != null) {
                List<IItem> items = mFastItemAdapter.getAdapterItems();
                int index = Util.indexOf(items, item -> item instanceof ISettingsItem && ((ISettingsItem) item).getSettings().checkId(id));
                if (index != -1) {
                    mFastItemAdapter.notifyAdapterItemChanged(index);
                    return true;
                }
            }
        } else {
//            for (ISetting setting : mSettings)
//                setting.init(mViews.get(setting.getParentId()), settData, global, this, this, this);
        }
        return false;
    }

    @Override
    public boolean isViewPager() {
        return !isPage && setup.getSettingsStyle() == Setup.SettingsStyle.ViewPager;
    }

    @Override
    public void onDestroyView() {
        SettingsFragmentInstanceManager.get().unregister(this);
        if (mManager != null) {
            mManager = null;
        }
        if (mBinding != null) {
            mBinding.unbind();
            mBinding = null;
        }
        mFastItemAdapter = null;
        mItems = null;
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mFastItemAdapter != null) {
            mFastItemAdapter.saveInstanceState(outState, fastAdapterPrefix);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public SettingsFragmentManager getSettingsManager() {
        if (isViewPager()) {
            return ((SettingsFragment) mPagerAdapter.getFragmentAt(((SettingsViewpagerFragmentBinding) mBinding).vpSettings.getCurrentItem())).getSettingsManager();
        } else {
            return mManager;
        }
    }

    @Override
    public Activity getParentActivity() {
        return getActivity();
    }

    @Override
    public ViewDataBinding getBinding() {
        return mBinding;
    }

    @Override
    public boolean handlesGlobalSetting() {
        return globalSetting;
    }

    @Override
    public Object getCustomSettingsObject() {
        if (globalSetting) {
            return null;
        }
        ISettingsFragmentParent parent = null;
        Fragment p = getParentFragment();
        while (p != null && parent == null) {
            if (p instanceof ISettingsFragmentParent) {
                parent = (ISettingsFragmentParent) p;
            } else {
                p = p.getParentFragment();
            }
        }

        if (parent == null && getActivity() instanceof ISettingsFragmentParent) {
            parent = (ISettingsFragmentParent) getActivity();
        }
        if (parent == null) {
            throw new RuntimeException(
                    "You need to implement ISettingsFragmentParent in any of the SettingsFragment's parents (either parent fragment or parent activity), if you want to use custom settings and not "
                            + "global settings only!");
        }
        if (parent.getCustomSettingsObject() == null) {
            throw new RuntimeException("You need to provide a custom settings object in your ISettingsFragmentParent, if you want to use custom settings and not global settings only!");
        }
        return parent.getCustomSettingsObject();
    }

    @Override
    public void showMultiLevelSetting(int groupId) {

        ISetup s = setup.copy();
        s.setSettingsStyle(Setup.SettingsStyle.List);
        s.setUseExpandableHeaders(false);

        MultiLevelSingleSettingsGroupActivity.start(getActivity(), groupId, s, globalSetting);
//        Toast.makeText(getActivity(), "Group: " + groupId, Toast.LENGTH_SHORT).show();
    }

    public RecyclerView getRecyclerView() {
        if (mBinding == null) {
            return null;
        }
        if (isViewPager()) {
            return ((SettingsFragment) mPagerAdapter.getFragmentAt(((SettingsViewpagerFragmentBinding) mBinding).vpSettings.getCurrentItem())).getRecyclerView();
        } else {
            return ((SettingsFragmentBinding) mBinding).rvSettings;
        }
    }

    class PagerAdapter extends AdvancedFragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment createItem(int i) {
            return SettingsFragment.createPage(setup, globalSetting, groupIds.get(i));
        }

        @Override
        public int getCount() {
            return groupIds.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mGroups.get(position).getTitle().getText();
        }
    }
}
