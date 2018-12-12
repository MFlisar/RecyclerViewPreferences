package com.michaelflisar.recyclerviewpreferences.classes;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Michael on 21.05.2017.
 */

public abstract class AdvancedFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    private HashMap<Integer, Fragment> mPageReferenceMap = new HashMap<>();

    public AdvancedFragmentStatePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = createItem(position);
        mPageReferenceMap.put(position, f);
        return f;
    }

    protected abstract Fragment createItem(int position);

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mPageReferenceMap.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }

    public Fragment getFragmentAt(int position) {
        return mPageReferenceMap.get(position);
    }

    public Collection<Fragment> getAllCachedFragments() {
        return mPageReferenceMap.values();
    }
}