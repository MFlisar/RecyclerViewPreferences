package com.michaelflisar.recyclerviewpreferences.classes;


import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dependency {

    public enum Type {
        Hide,
        Disable,
        Both
    }

    private IDependencyValidator mValidator;
    private List<ISetting> mParents;

    public static Dependency getBooleanDependency(ISetting parent, Type type) {
        return getBooleanDependency(parent, type, false);
    }

    public static Dependency getBooleanDependency(ISetting parent, Type type, final boolean invert) {
        return new Dependency(new Dependency.IDependencyValidator() {
            @Override
            public boolean isEnabled(ISetting parentSetting, boolean global, Object customSettingsObject) {
                if (type == Type.Disable || type == Type.Both) {
                    boolean check = check(parentSetting, global, customSettingsObject);
                    return invert ? !check : check;
                }
                return true;
            }

            @Override
            public boolean isVisible(ISetting parentSetting, boolean global, Object customSettingsObject) {
                if (type == Type.Hide || type == Type.Both) {
                    boolean check = check(parentSetting, global, customSettingsObject);
                    return invert ? !check : check;
                }
                return true;
            }

            private boolean check(ISetting parentSetting, boolean global, Object customSettingsObject) {
                boolean value = ((Boolean) parentSetting.getValue(customSettingsObject, global));
                if (global) {
                    return value;
                } else {
                    return parentSetting.getCustomEnabled(customSettingsObject) ? value : (Boolean) parentSetting.getValue(customSettingsObject, true);
                    //return parentSetting.getCustomEnabled(customSettingsObject) && value;
                }

            }
        }, parent);
    }

    public static Dependency getIntegerDependency(ISetting parent, Type type, Util.IPredicate<Integer> validator) {
        return getIntegerDependency(parent, type, false, validator);
    }

    public static Dependency getIntegerDependency(ISetting parent, Type type, final boolean invert, Util.IPredicate<Integer> validator) {
        return new Dependency(new Dependency.IDependencyValidator() {
            @Override
            public boolean isEnabled(ISetting parentSetting, boolean global, Object customSettingsObject) {
                if (type == Type.Disable || type == Type.Both) {
                    Integer check = check(parentSetting, global, customSettingsObject);
                    return invert ? !validator.apply(check) : validator.apply(check);
                }
                return true;
            }

            @Override
            public boolean isVisible(ISetting parentSetting, boolean global, Object customSettingsObject) {
                if (type == Type.Hide || type == Type.Both) {
                    Integer check = check(parentSetting, global, customSettingsObject);
                    return invert ? !validator.apply(check) : validator.apply(check);
                }
                return true;
            }

            private Integer check(ISetting parentSetting, boolean global, Object customSettingsObject) {
                Integer value = ((Integer) parentSetting.getValue(customSettingsObject, global));
                if (global) {
                    return value;
                } else {
                    return parentSetting.getCustomEnabled(customSettingsObject) ? value : (Integer) parentSetting.getValue(customSettingsObject, true);
                }
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
        for (ISetting p : mParents) {
            if (!mValidator.isEnabled(p, global, customSettingsObject)) {
                return false;
            }
        }
        return true;
    }

    public final boolean isVisible(boolean global, Object customSettingsObject) {
        for (ISetting p : mParents) {
            if (!mValidator.isVisible(p, global, customSettingsObject)) {
                return false;
            }
        }
        return true;
    }

    public interface IDependencyValidator {

        /**
         * @param parentSetting the setting the dependencies depent on
         * @return true, if all dependencies are enabled, false otherwise
         */
        boolean isEnabled(ISetting parentSetting, boolean global, Object customSettingsObject);

        /**
         * @param parentSetting the setting the dependencies depent on
         * @return true, if all dependencies are visible, false otherwise
         */
        boolean isVisible(ISetting parentSetting, boolean global, Object customSettingsObject);
    }
}
