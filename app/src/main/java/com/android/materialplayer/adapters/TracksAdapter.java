package com.android.materialplayer.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.materialplayer.R;
import com.android.materialplayer.models.ExtendedSong;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by dl1998 on 05.12.17.
 */

public class TracksAdapter extends AbstractRecyclerViewAdapter<TracksAdapter.ViewHolder, ExtendedSong> {

    private Context context;
    private List<ExtendedSong> songs;
    private ItemClickListener listener = new ItemClickListener() {
        @Override
        public void onClick(View view, int position, boolean isLongClick) {

        }
    };

    public TracksAdapter(Context context, List<ExtendedSong> songs) {
        super(songs);
        this.context = context;
        this.songs = songs;
    }

    @Override
    public TracksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tracks, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TracksAdapter.ViewHolder holder, int position) {

        holder.setItemClickListener(listener);

        ExtendedSong song = songs.get(position);

        holder.tvTitle.setText(song.getSongName());
        holder.tvArtist.setText(song.getArtistName());
        new ArtLoad(holder).execute(song);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public void add(ExtendedSong song) {
        songs.add(song);
    }

    public void update(int position, ExtendedSong song) {
        songs.set(position, song);
    }

    public void setAdapter(List<ExtendedSong> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public void sort(Comparator<ExtendedSong> comparator) {
        Collections.sort(this.songs, comparator);
        notifyDataSetChanged();
    }

    public void reversed() {
        Collections.reverse(songs);
        notifyDataSetChanged();
    }

    private class ArtLoad extends AsyncTask<ExtendedSong, String, Void> {

        private TracksAdapter.ViewHolder holder;

        public ArtLoad(TracksAdapter.ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        protected Void doInBackground(ExtendedSong... params) {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("file://");
            strBuilder.append(params[0].getArtPath());
            publishProgress(strBuilder.toString());
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Picasso.get()
                    .load(values[0])
                    .fit()
                    .placeholder(R.drawable.headphone)
                    .into(holder.ivCover);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivCover;
        TextView tvTitle;
        TextView tvArtist;
        View footer;

        private ItemClickListener listener;

        ViewHolder(View view) {
            super(view);
            ivCover = view.findViewById(R.id.ivTracksCover);
            tvTitle = view.findViewById(R.id.tvTracksName);
            tvArtist = view.findViewById(R.id.tvTracksArtist);
            footer = view.findViewById(R.id.footerSongs);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }
}
