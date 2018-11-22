package com.android.materialplayer.dao;

import com.android.materialplayer.entity.Artist;

import java.util.List;

/**
 * Created by dl1998 on 06.12.17.
 */

public interface ArtistDAO {

    void add(Artist artist);

    void update(Artist artist);

    void remove(Long id);

    void remove(Artist artist);

    void removeAll();

    Artist findById(Long id);

    List<Artist> getAll();

}
