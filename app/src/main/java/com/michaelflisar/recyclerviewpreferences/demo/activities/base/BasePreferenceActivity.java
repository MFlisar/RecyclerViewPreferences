package com.michaelflisar.recyclerviewpreferences.demo.activities.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.demo.R;
import com.michaelflisar.recyclerviewpreferences.demo.databinding.ActivityPreferencesBinding;
import com.michaelflisar.recyclerviewpreferences.fragments.SettingsFragment;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsActivity;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsFragment;

import java.util.List;

public abstract class BasePreferenceActivity extends BaseThemedActivity implements ISettingsActivity {

    // Setup
    protected boolean mGlobalSettings = true;
    private ActivityPreferencesBinding mBinding;
    protected ISettingsFragment mSettingsFragment;

    // State
    protected boolean mUseViewPager = true;
    protected boolean mUseExpandableHeaders = true;
    protected boolean mCompact = false;

    protected abstract List<Integer> getPrefGroupIds();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            mUseViewPager = savedInstanceState.getBoolean("mUseViewPager");
            mUseExpandableHeaders = savedInstanceState.getBoolean("mUseExpandableHeaders");
            mCompact = savedInstanceState.getBoolean("mCompact");
        } else {
            mUseViewPager = SettingsManager.get().getState().isUseViewPager();
            mUseExpandableHeaders = SettingsManager.get().getState().isUseExpandableHeaders();
            mCompact = SettingsManager.get().getState().isUseCompact();
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_preferences);
        updateSettings();
        updateView(savedInstanceState, false);
    }

    protected void updateSettings() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mUseViewPager", mUseViewPager);
        outState.putBoolean("mUseExpandableHeaders", mUseExpandableHeaders);
        outState.putBoolean("mCompact", mCompact);
    }

    protected void updateView(Bundle savedInstanceState, boolean reset) {
        if (reset || savedInstanceState == null) {
            mSettingsFragment = SettingsFragment.create(mUseViewPager, mGlobalSettings, mCompact, mUseExpandableHeaders,
                    getPrefGroupIds().toArray(new Integer[getPrefGroupIds().size()]));
            getSupportFragmentManager().beginTransaction().replace(R.id.placeholder, (Fragment) mSettingsFragment).commit();
        } else {
            mSettingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentById(R.id.placeholder);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.menu_use_view_pager).setChecked(mUseViewPager);
        menu.findItem(R.id.menu_use_expandable_headers).setChecked(mUseExpandableHeaders);
        if (mGlobalSettings) {
            menu.findItem(R.id.menu_use_compact_settings).setVisible(false);
        } else {
            menu.findItem(R.id.menu_use_compact_settings).setChecked(mCompact);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_use_view_pager:
                mUseViewPager = !mUseViewPager;
                item.setChecked(mUseViewPager);
                updateView(null, true);
                //mSettingsFragment.setUseViewPager(mUseViewPager);
                break;
            case R.id.menu_use_expandable_headers:
                mUseExpandableHeaders = !mUseExpandableHeaders;
                item.setChecked(mUseExpandableHeaders);
                mSettingsFragment.setUseExpandableHeaders(mUseExpandableHeaders);
                break;
            case R.id.menu_use_compact_settings:
                mCompact = !mCompact;
                item.setChecked(mCompact);
                mSettingsFragment.setUseCompactSettings(mCompact);
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
    public void onColorSelection(@NonNull ColorChooserDialog colorChooserDialog, @ColorInt int color) {
        int id = colorChooserDialog.getArguments().getInt(Definitions.DIALOG_SETTINGS_ID);
        boolean global = colorChooserDialog.getArguments().getBoolean(Definitions.DIALOG_SETTING_IS_GLOBAL);
        mSettingsFragment.getSettingsManager().handleColorSelected(id, this, color, global, getCustomSettingsObject());
//        SettingsFragmentInstanceManager.get().dispatchHandleColorSelected(id, this, color, global);
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog colorChooserDialog) {
        // nothing to do...
    }

    @Override
    public void onNumberSelected(int id, int value, boolean global) {
        mSettingsFragment.getSettingsManager().handleNumberChanged(id, this, Arrays.asList(value), global, getCustomSettingsObject());
//        SettingsFragmentInstanceManager.get().dispatchHandleNumberChanged(id, this, Arrays.asList(value), global);
    }

    @Override
    public void onTextChanged(int id, String value, boolean global) {
        mSettingsFragment.getSettingsManager().handleTextChanged(id, this, value, global, getCustomSettingsObject());
//        SettingsFragmentInstanceManager.get().dispatchHandleNumberChanged(id, this, Arrays.asList(value), global);
    }
    */
}
