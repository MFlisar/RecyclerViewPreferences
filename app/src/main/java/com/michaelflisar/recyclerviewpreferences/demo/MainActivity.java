package com.michaelflisar.recyclerviewpreferences.demo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.defaults.DefaultState;
import com.michaelflisar.recyclerviewpreferences.demo.activities.CustomGlobalPreferencesActivity;
import com.michaelflisar.recyclerviewpreferences.demo.activities.GlobalAndCustomPreferencesActivityOverview;
import com.michaelflisar.recyclerviewpreferences.demo.activities.GlobalSharedPreferencesActivity;
import com.michaelflisar.recyclerviewpreferences.demo.activities.base.BaseThemedActivity;
import com.michaelflisar.recyclerviewpreferences.demo.databinding.ActivityMainBinding;

/**
 * Created by flisar on 21.08.2017.
 */

public class MainActivity extends BaseThemedActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.cbDarkTheme.setChecked(SettingsManager.get().getState().isDarkTheme());
        binding.cbDarkTheme.setOnCheckedChangeListener((compoundButton, b) -> {
            ((DefaultState) SettingsManager.get().getState()).setIsDarkTheme(b);
            recreate();
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btGlobalPreferencesDemo:
                startActivity(new Intent(this, GlobalSharedPreferencesActivity.class));
                break;
            case R.id.btCustomPreferencesDemo:
                startActivity(new Intent(this, CustomGlobalPreferencesActivity.class));
                break;
            case R.id.btGlobalAndCustomPreferencesDemo:
                startActivity(new Intent(this, GlobalAndCustomPreferencesActivityOverview.class));
                break;
        }
    }
}
