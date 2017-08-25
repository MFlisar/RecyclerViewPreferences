package com.michaelflisar.recyclerviewpreferences.base;

/**
 * Created by Michael on 16.05.2017.
 */

public class MultiSettingData
{
    public int mDefaultId;
    public int mCustomId;
    public Integer mText;

    public MultiSettingData(int defaultId, int customId, Integer text)
    {
        mDefaultId = defaultId;
        mCustomId = customId;
        mText = text;
    }

    public final int getDefaultId()
    {
        return mDefaultId;
    }

    public final int getCustomId()
    {
        return mCustomId;
    }

    public final int getText()
    {
        return mText;
    }
}
