package com.android.materialplayer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.materialplayer.R;
import com.android.materialplayer.bitmap_converter.RoundedCornersTransform;
import com.android.materialplayer.models.ExtendedAlbum;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by dl1998 on 04.12.17.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ExtendedAlbum> albums;

    private ItemClickListener listener;

    public AlbumsAdapter(Context context, ArrayList<ExtendedAlbum> albums) {
        this.context = context;
        this.albums = albums;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_albums, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.setItemClickListener(listener);

        ExtendedAlbum album = albums.get(position);

        holder.tvArtist.setText(album.getArtistName());
        holder.tvTitle.setText(album.getAlbumName());
        new ArtLoad(holder).execute(album);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void update(ExtendedAlbum album) {
        albums.add(album);
    }

    public void setAdapter(ArrayList<ExtendedAlbum> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    public ArrayList<ExtendedAlbum> getAdapter() {
        return this.albums;
    }

    public void sort(Comparator<ExtendedAlbum> comparator) {
        Collections.sort(this.albums, comparator);
        notifyDataSetChanged();
    }

    public void reversed() {
        Collections.reverse(albums);
        notifyDataSetChanged();
    }

    private class ArtLoad extends AsyncTask<ExtendedAlbum, String, Void> {

        private AlbumsAdapter.ViewHolder holder;

        public ArtLoad(AlbumsAdapter.ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        protected Void doInBackground(ExtendedAlbum... params) {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("file://");
            strBuilder.append(params[0].getAlbumCover());
            publishProgress(strBuilder.toString());
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            Transformation transformation = new RoundedCornersTransform(30, 30);
            if (values[0].equals("file://null")) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.headphone);
                bitmap = transformation.transform(bitmap);
                holder.ivCover.setImageBitmap(bitmap);
            } else Picasso.get().load(values[0]).transform(transformation).into(holder.ivCover);
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
            this.ivCover = view.findViewById(R.id.ivAlbumsCover);
            this.tvTitle = view.findViewById(R.id.tvAlbumName);
            this.tvArtist = view.findViewById(R.id.tvAlbumArtist);
            this.footer = view.findViewById(R.id.footerAlbums);

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
