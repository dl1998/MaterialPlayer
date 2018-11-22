package com.android.materialplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.materialplayer.R;
import com.android.materialplayer.models.Album;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dl1998 on 29.12.17.
 */

public class MiniAlbumAdapter extends AbstractRecyclerViewAdapter<MiniAlbumAdapter.ViewHolder, Album> {

    private Context context;
    private List<Album> albums;

    public MiniAlbumAdapter(Context context, List<Album> albums) {
        super(albums);
        this.context = context;
        this.albums = albums;
    }

    @Override
    public MiniAlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_albums_list, parent, false);

        return new MiniAlbumAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MiniAlbumAdapter.ViewHolder holder, int position) {
        Album album = albums.get(position);

        Integer songCount = album.getSongsCount();
        String result = String.valueOf(songCount);
        if (songCount.equals(1)) result += " " + context.getResources().getString(R.string.song);
        else result += " " + context.getResources().getString(R.string.songs);

        holder.tvAlbumName.setText(album.getAlbumName());
        holder.tvAlbumSongs.setText(result);
        Picasso.get()
                .load("file://" + album.getAlbumCover())
                .placeholder(R.drawable.cover_not_available)
                .into(holder.ivAlbumCover);
    }

    @Override
    public int getItemCount() {
        return this.albums.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAlbumCover;
        TextView tvAlbumName;
        TextView tvAlbumSongs;
        View footer;

        ViewHolder(View view) {
            super(view);

            ivAlbumCover = view.findViewById(R.id.ivMiniAlbumCover);
            tvAlbumName = view.findViewById(R.id.tvMiniAlbumName);
            tvAlbumSongs = view.findViewById(R.id.tvMiniAlbumSongs);
            footer = view.findViewById(R.id.footerMiniAlbums);
        }
    }
}
