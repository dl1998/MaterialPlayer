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
import com.android.materialplayer.models.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dl1998 on 05.12.17.
 */

public class TracksAdapter extends AbstractRecyclerViewAdapter<TracksAdapter.ViewHolder, Song> {

    private Context context;
    private ArrayList<Song> songs;

    public TracksAdapter(Context context, ArrayList<Song> songs) {
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
        Song song = songs.get(position);

        holder.tvTitle.setText(song.getSongName());
        holder.tvArtist.setText(song.getArtistName());
        //holder.ivCover.setImageDrawable(song.getAlbumArt());
        //holder.ivCover.setImageDrawable(Drawable.createFromPath(new LoadAlbums(context.getContentResolver()).getAlbumArt(song.getAlbumId())));
        //holder.ivCover.setImageResource(R.drawable.cover_not_available);

        //Picasso.with(context).load("file://" + song.getAlbumArt()).placeholder(R.drawable.cover_not_available).into(holder.ivCover);
        new ArtLoad(holder).execute(song);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void add(Song song) {
        songs.add(song);
    }

    public void update(int position, Song song) {
        songs.set(position, song);
    }

    public void setAdapter(ArrayList<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    private class ArtLoad extends AsyncTask<Song, String, Void> {

        private TracksAdapter.ViewHolder holder;

        public ArtLoad(TracksAdapter.ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        protected Void doInBackground(Song... params) {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("file://");
            strBuilder.append(params[0].getArtPath());
            publishProgress(strBuilder.toString());
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Picasso.with(context).load(values[0]).placeholder(R.drawable.cover_not_available).into(holder.ivCover);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle;
        TextView tvArtist;
        View footer;

        ViewHolder(View view) {
            super(view);
            ivCover = view.findViewById(R.id.ivTracksCover);
            tvTitle = view.findViewById(R.id.tvTracksName);
            tvArtist = view.findViewById(R.id.tvTracksArtist);
            footer = view.findViewById(R.id.footerSongs);
        }
    }
}
