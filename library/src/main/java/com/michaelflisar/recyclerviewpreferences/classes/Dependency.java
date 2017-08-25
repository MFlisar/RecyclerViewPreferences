package com.michaelflisar.recyclerviewpreferences.classes;


import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dependency {

    private IDependencyValidator mValidator;
    private List<ISetting> mParents;

    public static Dependency getBooleanDependency(ISetting parent) {
        return new Dependency(new Dependency.IDependencyValidator() {
            @Override
            public boolean isEnabled(ISetting parentSetting, boolean global, Object customSettingsObject) {
                return ((Boolean) parentSetting.getValue(customSettingsObject, global));
            }

            @Override
            public boolean isVisible(ISetting parentSetting, boolean global, Object customSettingsObject) {
                return isEnabled(parentSetting, global, customSettingsObject);
            }
        }, parent);
    }

    public Dependency(IDependencyValidator validator, ISetting... parents) {

        mValidator = validator;
        mParents = new ArrayList<>();
        mParents.addAll(Arrays.asList(parents));
    }

    public boolean dependsOn(int id) {
        return Util.indexOf(mParents, setting -> setting.getSettingId() == id) != -1;
    }

    public final boolean isEnabled(boolean global, Object customSettingsObject) {
        for (ISetting p : mParents)
        {
            if (!mValidator.isEnabled(p, global, customSettingsObject))
                return false;
        }
        return true;
    }

    public final boolean isVisible(boolean global, Object customSettingsObject) {
        for (ISetting p : mParents)
        {
            if (!mValidator.isVisible(p, global, customSettingsObject))
                return false;
        }
        return true;
    }

    public interface IDependencyValidator {

        /**
         *
         * @param parentSetting the setting the dependencies depent on
         * @return true, if all dependencies are enabled, false otherwise
         */
        boolean isEnabled(ISetting parentSetting, boolean global, Object customSettingsObject);

        /**
         *
         * @param parentSetting the setting the dependencies depent on
         * @return true, if all dependencies are visible, false otherwise
         */
        boolean isVisible(ISetting parentSetting, boolean global, Object customSettingsObject);
    }
}
