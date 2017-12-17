package com.android.materialplayer.dao;

import com.android.materialplayer.entity.Song;

import java.util.List;

/**
 * Created by dl1998 on 05.12.17.
 */

public interface SongDAO {

    void add(Song song);

    void update(Song song);

    void remove(Long id);

    void remove(Song song);

    void removeAll();

    Song findById(Long id);

    List<Song> getAll();

}
