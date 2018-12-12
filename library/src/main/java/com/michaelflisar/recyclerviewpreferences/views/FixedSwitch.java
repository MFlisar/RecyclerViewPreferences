package com.michaelflisar.recyclerviewpreferences.views;

import android.content.Context;
import androidx.appcompat.widget.SwitchCompat;
import android.util.AttributeSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by flisar on 23.05.2017.
 */

public class FixedSwitch extends SwitchCompat
{
    public FixedSwitch(Context context) {
        super(context);
        initHack();
    }

    public FixedSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHack();
    }

    public FixedSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHack();
    }

    private Method methodCancelPositionAnimator = null;
    private Method methodSetThumbPosition = null;

    private void initHack(){
        try {
            methodCancelPositionAnimator = SwitchCompat.class.getDeclaredMethod("cancelPositionAnimator");
            methodSetThumbPosition = SwitchCompat.class.getDeclaredMethod("setThumbPosition", float.class);
            methodCancelPositionAnimator.setAccessible(true);
            methodSetThumbPosition.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void setChecked(boolean checked, boolean animate){
        // Java does not support super.super.xxx calls, a call to the SwitchCompat default setChecked method is needed.
        super.setChecked(checked);
        if(!animate) {

            // See original SwitchCompat source:
            // Calling the super method may result in setChecked() getting called
            // recursively with a different value, so load the REAL value...
            checked = isChecked();

            // Cancel any running animations (started by super.setChecked()) and immediately move the thumb to the new_settings position
            try {
                if(methodCancelPositionAnimator != null && methodSetThumbPosition != null) {
                    methodCancelPositionAnimator.invoke(this);
                    methodSetThumbPosition.invoke(this, checked ? 1 : 0);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
