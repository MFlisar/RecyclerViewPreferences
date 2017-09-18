package com.michaelflisar.recyclerviewpreferences.demo.activities.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.michaelflisar.recyclerviewpreferences.SettingsFragment;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.defaults.Setup;
import com.michaelflisar.recyclerviewpreferences.demo.R;
import com.michaelflisar.recyclerviewpreferences.demo.databinding.ActivityPreferencesBinding;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsFragment;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsFragmentParent;

import java.util.List;

public abstract class BasePreferenceActivity extends BaseThemedActivity implements ISettingsFragmentParent {

    // Setup
    protected boolean mGlobalSettings = true;
    private ActivityPreferencesBinding mBinding;
    protected ISettingsFragment mSettingsFragment;

    // State
    protected Setup mSetup = new Setup();

    protected abstract List<Integer> getPrefGroupIds();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            mSetup = savedInstanceState.getParcelable("mSetup");
        } else {
            mSetup = (Setup) SettingsManager.get().getState();
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
        outState.putParcelable("mSetup", mSetup);
    }

    protected void updateView(Bundle savedInstanceState, boolean reset) {
        if (reset || savedInstanceState == null) {
            mSettingsFragment = SettingsFragment.create(mSetup, mGlobalSettings, getPrefGroupIds().toArray(new Integer[getPrefGroupIds().size()]));
            getSupportFragmentManager().beginTransaction().replace(R.id.placeholder, (Fragment) mSettingsFragment).commit();
        } else {
            mSettingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentById(R.id.placeholder);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.menu_use_expandable_headers).setChecked(mSetup.isUseExpandableHeaders());
        switch (mSetup.getSettingsStyle()) {
            case ViewPager:
                menu.findItem(R.id.menu_style_view_pager).setChecked(true);
                break;
            case List:
                menu.findItem(R.id.menu_style_list).setChecked(true);
                break;
            case MultiLevelList:
                menu.findItem(R.id.menu_style_list_multi_level).setChecked(true);
                break;
        }


        if (mGlobalSettings) {
            menu.findItem(R.id.menu_use_compact_settings).setVisible(false);
        } else {
            menu.findItem(R.id.menu_use_compact_settings).setChecked(mSetup.getLayoutStyle() == Setup.LayoutStyle.Compact);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_style_view_pager:
                mSetup.setSettingsStyle(Setup.SettingsStyle.ViewPager);
                item.setChecked(true);
                updateView(null, true);
                break;
            case R.id.menu_style_list:
                mSetup.setSettingsStyle(Setup.SettingsStyle.List);
                item.setChecked(true);
                updateView(null, true);
                break;
            case R.id.menu_style_list_multi_level:
                mSetup.setSettingsStyle(Setup.SettingsStyle.MultiLevelList);
                item.setChecked(true);
                updateView(null, true);
                break;
            case R.id.menu_use_expandable_headers:
                mSetup.setUseExpandableHeaders(!mSetup.isUseExpandableHeaders());
                item.setChecked(mSetup.isUseExpandableHeaders());
                mSettingsFragment.setUseExpandableHeaders(mSetup.isUseExpandableHeaders());
                break;
            case R.id.menu_use_compact_settings:
                mSetup.setLayoutStyle(mSetup.getLayoutStyle() == Setup.LayoutStyle.Compact ? Setup.LayoutStyle.Normal : Setup.LayoutStyle.Compact);
                item.setChecked(mSetup.getLayoutStyle() == Setup.LayoutStyle.Compact);
                mSettingsFragment.setUseCompactSettings(mSetup.getLayoutStyle() == Setup.LayoutStyle.Compact);
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
