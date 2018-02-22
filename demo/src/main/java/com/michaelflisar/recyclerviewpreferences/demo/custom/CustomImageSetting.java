package com.michaelflisar.recyclerviewpreferences.demo.custom;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaelflisar.recyclerviewpreferences.SettingsFragment;
import com.michaelflisar.recyclerviewpreferences.demo.R;
import com.michaelflisar.recyclerviewpreferences.implementations.SharedPreferenceSettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.IDialogHandler;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettData;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISetting;
import com.michaelflisar.recyclerviewpreferences.interfaces.ISettingsViewHolder;
import com.michaelflisar.recyclerviewpreferences.settings.BaseCustomViewSetting;
import com.michaelflisar.recyclerviewpreferences.utils.DialogUtil;
import com.mikepenz.iconics.typeface.IIcon;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

/**
 * Created by flisar on 25.08.2017.
 */

public class CustomImageSetting {

    // ----------------
    // Data structure
    // ----------------

    public static class Data {
        public Uri image;

        public Data() {
            image = null;
        }

        public Data(Uri uri) {
            image = uri;
        }

        public String getDisplayValue() {
            if (image == null) {
                return "NO IMAGE SELECTED";
            }

            String name = image.getLastPathSegment();
            if (name != null) {
                return name;
            }

            return image.getPath();
        }
    }

    // ----------------
    // Serialisers
    // ----------------

    public static class Serialiser implements SharedPreferenceSettData.ISharedPreferenceStringSerialiser<Data> {

        @Override
        public String toString(Data value) {
            return value.image != null ? value.image.toString() : null;
        }

        @Override
        public Data fromString(String data) {
            return data != null ? new Data(Uri.parse(data)) : new Data();
        }
    }

    // ----------------
    // Setting
    // ----------------

    public static class Setting<CLASS, SettData extends ISettData<Data, CLASS, SettData, VH>, VH extends RecyclerView.ViewHolder &
            ISettingsViewHolder<Data, CLASS, SettData, VH>> extends
            BaseCustomViewSetting<CLASS, Data, SettData, VH> {

        public Setting(Class<CLASS> clazz, SettData settData, int title, IIcon icon) {
            super(clazz, settData, title, icon);
        }

        public Setting(Class<CLASS> clazz, SettData settData, String title, IIcon icon) {
            super(clazz, settData, title, icon);
        }

        @Override
        protected void setDisplayedValue(boolean topView, View v, SettData settData, boolean global, CLASS customSettingsObject) {
            Data data = getValue(customSettingsObject, global);
            // Display data in view that is bound in bindStub
//            return data.getDisplayValue();
            ((TextView) v.findViewById(R.id.tvText)).setText(data.getDisplayValue());

            if (data.image != null) {
                ((ImageView) v.findViewById(R.id.ivImage)).setImageURI(data.image);
            } else {
                ((ImageView) v.findViewById(R.id.ivImage)).setImageDrawable(null);
            }
        }

        @Override
        protected int getCustomView(boolean topValue) {
            return R.layout.image_setting_view;
        }

        @Override
        protected void showCustomDialog(VH vh, Activity activity, ViewDataBinding binding, SettData settData, boolean global, CLASS customSettingsObject) {
            // show a dialog; make sure to register a custom dialog handler in the settings setup once!!!
            // this one then is responsible to save a user changed value
            Data data = getValue(customSettingsObject, global);

            // for this demo we use a single image picker for global values only, so we use this example image picker;
            // normally you would need a dialog you can add the settings id to so that the dialog can dispatch the selection event and
            // the settings manager can handle it
            // for this demo we simply handle the image select event in the activity and as we know
            // that we have a single setting for this event only we also now the settings id to use for disatching the event
            ImagePicker.with(activity)
                    .setCameraOnly(false)
                    .setMultipleMode(false)
                    .setFolderMode(true)
                    .setFolderTitle("Albums")
                    .setImageTitle("Galleries")
                    .setDoneTitle("Done")
                    .setSavePath("ImagePicker")
                    .start();
        }

        @Override
        public int getLayoutTypeId() {
            return R.id.id_adapter_setting_image_item;
        }
    }

    // ----------------
    // Dialog handlers
    // ----------------

    public static class DialogHandler implements IDialogHandler<Data> {

        @Override
        public Class<Data> getHandledClass() {
            return Data.class;
        }

        @Override
        public <CLASS, SD extends ISettData<Data, CLASS, SD, VH>, VH extends RecyclerView.ViewHolder & ISettingsViewHolder<Data, CLASS, SD, VH>> boolean handleCustomEvent(
                SettingsFragment settingsFragment, ISetting setting, int id, Activity activity, Data data, boolean global, CLASS customSettingsObject) {
            // implement your own method, for the demo we use the default one...
            // this one update the value and assumes that the event is equal to the data type which is the case here
            return DialogUtil.handleCustomEvent(settingsFragment, setting, id, activity, data, global, customSettingsObject);
        }
    }
}
