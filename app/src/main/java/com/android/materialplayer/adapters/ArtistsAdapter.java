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
import com.android.materialplayer.models.Artist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dl1998 on 12.12.17.
 */

public class ArtistsAdapter extends AbstractRecyclerViewAdapter<ArtistsAdapter.ViewHolder, Artist> {

    private Context context;
    private ArrayList<Artist> artists;

    public ArtistsAdapter(Context context, ArrayList<Artist> artists) {
        super(artists);
        this.context = context;
        this.artists = artists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_artists, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Artist artist = artists.get(position);

        holder.tvArtistName.setText(artist.getArtistName());
        new ArtLoad(holder).execute(artist);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void setAdapter(ArrayList<Artist> artists) {
        this.artists = artists;
        notifyDataSetChanged();
    }

    private class ArtLoad extends AsyncTask<Artist, String, Void> {

        private ArtistsAdapter.ViewHolder holder;

        public ArtLoad(ArtistsAdapter.ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        protected Void doInBackground(Artist... params) {
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

        private ImageView ivCover;
        private TextView tvArtistName;
        private View footer;

        public ViewHolder(View view) {
            super(view);
            ivCover = view.findViewById(R.id.ivArtistsCover);
            tvArtistName = view.findViewById(R.id.tvArtistName);
            footer = view.findViewById(R.id.footerArtists);
        }
    }
}
