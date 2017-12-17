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
import com.android.materialplayer.models.Album;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dl1998 on 04.12.17.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Album> albums;

    public AlbumsAdapter(Context context, ArrayList<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_albums, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Album album = albums.get(position);

        holder.tvArtist.setText(album.getArtistName());
        holder.tvTitle.setText(album.getAlbumName());
        new ArtLoad(holder).execute(album);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void update(Album album) {
        albums.add(album);
    }

    public void setAdapter(ArrayList<Album> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    private class ArtLoad extends AsyncTask<Album, String, Void> {

        private AlbumsAdapter.ViewHolder holder;

        public ArtLoad(AlbumsAdapter.ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        protected Void doInBackground(Album... params) {
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
            this.ivCover = view.findViewById(R.id.ivAlbumsCover);
            this.tvTitle = view.findViewById(R.id.tvAlbumName);
            this.tvArtist = view.findViewById(R.id.tvAlbumArtist);
            this.footer = view.findViewById(R.id.footerAlbums);
        }
    }
}
