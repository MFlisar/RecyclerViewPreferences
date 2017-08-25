package com.michaelflisar.recyclerviewpreferences.demo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.michaelflisar.recyclerviewpreferences.demo.SettingsDefinitions;
import com.michaelflisar.recyclerviewpreferences.demo.activities.base.BasePreferenceActivity;
import com.michaelflisar.recyclerviewpreferences.demo.classes.DemoFolder;
import com.michaelflisar.recyclerviewpreferences.demo.classes.DemoInMemoryStorage;

import java.util.List;

/**
 * Created by flisar on 21.08.2017.
 */

public class GlobalAndCustomActivity extends BasePreferenceActivity {

    public static void start(Activity activity, boolean global, int index) {
        Intent intent = new Intent(activity, GlobalAndCustomActivity.class);
        intent.putExtra("global", global);
        intent.putExtra("index", index);
        activity.startActivity(intent);
    }

    private DemoFolder mFolder;

    @Override
    protected List<Integer> getPrefGroupIds() {
        return SettingsDefinitions.GLOBAL_AND_CUSTOM_IN_MEMORY_GROUPS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setSubtitle(mGlobalSettings ? "Global folder settings" : ("Folder " + (mFolder.getIndex() + 1) + " settings"));
    }

    @Override
    protected void updateSettings() {
        // we don't need the view pager here...
        mUseViewPager = false;
        mGlobalSettings = getIntent().getBooleanExtra("global", false);

        int index = getIntent().getIntExtra("index", -1);
        if (index != -1) {
            mFolder = DemoInMemoryStorage.getAndRememberFolder(index);
        }

    }

    @Override
    public Object getCustomSettingsObject() {
        return mFolder;
    }
}
