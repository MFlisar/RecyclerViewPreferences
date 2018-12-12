package com.michaelflisar.recyclerviewpreferences.simpledemo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.michaelflisar.recyclerviewpreferences.SettingsFragment;
import com.michaelflisar.recyclerviewpreferences.defaults.Setup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // --------------------------
    // Setup for this Activity
    // --------------------------

    public final ArrayList<Integer> mSettingsGroups = App.EXAMPLE_SETTING_GROUPS; // we show all settings in this activity, be providing another list we could show part of them only for example
    private SettingsFragment mSettingsFragment;
    private boolean mGlobalSettings = true; // as we use global settings persistet in our defined preferences file only
    private Setup mSetup = App.SETUP;  // we use global settings only, so we use the single setup instance from the app

    // --------------------------
    // Activity
    // --------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        updateView(savedInstanceState, false);
    }

    protected void updateView(Bundle savedInstanceState, boolean reset) {
        if (reset || savedInstanceState == null) {
            mSettingsFragment = SettingsFragment.create(mSetup, mGlobalSettings, mSettingsGroups.toArray(new Integer[mSettingsGroups.size()]));
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, mSettingsFragment).commit();
        } else {
            mSettingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}