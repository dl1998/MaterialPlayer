package com.android.materialplayer.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.materialplayer.R;
import com.android.materialplayer.Settings;
import com.android.materialplayer.activities.MainActivity;
import com.android.materialplayer.adapters.AlbumsAdapter;
import com.android.materialplayer.adapters.ItemClickListener;
import com.android.materialplayer.asyncloaders.AsyncAddAlbums;
import com.android.materialplayer.comparators.AlbumComparator;
import com.android.materialplayer.models.ExtendedAlbum;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by dl1998 on 02.12.17.
 */

public class FragmentAlbums extends AbstractTabFragment {

    private AlbumsAdapter adapter;

    private RecyclerView listAlbums;

    private MenuItem miReversed;

    public static FragmentAlbums newInstance(Context context) {
        FragmentAlbums fragment = new FragmentAlbums();

        fragment.context = context;
        fragment.setArguments(new Bundle());
        fragment.setTitle(context.getString(R.string.albums));

        fragment.adapter = null;
        Log.d("Info", "Create New Instance FragmentAlbums");

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_list, parent, false);

        listAlbums = view.findViewById(R.id.rvList);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (adapter == null) {
            adapter = new AlbumsAdapter(getActivity(), new ArrayList<ExtendedAlbum>());
            int numColumn = 2;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                numColumn = 2;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                numColumn = 3;
            listAlbums.setLayoutManager(new GridLayoutManager(getActivity(), numColumn));
            listAlbums.setAdapter(adapter);

            new AsyncAddAlbums(getActivity(), adapter).execute();
        }

        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                ArrayList<ExtendedAlbum> albums = adapter.getAdapter();
                ExtendedAlbum album = albums.get(position);
                MainActivity activity = (MainActivity) getActivity();
                setHasOptionsMenu(false);
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.switchableLayout, FragmentAlbum.getInstance(album));
                fragmentTransaction.addToBackStack("fragment_album");
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.album_sort_by, menu);
        miReversed = menu.findItem(R.id.action_reversed);

        final Integer ordinal = Settings.preferences.getInt(Settings.ALBUM_ORDER, 0);
        final Boolean reversed = Settings.preferences.getBoolean(Settings.ALBUM_REVERSED, false);

        if (reversed) miReversed.setChecked(true);
        if (ordinal == 0) menu.findItem(R.id.sort_by_album_in_album).setChecked(true);
        else if (ordinal == 1) menu.findItem(R.id.sort_by_no_tracks).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reversed:
                toggleReversed(item);
                break;
            case R.id.sort_by_album_in_album:
            case R.id.sort_by_no_tracks:
                item.setChecked(true);
                final Settings.AlbumOrder order = AlbumComparator.getOrder(item);
                adapter.sort(new Comparator<ExtendedAlbum>() {
                    @Override
                    public int compare(ExtendedAlbum album, ExtendedAlbum album1) {
                        return AlbumComparator.compareAlbum(album, album1, order);
                    }
                });
                if (miReversed.isChecked()) reversed();
                saveSortOrder(order);
        }
        return true;
    }

    private void saveSortOrder(Settings.AlbumOrder order) {
        Settings.preferences.edit().putInt(Settings.ALBUM_ORDER, order.ordinal()).apply();
    }

    private void saveSortReversed(Boolean reversed) {
        Settings.preferences.edit().putBoolean(Settings.ALBUM_REVERSED, reversed).apply();
    }

    private void toggleReversed(MenuItem item) {
        if (item.isChecked()) {
            item.setChecked(false);
            saveSortReversed(false);
        } else {
            item.setChecked(true);
            saveSortReversed(true);
        }
        reversed();
    }

    private void reversed() {
        adapter.reversed();
    }
}
