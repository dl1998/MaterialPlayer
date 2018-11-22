package com.android.materialplayer.dao;

import com.android.materialplayer.entity.Album;

import java.util.List;

/**
 * Created by dl1998 on 06.12.17.
 */

public interface AlbumDAO {

    void add(Album album);

    void update(Album album);

    void remove(Long id);

    void remove(Album album);

    void removeAll();

    Album findById(Long id);

    List<Album> getAll();

    List<Album> getAllByArtistId(Long id);

}
