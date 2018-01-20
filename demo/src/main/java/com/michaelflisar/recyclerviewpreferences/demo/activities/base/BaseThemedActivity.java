package com.michaelflisar.recyclerviewpreferences.demo.activities.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;
import com.michaelflisar.recyclerviewpreferences.demo.R;

/**
 * Created by flisar on 23.08.2017.
 */

public class BaseThemedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(SettingsManager.get().getState().isDarkTheme() ? R.style.AppThemeDark : R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }
}
