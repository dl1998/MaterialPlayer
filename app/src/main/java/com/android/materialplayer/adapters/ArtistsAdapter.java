package com.android.materialplayer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.materialplayer.R;
import com.android.materialplayer.Settings;
import com.android.materialplayer.bitmap_converter.RoundedCornersTransform;
import com.android.materialplayer.json_objects.Image;
import com.android.materialplayer.json_objects.LastFM;
import com.android.materialplayer.models.Artist;
import com.google.gson.Gson;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dl1998 on 12.12.17.
 */

public class ArtistsAdapter extends AbstractRecyclerViewAdapter<ArtistsAdapter.ViewHolder, Artist> {

    private Context context;
    private ArrayList<Artist> artists;

    private ItemClickListener listener = new ItemClickListener() {
        @Override
        public void onClick(View view, int position, boolean isLongClick) {

        }
    };

    private Picasso picasso;
    private LruCache<String, Bitmap> imageCache;

    public ArtistsAdapter(Context context, ArrayList<Artist> artists) {
        super(artists);
        this.context = context;
        this.artists = artists;
        this.picasso = Picasso.get();
        //this.imageCache = new LruCache<>();
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
        Transformation transformation = new RoundedCornersTransform(30, 30);
        picasso.load(R.drawable.unknown_artist).transform(transformation).into(holder.ivCover);
        //Picasso.with(context).load(artist.getArtPath()).placeholder(R.drawable.unknown_artist).into(holder.ivCover);
        //new ArtLoad(holder, position).execute(artist);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public ArrayList<Artist> getAdapter() {
        return this.artists;
    }

    public void setAdapter(ArrayList<Artist> artists) {
        this.artists = artists;
        notifyDataSetChanged();
    }

    public void sort(Comparator<Artist> comparator) {
        Collections.sort(this.artists, comparator);
        notifyDataSetChanged();
    }

    public void reversed() {
        Collections.reverse(this.artists);
        notifyDataSetChanged();
    }

    public void setOnClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    private class ArtLoad extends AsyncTask<Artist, String, Void> {

        private ArtistsAdapter.ViewHolder holder;
        private int position;

        public ArtLoad(ArtistsAdapter.ViewHolder holder, int position) {
            this.holder = holder;
            this.position = position;
        }

        @Override
        protected Void doInBackground(Artist... params) {
            /*StringBuilder strBuilder = new StringBuilder();
            if (params[0].getArtPath().isEmpty())strBuilder.append("file://");
            strBuilder.append(params[0].getArtPath());
            publishProgress(strBuilder.toString());*/

            LastFM data = null;
            String URI = String.format(Settings.URI, Settings.Methods.ARTIST_INFO, artists.get(position).getArtistName());

            //this.position = Integer.parseInt(strings[1]);

            //String newUri = String.format(uri, strings[0]);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URI)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();

                int status = response.code();
                Log.d("Status", String.valueOf(response.code()));

                String json = response.body().string();

                Gson gson = new Gson();
                data = gson.fromJson(json, LastFM.class);

                if (data != null) {
                    List<Image> images = data.getArtist().getImage();
                    String artPath = images.get(images.size() - 1).getText();

                    Log.d("Artist", artists.get(position).getArtistName());
                    if (images != null) Log.d("IMG_SRC", artPath);

                    if (status == 200 && !artPath.isEmpty()) publishProgress(artPath);
                }
                //Log.d("Info", data.getArtist().getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            picasso.load(values[0])
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.cover_not_available)
                    .into(holder.ivCover);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView ivCover;
        private TextView tvArtistName;
        private View footer;

        ViewHolder(View view) {
            super(view);
            ivCover = view.findViewById(R.id.ivArtistsCover);
            tvArtistName = view.findViewById(R.id.tvArtistName);
            footer = view.findViewById(R.id.footerArtists);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void onClick(View view) {
            ArtistsAdapter.this.listener.onClick(view, getAdapterPosition(), false);
        }

        public boolean onLongClick(View view) {
            ArtistsAdapter.this.listener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }
}
