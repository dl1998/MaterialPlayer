package com.android.materialplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.materialplayer.R;
import com.android.materialplayer.Settings;
import com.android.materialplayer.models.ExtendedSong;
import com.android.materialplayer.models.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

/**
 * Created by dl1998 on 02.01.18.
 */

public class TracksInAlbumAdapter extends AbstractRecyclerViewAdapter<TracksInAlbumAdapter.ViewHolder, Song> {

    private Context context;
    private ArrayList<Song> songs;
    private boolean firstStart;

    private ItemClickListener listener = new ItemClickListener() {
        @Override
        public void onClick(View view, int position, boolean isLongClick) {
            if (!isLongClick) {
                if (firstStart) {
                    ArrayList<ExtendedSong> extendedSongs = new ArrayList<>();
                    for (Song song : TracksInAlbumAdapter.this.songs) {
                        extendedSongs.add(new ExtendedSong(song));
                    }
                    Settings.musicService.setList(extendedSongs);
                    firstStart = false;
                }
                if (Settings.musicService != null) {
                    Settings.musicService.stopSong();
                    Settings.musicService.setSong(position);
                    Settings.musicService.playSong();
                        /*MainActivity activity = (MainActivity) context;
                        activity.setSong(extendedSongs.get(position));
                        activity.initSeekBar();*/
                }
            }
        }
    };

    public TracksInAlbumAdapter(Context context, ArrayList<Song> songs) {
        super(songs);
        this.context = context;
        this.songs = songs;
        this.firstStart = true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tracks_in_album, parent, false);

        Collections.sort(this.songs, new Comparator<Song>() {
            @Override
            public int compare(Song song, Song song1) {
                return song.getTrackNumber().compareTo(song1.getTrackNumber());
            }
        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = songs.get(position);

        if (song.getTrackNumber() != 0)
            holder.tvSongNumber.setText(String.valueOf(song.getTrackNumber() % 1000));
        else holder.tvSongNumber.setText("-");
        holder.tvSongName.setText(song.getSongName());

        String time = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(song.getDuration()),
                TimeUnit.MILLISECONDS.toSeconds(song.getDuration()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(song.getDuration()))
        );

        holder.tvSongDuration.setText(time);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public void setAdapter(ArrayList<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvSongNumber;
        private TextView tvSongName;
        private TextView tvSongDuration;
        private View footer;

        public ViewHolder(View view) {
            super(view);

            tvSongNumber = view.findViewById(R.id.tvSongNumber);
            tvSongName = view.findViewById(R.id.tvTrackName);
            tvSongDuration = view.findViewById(R.id.tvTrackDuration);
            footer = view.findViewById(R.id.footerMiniSongs);

            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            TracksInAlbumAdapter.this.listener.onClick(view, getAdapterPosition(), false);
        }
    }
}
