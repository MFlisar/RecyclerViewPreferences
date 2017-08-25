package com.michaelflisar.recyclerviewpreferences.base;


import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.SettingsManager;

public class SettingsText {

    private int mResText;
    private String mText;

    public SettingsText(int text) {
        mResText = text;
        mText = null;
    }

    public SettingsText(String text) {
        mResText = -1;
        mText = text;
    }

    public void display(TextView tv) {
        if (mResText != -1) {
            tv.setText(mResText);
        } else {
            tv.setText(mText);
        }
    }

    public String getText() {
        if (mResText != -1) {
            return SettingsManager.get().getContext().getString(mResText);
        } else {
            return mText;
        }
    }
}
