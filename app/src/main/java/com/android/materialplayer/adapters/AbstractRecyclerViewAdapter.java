package com.android.materialplayer.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by dl1998 on 06.12.17.
 */

public abstract class AbstractRecyclerViewAdapter<T extends RecyclerView.ViewHolder, V> extends RecyclerView.Adapter<T> {
    private List<V> items;

    public AbstractRecyclerViewAdapter(List<V> items) {
        this.items = items;
    }

    public void add(V object) {
        items.add(object);
    }

    public List<V> getData() {
        return this.items;
    }

    public void setData(List<V> items) {
        this.items = items;
    }
}
