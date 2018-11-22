package com.android.materialplayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.materialplayer.R;
import com.android.materialplayer.Settings;
import com.android.materialplayer.activities.MainActivity;
import com.android.materialplayer.adapters.ItemClickListener;
import com.android.materialplayer.adapters.TracksAdapter;
import com.android.materialplayer.asyncloaders.AsyncAddSongs;
import com.android.materialplayer.comparators.SongComparator;
import com.android.materialplayer.models.ExtendedSong;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by dl1998 on 02.12.17.
 */

public class FragmentTracks extends AbstractTabFragment {

    private TracksAdapter adapter;

    private RecyclerView listTracks;
    private LinearLayoutManager manager;

    private Integer position;

    private MenuItem miReversed;

    public static FragmentTracks newInstance(Context context) {
        FragmentTracks fragment = new FragmentTracks();
        fragment.context = context;
        fragment.setArguments(new Bundle());
        fragment.setTitle(context.getString(R.string.tracks));

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_list, parent, false);

        listTracks = view.findViewById(R.id.rvList);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        boolean create = adapter == null;
        if (create) adapter = new TracksAdapter(getActivity(), new ArrayList<ExtendedSong>());
        listTracks.setAdapter(adapter);
        if (manager == null) manager = new LinearLayoutManager(getActivity());
        listTracks.setLayoutManager(manager);

        if (position != null) manager.scrollToPosition(position);

        if (create) new AsyncAddSongs(getActivity(), adapter).execute();

        adapter.setOnItemClickListener(new ItemClickListener() {
            private Boolean firstStart = true;

            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    if (firstStart) {
                        Settings.musicService.setList(Settings.songs);
                        firstStart = false;
                    }
                    if (Settings.musicService != null) {
                        Settings.musicService.stopSong();
                        Settings.musicService.setSong(position);
                        Settings.musicService.playSong();
                        MainActivity activity = (MainActivity) context;
                        activity.initSeekBar();
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        position = manager.findFirstVisibleItemPosition();
        listTracks.setLayoutManager(null);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.song_sort_by, menu);
        miReversed = menu.findItem(R.id.action_reversed);

        final Integer ordinal = Settings.preferences.getInt(Settings.SONG_ORDER, 0);
        final Boolean reversed = Settings.preferences.getBoolean(Settings.SONG_REVERSED, false);

        if (reversed) miReversed.setChecked(true);
        if (ordinal == 0) menu.findItem(R.id.sort_by_title).setChecked(true);
        else if (ordinal == 1) menu.findItem(R.id.sort_by_album).setChecked(true);
        else if (ordinal == 2) menu.findItem(R.id.sort_by_artist).setChecked(true);
        else if (ordinal == 3) menu.findItem(R.id.sort_by_year).setChecked(true);
        else if (ordinal == 4) menu.findItem(R.id.sort_by_duration).setChecked(true);

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reversed:
                toggleReversed(item);
                break;
            case R.id.sort_by_title:
            case R.id.sort_by_album:
            case R.id.sort_by_artist:
            case R.id.sort_by_year:
            case R.id.sort_by_duration:
                item.setChecked(true);
                final Settings.SongOrder order = SongComparator.getOrder(item);
                adapter.sort(new Comparator<ExtendedSong>() {
                    @Override
                    public int compare(ExtendedSong song, ExtendedSong song1) {
                        return SongComparator.compareSong(song, song1, order);
                    }
                });
                if (miReversed.isChecked()) reversed();
                saveSortOrder(order);
                break;
        }

        return true;
    }

    private void saveSortOrder(Settings.SongOrder order) {
        Settings.preferences.edit().putInt(Settings.SONG_ORDER, order.ordinal()).apply();
    }

    private void saveSortReversed(Boolean reversed) {
        Settings.preferences.edit().putBoolean(Settings.SONG_REVERSED, reversed).apply();
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
