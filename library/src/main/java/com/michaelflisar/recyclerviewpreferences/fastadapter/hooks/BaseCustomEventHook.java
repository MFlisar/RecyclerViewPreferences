package com.michaelflisar.recyclerviewpreferences.fastadapter.hooks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.listeners.CustomEventHook;

import java.util.List;

/**
 * Created by Michael on 20.05.2017.
 */

public abstract class BaseCustomEventHook<T extends IItem> extends CustomEventHook<T> {

    // -------------------------
    // Interface
    // -------------------------

    /*
     * bind the desired event hook listener to the view
     * use it like following:
     * view.setOnClickListener(view -> { onHandleCustomEvent(...); });
     */
    public abstract void onBindListener(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<T> fastAdapter);

    public abstract void onEvent(View view, int pos, FastAdapter<T> fastAdapter, T item, RecyclerView.ViewHolder viewHolder);

    // -------------------------
    // Helper function
    // -------------------------

    /*
     * call this from within {@link onBindListener} to tell the hook that the desired event just happened
     */
    protected final void onEventOccurred(View view, RecyclerView.ViewHolder viewHolder, FastAdapter<T> fastAdapter)
    {
        //we get the adapterPosition from the viewHolder
        int pos = fastAdapter.getHolderAdapterPosition(viewHolder);
        //make sure the click was done on a valid item
        if (pos != RecyclerView.NO_POSITION) {
            //we update our item with the changed property
            onEvent(view, pos, fastAdapter, fastAdapter.getItem(pos), viewHolder);
        }
    }

    // -------------------------
    // Default implementation
    // -------------------------

    @Override
    public final void attachEvent(View view, RecyclerView.ViewHolder viewHolder) {
        onBindListener(view, viewHolder, getFastAdapter(viewHolder));
    }

    /*
     * return the target view of the event hook
     * return null, if this ViewHolder should NOT be attached to the event hook
     */
    // public final View onBind(@NonNull RecyclerView.ViewHolder viewHolder)

    @Nullable
    @Override
    public List<View> onBindMany(@NonNull RecyclerView.ViewHolder viewHolder) {
        // TODO...
        return null;
    }
}