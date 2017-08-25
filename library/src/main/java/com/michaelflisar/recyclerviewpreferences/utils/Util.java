package com.michaelflisar.recyclerviewpreferences.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.*;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Util {

    public static int convertDpToPixel(float dp, Context context) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        // float px = dp * (context.getResources().getDisplayMetrics().densityDpi / 160f);
        return (int) px;
    }

    public static int getTextColor() {
        // Default state color from primary_text_dark = android.R.color.background_light
        // Default state color from primary_text_light = android.R.color.background_dark
        return ContextCompat.getColor(SettingsManager.get().getContext(),
                SettingsManager.get().getState().isDarkTheme() ? android.R.color.primary_text_dark : android.R.color.primary_text_light);
    }

//    public static int getTextColorDefaultState() {
//        return getTextColor();
//        // Default state color from primary_text_dark = android.R.color.background_light
//        // Default state color from primary_text_light = android.R.color.background_dark
////        return SettingsManager.get().getState().isDarkTheme() ? Color.WHITE : Color.BLACK;
////        return ContextCompat.getColor(SettingsManager.get().getContext(),
////                SettingsManager.get().getState().isDarkTheme() ? android.R.color.background_light : android.R.color.background_dark);
//    }

    public static IconicsDrawable getCompoundIcon(IIcon icon, int color) {
        return new IconicsDrawable(SettingsManager.get().getContext(), icon).color(color).sizeDp(24).paddingDp(4);
    }

    public static void setCompoundIconLeftOrClear(TextView tv, IIcon icon, int color) {
       if (icon == null) {
           tv.setCompoundDrawables(null, null, null, null);
       } else {
           tv.setCompoundDrawables(getCompoundIcon(icon, color), null, null, null);
       }
    }

    public static void setCircleColorBackground(View view, int color, boolean withBorder, boolean darkTheme) {
        GradientDrawable drawable = null;
        if (withBorder) {
            drawable = (GradientDrawable) ContextCompat.getDrawable(view.getContext(), darkTheme ? R.drawable.circle_with_border_dark : R.drawable.circle_with_border_light);
        } else {
            drawable = (GradientDrawable) ContextCompat.getDrawable(view.getContext(), R.drawable.circle);
        }
        drawable.setColor(color);
        view.setBackground(drawable);
    }

    public static void setTextAppearance(TextView tv, int textAppearance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv.setTextAppearance(textAppearance);
        } else {
            //noinspection deprecation
            tv.setTextAppearance(tv.getContext(), textAppearance);
        }
    }

    public synchronized static <T, S extends Collection<T>> T find(S list, IPredicate<T> predicate) {
        if (list != null) {
            for (T element : list) {
                if (predicate.apply(element)) {
                    return element;
                }
            }
        }
        return null;
    }

    public static <T, S> ArrayList<S> convertList(T[] list, IConverter<T, S> converter) {
        if (list == null)
            return new ArrayList<>();
        return convertList(Arrays.asList(list), converter);
    }

    public static <T, S> ArrayList<S> convertList(Collection<T> list, IConverter<T, S> converter) {
        ArrayList<S> result = new ArrayList<>();
        if (list != null) {
            for (T item : list) {
                result.add(converter.convert(item));
            }
        }
        return result;
    }

    public synchronized static <T, S> int indexOf(List<S> list, IConverter<S, T> converter, IPredicate<T> predicate) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (predicate.apply(converter.convert(list.get(i)))) {
                    return i;
                }
            }
        }
        return -1;
    }

    public synchronized static <T, S> int indexOf(List<S> list, IPredicate<S> predicate) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (predicate.apply(list.get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    public synchronized static <T, S> int indexOf(S[] list, IPredicate<S> predicate) {
        if (list != null) {
            return indexOf(Arrays.asList(list), predicate);
        }
        return -1;
    }

    public interface IPredicate<T> {
        boolean apply(T type);
    }

    public interface IConverter<S, T> {
        T convert(S data);
    }
}
