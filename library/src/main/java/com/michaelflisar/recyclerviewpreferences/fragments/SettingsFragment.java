package com.michaelflisar.recyclerviewpreferences.fragments;


import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.SettingsGroup;
import com.michaelflisar.recyclerviewpreferences.classes.AdvancedFragmentStatePagerAdapter;
import com.michaelflisar.recyclerviewpreferences.classes.SettingsFragmentManager;
import com.michaelflisar.recyclerviewpreferences.databinding.SettingsFragmentBinding;
import com.michaelflisar.recyclerviewpreferences.databinding.SettingsViewpagerFragmentBinding;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettCallback;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsActivity;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsFragment;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsItem;
import com.michaelflisar.recyclerviewpreferences.utils.SettingsFragmentInstanceManager;
import com.michaelflisar.recyclerviewpreferences.utils.SettingsUtil;
import com.michaelflisar.recyclerviewpreferences.utils.Util;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class SettingsFragment extends Fragment implements ISettCallback, ISettingsFragment {

    public static final String SINGLE_GROUP_ID = "SINGLE_GROUP_ID";

    public static final String USE_VIEW_PAGER = "USE_VIEW_PAGER";
    public static final String GLOBAL_SETTING_ID = "GLOBAL_SETTING_ID";
    public static final String COMPACT_SETTING_ID = "COMPACT_SETTING_ID";
    public static final String GROUP_ID = "GROUP_ID";
    public static final String BUNDLE_FASTADAPTER_PREFIX = "BUNDLE_FASTADAPTER_PREFIX";
    public static final String USE_EXPANDABLE_HEADERS = "USE_EXPANDABLE_HEADERS";

    private PagerAdapter mPagerAdapter;
    private String mFastAdapterBundlePrefix;
    private boolean mUseViewPager;
    private boolean mGlobalSetting;
    private boolean mCompact;
    private ArrayList<Integer> mGroupIds;
    private List<SettingsGroup> mGroups;
    private List<ISetting> mSettings;
    private ViewDataBinding mBinding;
    private FastItemAdapter mFastItemAdapter;
    private List<IItem> mItems;
    private SettingsFragmentManager mManager;
    private boolean mUseExpandableHeaders;

    public static SettingsFragment create(boolean useViewPager, boolean globalSetting, boolean compact, boolean useExpandableHeaders, Integer... groupId) {
        Bundle args = new Bundle();
        args.putBoolean(USE_VIEW_PAGER, useViewPager);
        args.putBoolean(GLOBAL_SETTING_ID, globalSetting);
        args.putBoolean(COMPACT_SETTING_ID, compact);
        args.putBoolean(USE_EXPANDABLE_HEADERS, useExpandableHeaders);
        args.putIntegerArrayList(GROUP_ID, new ArrayList<>(Arrays.asList(groupId)));
        args.putString(BUNDLE_FASTADAPTER_PREFIX, UUID.randomUUID().toString());
        SettingsFragment f = new SettingsFragment();
        f.setArguments(args);
        return f;
    }

    static SettingsFragment createPage(boolean globalSetting, boolean compact, boolean useExpandableHeaders, int groupId) {
        Bundle args = new Bundle();
        args.putInt(SINGLE_GROUP_ID, groupId);
        args.putBoolean(GLOBAL_SETTING_ID, globalSetting);
        args.putBoolean(COMPACT_SETTING_ID, compact);
        args.putBoolean(USE_EXPANDABLE_HEADERS, useExpandableHeaders);
        args.putString(BUNDLE_FASTADAPTER_PREFIX, UUID.randomUUID().toString());
        SettingsFragment f = new SettingsFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(SINGLE_GROUP_ID)) {
            mUseViewPager = false;
            mGroupIds = new ArrayList<>();
            mGroupIds.add(getArguments().getInt(SINGLE_GROUP_ID));
        } else {
            mUseViewPager = getArguments().getBoolean(USE_VIEW_PAGER);
            mGroupIds = getArguments().getIntegerArrayList(GROUP_ID);
        }
        mFastAdapterBundlePrefix = getArguments().getString(BUNDLE_FASTADAPTER_PREFIX);
        mGlobalSetting = getArguments().getBoolean(GLOBAL_SETTING_ID);
        mCompact = getArguments().getBoolean(COMPACT_SETTING_ID);
        mUseExpandableHeaders = getArguments().getBoolean(USE_EXPANDABLE_HEADERS);
        mGroups = new ArrayList<>();
        mSettings = new ArrayList<>();
        for (Integer id : mGroupIds) {
            mGroups.add(SettingsManager.get().getGroupById(id));
            mSettings.addAll(SettingsManager.get().getSettingsByGroupId(id));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mUseViewPager) {
            mPagerAdapter = new PagerAdapter(getChildFragmentManager());
            SettingsViewpagerFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.settings_viewpager_fragment, container, false);
            binding.vpSettings.setAdapter(mPagerAdapter);
            binding.tabs.setupWithViewPager(binding.vpSettings);

            mBinding = binding;
        } else {
            SettingsFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false);
            mFastItemAdapter = new FastItemAdapter<>();

            mManager = new SettingsFragmentManager(getActivity(), mFastItemAdapter, mSettings);

            mItems = SettingsUtil.getSettingItems(mGlobalSetting, getCustomSettingsObject(), mCompact, false /* NO view pager*/, mUseExpandableHeaders, this, SettingsManager.get().getCollapsedSettingIds(),
                    mGroupIds.toArray(new Integer[mGroupIds.size()]));
            SettingsUtil.initAdapter(mFastItemAdapter, mItems, savedInstanceState, mFastAdapterBundlePrefix);

            binding.rvSettings.setLayoutManager(new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.VERTICAL, false));
            binding.rvSettings.setAdapter(mFastItemAdapter);
            SettingsUtil.addDecorator(binding.rvSettings);

            mBinding = binding;
        }
        SettingsFragmentInstanceManager.get().register(this);
        return mBinding.getRoot();
    }

    public void setUseExpandableHeaders(boolean enabled) {
        if (mUseViewPager) {
            Collection<Fragment> fragments = mPagerAdapter.getAllCachedFragments();
            for (Fragment f : fragments) {
                ((SettingsFragment) f).setUseExpandableHeaders(enabled);
            }
        } else {
            mUseExpandableHeaders = enabled;
            getArguments().putBoolean(USE_EXPANDABLE_HEADERS, enabled);
            if (mItems != null) {
                SettingsUtil.updateExpandableHeaders(mItems, enabled);
                SettingsUtil.setExpandedState(mItems, true);
                mFastItemAdapter.notifyAdapterDataSetChanged();
            }
        }
    }

    public void setUseCompactSettings(boolean enabled) {
        if (mUseViewPager) {
            Collection<Fragment> fragments = mPagerAdapter.getAllCachedFragments();
            for (Fragment f : fragments) {
                ((SettingsFragment) f).setUseCompactSettings(enabled);
            }
        } else {
            mCompact = enabled;
            getArguments().putBoolean(COMPACT_SETTING_ID, enabled);
            if (mItems != null) {
                SettingsUtil.updateCompactMode(mItems, enabled);
                mFastItemAdapter.notifyAdapterDataSetChanged();
            }
        }
    }

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
        return mUseViewPager;
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
            mFastItemAdapter.saveInstanceState(outState, mFastAdapterBundlePrefix);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public SettingsFragmentManager getSettingsManager() {
        if (mUseViewPager) {
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
    public Object getCustomSettingsObject() {
        if (mGlobalSetting) {
            return null;
        }
        if (!(getActivity() instanceof ISettingsActivity) || ((ISettingsActivity) getActivity()).getCustomSettingsObject() == null) {
            throw new RuntimeException("You need to implement ISettingsActivity in the SettingsFragment's activity and provide a custom settings object, if you want to use custom settings and not global settings only!");
        }
        return ((ISettingsActivity) getActivity()).getCustomSettingsObject();
    }

    class PagerAdapter extends AdvancedFragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment createItem(int i) {
            return SettingsFragment.createPage(mGlobalSetting, mCompact, mUseExpandableHeaders, mGroupIds.get(i));
        }

        @Override
        public int getCount() {
            return mGroupIds.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mGroups.get(position).getTitle().getText();
        }

    }
}
