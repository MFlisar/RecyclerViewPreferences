package com.michaelflisar.recyclerviewpreferences.demo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.michaelflisar.recyclerviewpreferences.demo.R;
import com.michaelflisar.recyclerviewpreferences.demo.activities.base.BaseThemedActivity;

/**
 * Created by flisar on 21.08.2017.
 */

public class GlobalAndCustomPreferencesActivityOverview extends BaseThemedActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_global_and_custom);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btGlobal:
                GlobalAndCustomActivity.start(this, true, -1);
                break;
            case R.id.btCustom1:
                GlobalAndCustomActivity.start(this, false, 0);
                break;
            case R.id.btCustom2:
                GlobalAndCustomActivity.start(this, false, 1);
                break;
        }
    }
}