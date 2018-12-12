package com.michaelflisar.recyclerviewpreferences.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.R;
import com.michaelflisar.recyclerviewpreferences.SettingsFragment;
import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.base.SettingsGroup;
import com.michaelflisar.recyclerviewpreferences.databinding.ActivityMultilevelSingleSettingsGroupBinding;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


/**
 * Created by flisar on 24.05.2017.
 */

public class MultiLevelSingleSettingsGroupActivity extends AppCompatActivity
{

    public static final void start(FragmentActivity parent, int groupId, ISetup setup, Boolean globalSetting)
    {
        Intent intent = new Intent(parent, MultiLevelSingleSettingsGroupActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("setup", setup);
        intent.putExtra("globalSetting", globalSetting);
        parent.startActivity(intent);
    }

    int groupId;
    ISetup setup;
    Boolean globalSetting;

    private ActivityMultilevelSingleSettingsGroupBinding mBinding;
    private Fragment f = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Integer theme = SettingsManager.get().getState().getActivityTheme();
        if (theme != null)
        {
            setTheme(theme);
        }
        super.onCreate(savedInstanceState);

        groupId = getIntent().getIntExtra("groupId", -1);
        setup = getIntent().getParcelableExtra("setup");
        globalSetting = getIntent().getBooleanExtra("globalSetting", false);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_multilevel_single_settings_group);

        SettingsGroup group = SettingsManager.get().getGroupById(groupId);

        if (getSupportActionBar() == null)
        {
            setSupportActionBar(mBinding.toolbar);

        }
        else
        {
            // we have already a toolbar in the theme
            mBinding.toolbar.setVisibility(View.GONE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(group.getTitle().getText());

        if (savedInstanceState == null)
        {

            f = SettingsFragment.createMultiLevelPage(
                    setup,
                    globalSetting,
                    groupId
            );

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, f).commit();
        }
        else
        {
            f = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
